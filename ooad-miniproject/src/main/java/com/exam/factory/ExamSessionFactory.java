package com.exam.factory;

import com.exam.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

// [PATTERN: Factory Method] — Encapsulates the complexity of ExamSession creation
@Component
public class ExamSessionFactory {

    public ExamSession createSession(ExamBlueprint blueprint, Student student) {
        ExamSession session = new ExamSession();
        session.setStudent(student);
        session.setExam(blueprint.getExam());
        session.setState(ExamSession.ExamState.ACTIVE);
        session.setTimeRemaining(blueprint.getExam().getDurationMinutes() * 60);
        session.setCurrentDifficulty(DifficultyLevel.EASY);
        session.setViolationScore(0);
        session.setWarningCount(0);
        session.setIsTerminated(false);
        session.setActualStartTime(LocalDateTime.now());
        return session;
    }
}
