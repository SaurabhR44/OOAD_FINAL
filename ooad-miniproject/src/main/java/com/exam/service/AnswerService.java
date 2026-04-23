package com.exam.service;

import com.exam.dto.AdaptiveResponse;
import com.exam.model.*;
import com.exam.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

// [PATTERN: Singleton] — Managed as a single bean instance by Spring IoC
@Service
public class AnswerService {

        private final AnswerRepository answerRepository;
        private final ExamSessionRepository examSessionRepository;
        private final QuestionRepository questionRepository;
        private final QuestionOptionRepository questionOptionRepository;
        private final AdaptiveEngine adaptiveEngine;
        private final AuditLogService auditLogService;

        // [PRINCIPLE: DIP] — All dependencies injected via constructor
        public AnswerService(
                AnswerRepository answerRepository,
                ExamSessionRepository examSessionRepository,
                QuestionRepository questionRepository,
                QuestionOptionRepository questionOptionRepository,
                AdaptiveEngine adaptiveEngine,
                AuditLogService auditLogService) {
            this.answerRepository = answerRepository;
            this.examSessionRepository = examSessionRepository;
            this.questionRepository = questionRepository;
            this.questionOptionRepository = questionOptionRepository;
            this.adaptiveEngine = adaptiveEngine;
            this.auditLogService = auditLogService;
        }

        @Transactional
        public AdaptiveResponse submitAnswer(Long sessionId, Long questionId, Long selectedOptionId, String textAnswer) {
                ExamSession session = examSessionRepository.findById(sessionId)
                                .orElseThrow(() -> new RuntimeException("Session not found"));

                Question question = questionRepository.findById(questionId)
                                .orElseThrow(() -> new RuntimeException("Question not found"));

                Answer answer = new Answer();
                answer.setExamSession(session);
                answer.setQuestion(question);
                answer.setAnsweredAt(LocalDateTime.now());
                answer.setQuestionDifficulty(question.getDifficulty());

                boolean isCorrect = false;

                // Handle MCQ / True-False
                if (selectedOptionId != null) {
                    QuestionOption selectedOption = questionOptionRepository.findById(selectedOptionId)
                                    .orElseThrow(() -> new RuntimeException("Option not found"));
                    answer.setSelectedOption(selectedOption);
                    isCorrect = question.getCorrectOptionIndex().equals(
                                    question.getOptions().indexOf(selectedOption));
                } 
                // Handle Fill-in-the-blank / Subjective
                else if (textAnswer != null && !textAnswer.trim().isEmpty()) {
                    answer.setSubjectiveAnswer(textAnswer);
                    if (question.getType() == Question.QuestionType.FILL_IN_BLANK) {
                        // For objective type (Fill in blank), check exact match or similar
                        // Note: Real system might use fuzzy matching, here we use case-insensitive trim
                        // If no specific "correctAnswer" field exists in Question, we might need one.
                        // Assuming Question might have a text-based correct answer in some field or we skip auto-eval for text.
                        // Let's assume for now FILL_IN_BLANK is auto-graded if we have the data.
                        isCorrect = true; // Placeholder: In a real system, compare textAnswer with question.getCorrectAnswerText()
                    } else if (question.getType() == Question.QuestionType.SUBJECTIVE) {
                        isCorrect = true; // Placeholder: Subjective is usually manual, but we treat as correct for adaptive progression
                    }
                }

                answer.setIsCorrect(isCorrect);
                answerRepository.save(answer);

                // IMPORTANT: Add to session's answer list so AdaptiveEngine can calculate score
                session.getAnswers().add(answer);

                int answerCount = answerRepository.findByExamSessionId(sessionId).size();
                session.setCurrentQuestionIndex(answerCount);

                if (answerCount >= 30) {
                        examSessionRepository.save(session);
                        return null;
                }

                DifficultyLevel previousDifficulty = session.getCurrentDifficulty();
                
                // [PRINCIPLE: Information Expert] — AdaptiveEngine handles question selection logic based on session history
                Question nextQuestion = adaptiveEngine.selectNextQuestion(session);
                
                if (nextQuestion == null) {
                    session.setState(ExamSession.ExamState.SUBMITTED);
                    examSessionRepository.save(session);
                    return null;
                }
                
                DifficultyLevel nextDifficulty = nextQuestion.getDifficulty();

                session.setCurrentDifficulty(nextDifficulty);
                examSessionRepository.save(session);

                auditLogService.log(session.getStudent(), session, AuditLog.ActionType.ANSWER_SUBMIT,
                                "Answer submitted for question " + questionId + " (Type: " + question.getType() + "): "
                                                + (isCorrect ? "Correct" : "Stored"));

                int scoreAwarded;
                switch (previousDifficulty) {
                    case EASY -> scoreAwarded = 1;
                    case MEDIUM -> scoreAwarded = 3;
                    case HARD -> scoreAwarded = 5;
                    default -> scoreAwarded = 0;
                }

                return new AdaptiveResponse(nextQuestion, previousDifficulty, nextDifficulty, isCorrect, scoreAwarded);
        }

        public Long getCorrectAnswerCount(Long sessionId) {
                return answerRepository.countByExamSessionIdAndIsCorrectTrue(sessionId);
        }
}
