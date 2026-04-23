package com.exam.service.strategy;

import com.exam.model.DifficultyLevel;
import com.exam.model.ExamSession;
import org.springframework.stereotype.Component;

// [PATTERN: Strategy] — Medium implementation
@Component("mediumStrategy")
public class MediumStrategy implements DifficultyStrategy {
    @Override
    public DifficultyLevel adjust(ExamSession session) {
        return DifficultyLevel.MEDIUM;
    }
}
