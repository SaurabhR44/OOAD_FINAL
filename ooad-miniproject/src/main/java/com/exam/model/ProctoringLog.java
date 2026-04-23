package com.exam.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "proctoring_logs")
@Data
public class ProctoringLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "session_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private ExamSession examSession;

    private String violationType; // e.g., "NO_FACE", "MULTIPLE_FACES", "TAB_SWITCH"
    private String description;
    private LocalDateTime timestamp;
    private byte[] evidenceSnapshot; // Blob for storing violation image if needed
}
