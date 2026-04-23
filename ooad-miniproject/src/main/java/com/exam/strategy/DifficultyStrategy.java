package com.exam.strategy;

import com.exam.model.DifficultyLevel;

/**
 * STRATEGY PATTERN: Defines the contract for adaptive difficulty algorithms.
 */
public interface DifficultyStrategy {
    DifficultyLevel getNextDifficulty(boolean isCorrect, DifficultyLevel currentDifficulty);
}
