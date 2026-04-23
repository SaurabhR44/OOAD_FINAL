package com.exam.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// [PATTERN: Builder] — Allows for safe, readable construction of complex Question objects
// [PRINCIPLE: Encapsulation] — Controlled access via Lombok accessors
@Entity
@Table(name = "questions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DifficultyLevel difficulty;

    @Column(nullable = false)
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private QuestionType type = QuestionType.MCQ;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Builder.Default
    private List<QuestionOption> options = new ArrayList<>();

    private Integer correctOptionIndex;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private Faculty createdBy;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    public enum QuestionType {
        MCQ, SUBJECTIVE, TRUE_FALSE, FILL_IN_BLANK
    }
}
