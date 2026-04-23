package com.exam.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exam_sessions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamState state = ExamState.CREATED;

    private LocalDateTime scheduledStartTime;
    private LocalDateTime actualStartTime;
    private LocalDateTime endTime;
    private LocalDateTime submittedAt;

    @Column(nullable = false)
    private Integer timeRemaining; // in seconds

    private Integer currentQuestionIndex = 0;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel currentDifficulty = DifficultyLevel.EASY;

    @OneToMany(mappedBy = "examSession", cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "examSession", cascade = CascadeType.ALL)
    private List<ProctoringLog> proctoringLogs = new ArrayList<>();

    private Integer violationScore = 0;
    private Integer warningCount = 0;

    @Column(nullable = false)
    private Boolean isTerminated = false;

    private String terminationReason;

    public enum ExamState {
        CREATED,
        SCHEDULED,
        ACTIVE,
        PAUSED,
        RESUMED,
        SUBMITTED,
        EVALUATED,
        CLOSED,
        TERMINATED
    }

    public enum Status {
        ACTIVE,
        COMPLETED,
        TERMINATED
    }
}
