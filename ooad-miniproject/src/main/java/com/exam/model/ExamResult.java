package com.exam.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "exam_results")
@Data
public class ExamResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "exam_session_id", nullable = false)
    private ExamSession examSession;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @Column(nullable = false)
    private Integer rawScore = 0;

    @Column(nullable = false)
    private Double normalizedScore = 0.0; // Difficulty-adjusted score

    @Column(nullable = false)
    private Double percentage = 0.0;

    @Column(nullable = false)
    private Integer totalQuestions = 0;

    @Column(nullable = false)
    private Integer correctAnswers = 0;

    private Integer easyCorrect = 0;
    private Integer mediumCorrect = 0;
    private Integer hardCorrect = 0;

    private LocalDateTime evaluatedAt;
    private LocalDateTime publishedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResultStatus status = ResultStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private FinalCompetency competency;

    @Column(nullable = false)
    private Boolean isPassed = false;

    public Integer getScore() {
        return rawScore;
    }

    private String remarks;

    public enum FinalCompetency {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED,
        EXPERT
    }

    public enum ResultStatus {
        PENDING,
        EVALUATED,
        PUBLISHED,
        WITHHELD
    }
}
