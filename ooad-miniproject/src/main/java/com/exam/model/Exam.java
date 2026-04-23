package com.exam.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exams")
@Data
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String topic; // Links to Question.topic

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer durationMinutes;

    private Integer passingScore = 50; // Percentage

    @ManyToOne
    @JoinColumn(name = "created_by")
    private Faculty createdBy;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "blueprint_id")
    private ExamBlueprint blueprint;

    @ManyToMany
    @JoinTable(name = "exam_questions", joinColumns = @JoinColumn(name = "exam_id"), inverseJoinColumns = @JoinColumn(name = "question_id"))
    private List<Question> questionPool = new ArrayList<>();

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<ExamSession> sessions = new ArrayList<>();

    private LocalDateTime scheduledStartTime;
    private LocalDateTime scheduledEndTime;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false)
    private Boolean allowPauseResume = false;

    @Column(nullable = false)
    private Integer maxViolationScore = 10; // Auto-terminate threshold

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private ExamType type = ExamType.ADAPTIVE;

    public enum ExamType {
        ADAPTIVE,
        FIXED,
        PRACTICE
    }
}
