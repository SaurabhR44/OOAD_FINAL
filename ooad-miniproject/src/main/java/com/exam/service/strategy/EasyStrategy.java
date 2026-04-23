package com.exam.service.strategy;

import com.exam.model.DifficultyLevel;
import com.exam.model.ExamSession;
import org.springframework.stereotype.Component;

// [PATTERN: Strategy] — Easy implementation
@Component("easyStrategy")
public class EasyStrategy implements DifficultyStrategy {
    @Override
    public DifficultyLevel adjust(ExamSession session) {
        return DifficultyLevel.EASY;
    }
}
