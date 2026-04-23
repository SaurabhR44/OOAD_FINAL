package com.exam.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "exam_session_id")
    private ExamSession examSession;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType actionType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    private LocalDateTime timestamp = LocalDateTime.now();

    private String ipAddress;
    private String userAgent;

    @Column(columnDefinition = "TEXT")
    private String metadata; // JSON for additional data

    public enum ActionType {
        LOGIN,
        LOGOUT,
        EXAM_START,
        EXAM_PAUSE,
        EXAM_RESUME,
        EXAM_SUBMIT,
        ANSWER_SUBMIT,
        VIOLATION_DETECTED,
        EXAM_TERMINATED,
        QUESTION_VIEW,
        ADMIN_ACTION,
        SYSTEM_EVENT
    }
}
