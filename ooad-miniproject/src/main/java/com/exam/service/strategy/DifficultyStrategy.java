package com.exam.service.strategy;

import com.exam.model.DifficultyLevel;
import com.exam.model.ExamSession;

// [PATTERN: Strategy] — Interface for runtime-swappable difficulty algorithms
// [PRINCIPLE: LSP] — All implementations (Easy/Medium/Hard) are substitutable wherever DifficultyStrategy is used
public interface DifficultyStrategy {
    DifficultyLevel adjust(ExamSession session);
}

