package com.exam.service;

import com.exam.model.*;
import com.exam.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// [PATTERN: Singleton] — Managed as a single bean instance by Spring IoC
// [GRASP: High Cohesion] — Solely responsible for violation recording and retrieval
// [PRINCIPLE: SRP] — Each class does ONE thing, this focuses purely on violations
@Service
public class ViolationService {

    private final ViolationLogRepository violationLogRepository;
    private final ExamSessionRepository examSessionRepository;
    private final ExamService examService;
    private final AuditLogService auditLogService;

    // [PRINCIPLE: DIP] — Depends on ExamService interface
    public ViolationService(
            ViolationLogRepository violationLogRepository,
            ExamSessionRepository examSessionRepository,
            ExamService examService,
            AuditLogService auditLogService) {
        this.violationLogRepository = violationLogRepository;
        this.examSessionRepository = examSessionRepository;
        this.examService = examService;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public ViolationLog logViolation(Long sessionId, ViolationLog.ViolationType type,
            ViolationLog.Severity severity, String description, String evidencePath) {
        ExamSession session = examSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        ViolationLog violation = new ViolationLog();
        violation.setExamSession(session);
        violation.setType(type);
        violation.setSeverity(severity);
        violation.setDescription(description);
        violation.setEvidencePath(evidencePath);
        violation.setSeverityScore(severity.getScore());
        violation.setTimestamp(LocalDateTime.now());

        ViolationLog saved = violationLogRepository.save(violation);

        session.setViolationScore(session.getViolationScore() + severity.getScore());

        if (session.getViolationScore() >= session.getExam().getMaxViolationScore()) {
            examService.terminateExam(sessionId,
                    "Auto-terminated: Violation score exceeded threshold (" + session.getViolationScore() + ")");
        } else if (severity == ViolationLog.Severity.CRITICAL) {
            session.setWarningCount(session.getWarningCount() + 1);
        }

        examSessionRepository.save(session);

        auditLogService.log(session.getStudent(), session, AuditLog.ActionType.VIOLATION_DETECTED,
                "Violation: " + type + " (" + severity + ") - " + description);

        return saved;
    }

    public List<ViolationLog> getSessionViolations(Long sessionId) {
        return violationLogRepository.findByExamSessionId(sessionId);
    }

    public List<ViolationLog> getUnreviewedViolations() {
        return violationLogRepository.findByIsReviewedFalse();
    }

    @Transactional
    public ViolationLog reviewViolation(Long violationId, String reviewNotes) {
        ViolationLog violation = violationLogRepository.findById(violationId)
                .orElseThrow(() -> new RuntimeException("Violation not found"));

        violation.setIsReviewed(true);
        violation.setReviewNotes(reviewNotes);

        return violationLogRepository.save(violation);
    }
}
