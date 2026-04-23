package com.exam.service;

import com.exam.dto.AdaptiveResponse;
import com.exam.model.*;
import com.exam.repository.AnswerRepository;
import com.exam.repository.ExamSessionRepository;
import com.exam.repository.QuestionRepository;
import com.exam.service.strategy.DifficultyStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdaptiveExamService {

    private final QuestionRepository questionRepository;
    private final ExamSessionRepository examSessionRepository;
    private final AnswerRepository answerRepository;
    private final DifficultyStrategy difficultyStrategy;

    public AdaptiveExamService(QuestionRepository questionRepository,
                               ExamSessionRepository examSessionRepository,
                               AnswerRepository answerRepository,
                               @Qualifier("mediumStrategy") DifficultyStrategy difficultyStrategy) {
        this.questionRepository = questionRepository;
        this.examSessionRepository = examSessionRepository;
        this.answerRepository = answerRepository;
        this.difficultyStrategy = difficultyStrategy;
    }

    @Transactional
    public AdaptiveResponse submitAnswerAndGetNext(Long sessionId, Long currentQuestionId, Long submittedOptionId) {
        ExamSession session = examSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        Question currentQuestion = questionRepository.findById(currentQuestionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        boolean isCorrect = currentQuestion.getCorrectOptionIndex() != null &&
                currentQuestion.getOptions().stream()
                        .filter(opt -> opt.getId().equals(submittedOptionId))
                        .findFirst()
                        .map(opt -> currentQuestion.getOptions().indexOf(opt) == currentQuestion.getCorrectOptionIndex())
                        .orElse(false);

        Answer answer = new Answer();
        answer.setExamSession(session);
        answer.setQuestion(currentQuestion);
        answer.setIsCorrect(isCorrect);
        answer.setAnsweredAt(LocalDateTime.now());
        answer.setQuestionDifficulty(currentQuestion.getDifficulty());
        session.getAnswers().add(answer);

        DifficultyLevel currentDifficulty = session.getCurrentDifficulty();
        DifficultyLevel nextDifficulty = difficultyStrategy.adjust(session);
        session.setCurrentDifficulty(nextDifficulty);

        examSessionRepository.save(session);

        // Get next question using direct topic lookup (fail-safe)
        String topic = session.getExam().getTopic();
        List<Long> answeredIds = session.getAnswers().stream()
                .map(a -> a.getQuestion().getId())
                .collect(Collectors.toList());

        Question nextQuestion = questionRepository.findAll().stream()
                .filter(q -> q.getTopic().equalsIgnoreCase(topic))
                .filter(q -> !answeredIds.contains(q.getId()))
                .findFirst()
                .orElse(null);

        int scoreAwarded = switch (currentDifficulty) {
            case EASY -> 1;
            case MEDIUM -> 3;
            case HARD -> 5;
        };

        return new AdaptiveResponse(nextQuestion, currentDifficulty, nextDifficulty, isCorrect, scoreAwarded);
    }
}
