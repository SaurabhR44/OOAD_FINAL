package com.exam.service.observer;

import com.exam.model.AuditLog;
import com.exam.service.AuditLogService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// [PATTERN: Observer] — Listens for ViolationDetectedEvent and takes action
@Component
public class AuditLogObserver {

    private final AuditLogService auditLogService;

    public AuditLogObserver(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @EventListener
    public void onViolation(ViolationDetectedEvent event) {
        // [PRINCIPLE: SRP] — This observer only handles logging concerns
        System.out.println("AuditLogObserver: Recording violation for session " + event.getSessionId());
        // In a real app, we'd map sessionId to a student/session entity here
    }
}
