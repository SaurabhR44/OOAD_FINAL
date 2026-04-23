package com.exam.service;

import com.exam.model.Answer;
import com.exam.model.DifficultyLevel;
import com.exam.model.ExamSession;
import com.exam.model.Question;
import com.exam.repository.QuestionRepository;
import com.exam.service.strategy.DifficultyStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// [PATTERN: Singleton] — Managed as a single bean instance by Spring IoC
// [GRASP: Information Expert] — AdaptiveEngine is the Information Expert for question selection because it holds state and logic
@Service
public class AdaptiveEngine {

    private final QuestionRepository questionRepository;
    
    // [PATTERN: Strategy] — Runtime-swappable difficulty algorithms
    // [PRINCIPLE: OCP] — New strategies can be added without modifying this class
    private final DifficultyStrategy easyStrategy;
    private final DifficultyStrategy mediumStrategy;
    private final DifficultyStrategy hardStrategy;
    
    private DifficultyStrategy currentStrategy;

    public AdaptiveEngine(QuestionRepository questionRepository,
                          @Qualifier("easyStrategy") DifficultyStrategy easyStrategy,
                          @Qualifier("mediumStrategy") DifficultyStrategy mediumStrategy,
                          @Qualifier("hardStrategy") DifficultyStrategy hardStrategy) {
        this.questionRepository = questionRepository;
        this.easyStrategy = easyStrategy;
        this.mediumStrategy = mediumStrategy;
        this.hardStrategy = hardStrategy;
        this.currentStrategy = easyStrategy; // Default
    }

    @Transactional(readOnly = true)
    public Question selectNextQuestion(ExamSession session) {
        if (session == null || session.getExam() == null) return null;
        
        // Safely get answers
        List<Answer> answers = session.getAnswers() != null ? session.getAnswers() : new ArrayList<>();
        
        // [PATTERN: Iterator] — Stream API iterates collection elements
        List<Long> answeredIds = answers.stream()
                .map(a -> a.getQuestion().getId())
                .collect(Collectors.toList());

        // Determine next difficulty dynamically
        DifficultyLevel targetDifficulty = determineDifficulty(answers, session);
        session.setCurrentDifficulty(targetDifficulty);

        String topic = session.getExam().getTopic();
        System.out.println("🔍 [ADAPTIVE] Searching for topic: " + topic + " | Target Difficulty: " + targetDifficulty);
        
        // Filter questions based on topic, not answered, and difficulty
        List<Question> allForTopic = questionRepository.findAll().stream()
                .filter(q -> q.getTopic().equalsIgnoreCase(topic))
                .filter(q -> !answeredIds.contains(q.getId()))
                .filter(q -> q.getDifficulty() == targetDifficulty)
                .collect(Collectors.toList());

        if (!allForTopic.isEmpty()) {
            Question selected = allForTopic.get(0);
            System.out.println("✅ [ADAPTIVE] Selected Question ID: " + selected.getId() + " | Difficulty: " + selected.getDifficulty());
            return selected;
        }

        // FALLBACK: If no questions for specific difficulty, try any difficulty in topic
        List<Question> fallbackTopic = questionRepository.findAll().stream()
                .filter(q -> q.getTopic().equalsIgnoreCase(topic))
                .filter(q -> !answeredIds.contains(q.getId()))
                .collect(Collectors.toList());

        if (!fallbackTopic.isEmpty()) {
            Question selected = fallbackTopic.get(0);
            System.out.println("[ADAPTIVE] Fallback selected Question ID: " + selected.getId() + " | Difficulty: " + selected.getDifficulty());
            return selected;
        }

        System.out.println("[ADAPTIVE] Topic empty. No more questions.");
        return null;
    }
    
    private DifficultyLevel determineDifficulty(List<Answer> answers, ExamSession session) {
        if (answers.isEmpty()) {
            this.currentStrategy = easyStrategy;
            return this.currentStrategy.adjust(session);
        }
        
        long correctCount = answers.stream().filter(a -> a.getIsCorrect() != null && a.getIsCorrect()).count();
        double scorePercentage = (double) correctCount / answers.size() * 100.0;
        
        // Swap strategy at runtime based on performance
        if (scorePercentage < 40.0) {
            this.currentStrategy = easyStrategy;
        } else if (scorePercentage < 75.0) {
            this.currentStrategy = mediumStrategy;
        } else {
            this.currentStrategy = hardStrategy;
        }
        
        // [PATTERN: Strategy] — Delegates to the current swappable strategy
        return this.currentStrategy.adjust(session);
    }

    public Question getNextQuestion(boolean lastCorrect, DifficultyLevel lastLevel, String topic) {
        return questionRepository.findRandomByDifficultyAndTopic(lastLevel, topic);
    }

    public long getQuestionCount() {
        return questionRepository.count();
    }
}
