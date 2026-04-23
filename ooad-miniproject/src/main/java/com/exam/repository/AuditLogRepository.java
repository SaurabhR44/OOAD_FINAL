package com.exam.repository;

import com.exam.model.AuditLog;
import com.exam.model.AuditLog.ActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByUserId(Long userId);

    List<AuditLog> findByExamSessionId(Long examSessionId);

    List<AuditLog> findByActionType(ActionType actionType);
}
