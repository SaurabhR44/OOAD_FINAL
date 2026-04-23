package com.exam.service.strategy;

import com.exam.model.DifficultyLevel;
import com.exam.model.ExamSession;
import org.springframework.stereotype.Component;

// [PATTERN: Strategy] — Linear difficulty adjustment: increase on correct, decrease on wrong
@Component("linearStrategy")
public class LinearDifficultyStrategy implements DifficultyStrategy {

    @Override
    public DifficultyLevel adjust(ExamSession session) {
        // Keep the current difficulty — override per exam logic
        return session.getCurrentDifficulty();
    }

    public DifficultyLevel determineNextLevel(boolean isCorrect, DifficultyLevel currentLevel) {
        if (isCorrect) {
            if (currentLevel == DifficultyLevel.EASY) return DifficultyLevel.MEDIUM;
            if (currentLevel == DifficultyLevel.MEDIUM) return DifficultyLevel.HARD;
            return DifficultyLevel.HARD;
        } else {
            if (currentLevel == DifficultyLevel.HARD) return DifficultyLevel.MEDIUM;
            if (currentLevel == DifficultyLevel.MEDIUM) return DifficultyLevel.EASY;
            return DifficultyLevel.EASY;
        }
    }
}
