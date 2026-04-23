package com.exam.repository;

import com.exam.model.ViolationLog;
import com.exam.model.ViolationLog.ViolationType;
import com.exam.model.ViolationLog.Severity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViolationLogRepository extends JpaRepository<ViolationLog, Long> {
    List<ViolationLog> findByExamSessionId(Long examSessionId);

    List<ViolationLog> findByExamSessionIdAndType(Long examSessionId, ViolationType type);

    List<ViolationLog> findByExamSessionIdAndSeverity(Long examSessionId, Severity severity);

    Long countByExamSessionId(Long examSessionId);

    List<ViolationLog> findByIsReviewedFalse();
}
