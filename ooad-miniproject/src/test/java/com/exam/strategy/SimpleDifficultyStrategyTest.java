package com.exam.strategy;

import com.exam.model.DifficultyLevel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleDifficultyStrategyTest {

    private final DifficultyStrategy strategy = new SimpleDifficultyStrategy();

    @Test
    public void testEasyToMediumOnCorrectAnswer() {
        DifficultyLevel next = strategy.getNextDifficulty(true, DifficultyLevel.EASY);
        assertEquals(DifficultyLevel.MEDIUM, next, "Should upgrade to MEDIUM when answering EASY correctly.");
    }

    @Test
    public void testMediumToHardOnCorrectAnswer() {
        DifficultyLevel next = strategy.getNextDifficulty(true, DifficultyLevel.MEDIUM);
        assertEquals(DifficultyLevel.HARD, next, "Should upgrade to HARD when answering MEDIUM correctly.");
    }
    
    @Test
    public void testHardStaysHardOnCorrectAnswer() {
        DifficultyLevel next = strategy.getNextDifficulty(true, DifficultyLevel.HARD);
        assertEquals(DifficultyLevel.HARD, next, "Should remain HARD when answering HARD correctly.");
    }

    @Test
    public void testHardToMediumOnWrongAnswer() {
        DifficultyLevel next = strategy.getNextDifficulty(false, DifficultyLevel.HARD);
        assertEquals(DifficultyLevel.MEDIUM, next, "Should downgrade to MEDIUM when answering HARD wrongly.");
    }

    @Test
    public void testMediumToEasyOnWrongAnswer() {
        DifficultyLevel next = strategy.getNextDifficulty(false, DifficultyLevel.MEDIUM);
        assertEquals(DifficultyLevel.EASY, next, "Should downgrade to EASY when answering MEDIUM wrongly.");
    }

    @Test
    public void testEasyStaysEasyOnWrongAnswer() {
        DifficultyLevel next = strategy.getNextDifficulty(false, DifficultyLevel.EASY);
        assertEquals(DifficultyLevel.EASY, next, "Should remain EASY when answering EASY wrongly.");
    }
}
