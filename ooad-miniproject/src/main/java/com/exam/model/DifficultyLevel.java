package com.exam.model;

public enum DifficultyLevel {
    EASY, MEDIUM, HARD;

    public DifficultyLevel next() {
        if (this == EASY) return MEDIUM;
        return HARD;
    }

    public DifficultyLevel previous() {
        if (this == HARD) return MEDIUM;
        return EASY;
    }
}
