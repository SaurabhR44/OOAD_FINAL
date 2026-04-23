package com.exam.repository;

import com.exam.model.ExamSession;
import com.exam.model.ExamSession.ExamState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamSessionRepository extends JpaRepository<ExamSession, Long> {
    List<ExamSession> findByStudentId(Long studentId);

    List<ExamSession> findByExamId(Long examId);

    List<ExamSession> findByState(ExamState state);

    Optional<ExamSession> findByStudentIdAndExamId(Long studentId, Long examId);

    List<ExamSession> findByStateAndIsTerminatedFalse(ExamState state);
}
