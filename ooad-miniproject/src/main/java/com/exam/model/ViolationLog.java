package com.exam.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "violations")
@Data
public class ViolationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_session_id", nullable = false)
    private ExamSession examSession;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ViolationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String description;

    private String evidencePath; // Path to screenshot/video

    @Column(nullable = false)
    private Integer severityScore = 0;

    private Boolean isReviewed = false;
    private String reviewNotes;

    public enum ViolationType {
        FACE_NOT_DETECTED,
        MULTIPLE_FACES,
        FACE_DISAPPEARED,
        TAB_SWITCH,
        WINDOW_BLUR,
        FULLSCREEN_EXIT,
        SUSPICIOUS_CURSOR,
        IDLE_DETECTED,
        AUDIO_ANOMALY,
        BACKGROUND_NOISE,
        VOICE_DETECTED,
        COPY_PASTE_ATTEMPT,
        SCREENSHOT_ATTEMPT,
        WEBCAM_ERROR,
        OTHER
    }

    public enum Severity {
        LOW(1),
        MEDIUM(3),
        HIGH(6),
        CRITICAL(10);

        private final int score;

        Severity(int score) {
            this.score = score;
        }

        public int getScore() {
            return score;
        }
    }
}
