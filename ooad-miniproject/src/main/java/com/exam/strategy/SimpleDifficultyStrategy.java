package com.exam.strategy;

import com.exam.model.DifficultyLevel;
import org.springframework.stereotype.Component;

/**
 * Concrete Strategy implementing a simple linear progression logic.
 */
@Component
public class SimpleDifficultyStrategy implements DifficultyStrategy {

    @Override
    public DifficultyLevel getNextDifficulty(boolean isCorrect, DifficultyLevel current) {
        if (isCorrect) {
            return switch (current) {
                case EASY -> DifficultyLevel.MEDIUM;
                case MEDIUM, HARD -> DifficultyLevel.HARD;
            };
        } else {
            return switch (current) {
                case HARD -> DifficultyLevel.MEDIUM;
                case MEDIUM, EASY -> DifficultyLevel.EASY;
            };
        }
    }
}
