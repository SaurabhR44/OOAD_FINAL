package com.exam.dto;

import com.exam.model.DifficultyLevel;
import com.exam.model.Question;

public class AdaptiveResponse {
    private Question nextQuestion;
    private DifficultyLevel previousDifficulty;
    private DifficultyLevel nextDifficulty;
    private boolean wasCorrect;
    private int scoreAwarded;

    public AdaptiveResponse() {
    }

    public AdaptiveResponse(Question nextQuestion, DifficultyLevel previousDifficulty, DifficultyLevel nextDifficulty, boolean wasCorrect, int scoreAwarded) {
        this.nextQuestion = nextQuestion;
        this.previousDifficulty = previousDifficulty;
        this.nextDifficulty = nextDifficulty;
        this.wasCorrect = wasCorrect;
        this.scoreAwarded = scoreAwarded;
    }

    public Question getNextQuestion() {
        return nextQuestion;
    }

    public void setNextQuestion(Question nextQuestion) {
        this.nextQuestion = nextQuestion;
    }

    public DifficultyLevel getPreviousDifficulty() {
        return previousDifficulty;
    }

    public void setPreviousDifficulty(DifficultyLevel previousDifficulty) {
        this.previousDifficulty = previousDifficulty;
    }

    public DifficultyLevel getNextDifficulty() {
        return nextDifficulty;
    }

    public void setNextDifficulty(DifficultyLevel nextDifficulty) {
        this.nextDifficulty = nextDifficulty;
    }

    public boolean isWasCorrect() {
        return wasCorrect;
    }

    public void setWasCorrect(boolean wasCorrect) {
        this.wasCorrect = wasCorrect;
    }

    public int getScoreAwarded() {
        return scoreAwarded;
    }

    public void setScoreAwarded(int scoreAwarded) {
        this.scoreAwarded = scoreAwarded;
    }
}
