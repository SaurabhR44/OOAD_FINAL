package com.exam.service;

import com.exam.model.*;
import com.exam.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResultService {

    @Autowired
    private ExamResultRepository examResultRepository;

    @Autowired
    private ExamSessionRepository examSessionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Transactional
    public ExamResult evaluateExam(Long sessionId) {
        ExamSession session = examSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.getState() != ExamSession.ExamState.SUBMITTED) {
            throw new RuntimeException("Exam not submitted yet");
        }

        // Calculate scores
        Long correctCount = answerRepository.countByExamSessionIdAndIsCorrectTrue(sessionId);
        Long totalQuestions = (long) session.getAnswers().size();

        // Calculate difficulty-based scores
        int rawScore = 0;
        double normalizedScore = 0.0;
        int easyCorrect = 0, mediumCorrect = 0, hardCorrect = 0;

        for (Answer answer : session.getAnswers()) {
            if (answer.getIsCorrect()) {
                rawScore++;

                // Difficulty multipliers
                double multiplier = switch (answer.getQuestionDifficulty()) {
                    case EASY -> 1.0;
                    case MEDIUM -> 1.2;
                    case HARD -> 1.5;
                };

                normalizedScore += multiplier;

                // Count by difficulty
                switch (answer.getQuestionDifficulty()) {
                    case EASY -> easyCorrect++;
                    case MEDIUM -> mediumCorrect++;
                    case HARD -> hardCorrect++;
                }
            }
        }

        double percentage = (correctCount.doubleValue() / totalQuestions) * 100;

        // Determine competency
        ExamResult.FinalCompetency competency;
        if (normalizedScore >= totalQuestions * 1.3) {
            competency = ExamResult.FinalCompetency.EXPERT;
        } else if (normalizedScore >= totalQuestions * 1.1) {
            competency = ExamResult.FinalCompetency.ADVANCED;
        } else if (normalizedScore >= totalQuestions * 0.8) {
            competency = ExamResult.FinalCompetency.INTERMEDIATE;
        } else {
            competency = ExamResult.FinalCompetency.BEGINNER;
        }

        // Create result
        ExamResult result = new ExamResult();
        result.setExamSession(session);
        result.setStudent(session.getStudent());
        result.setExam(session.getExam());
        result.setRawScore(rawScore);
        result.setNormalizedScore(normalizedScore);
        result.setPercentage(percentage);
        result.setTotalQuestions(totalQuestions.intValue());
        result.setCorrectAnswers(correctCount.intValue());
        result.setEasyCorrect(easyCorrect);
        result.setMediumCorrect(mediumCorrect);
        result.setHardCorrect(hardCorrect);
        result.setCompetency(competency);
        result.setIsPassed(percentage >= session.getExam().getPassingScore());
        result.setStatus(ExamResult.ResultStatus.EVALUATED);
        result.setEvaluatedAt(LocalDateTime.now());

        // Update session state
        session.setState(ExamSession.ExamState.EVALUATED);
        examSessionRepository.save(session);

        return examResultRepository.save(result);
    }

    @Transactional
    public ExamResult publishResult(Long resultId) {
        ExamResult result = examResultRepository.findById(resultId)
                .orElseThrow(() -> new RuntimeException("Result not found"));

        result.setStatus(ExamResult.ResultStatus.PUBLISHED);
        result.setPublishedAt(LocalDateTime.now());

        return examResultRepository.save(result);
    }

    public List<ExamResult> getResultsBySrn(String srn) {
        Student student = studentRepository.findBySrn(srn)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return examResultRepository.findByStudent(student);
    }

    @Autowired
    private StudentRepository studentRepository;
}
