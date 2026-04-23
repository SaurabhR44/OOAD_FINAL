package com.exam.service;

import com.exam.model.*;
import com.exam.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public AuditLog log(User user, ExamSession session, AuditLog.ActionType actionType, String description) {
        AuditLog auditLog = new AuditLog();
        auditLog.setUser(user);
        auditLog.setExamSession(session);
        auditLog.setActionType(actionType);
        auditLog.setDescription(description);

        return auditLogRepository.save(auditLog);
    }

    public AuditLog log(User user, AuditLog.ActionType actionType, String description) {
        return log(user, null, actionType, description);
    }
}
