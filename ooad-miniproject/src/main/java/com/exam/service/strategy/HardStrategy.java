package com.exam.service.strategy;

import com.exam.model.DifficultyLevel;
import com.exam.model.ExamSession;
import org.springframework.stereotype.Component;

// [PATTERN: Strategy] — Hard implementation
@Component("hardStrategy")
public class HardStrategy implements DifficultyStrategy {
    @Override
    public DifficultyLevel adjust(ExamSession session) {
        return DifficultyLevel.HARD;
    }
}
