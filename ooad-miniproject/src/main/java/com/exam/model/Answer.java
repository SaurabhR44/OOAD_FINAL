package com.exam.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "answers")
@Data
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_session_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private ExamSession examSession;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "selected_option_id")
    private QuestionOption selectedOption;

    @Column(columnDefinition = "TEXT")
    private String subjectiveAnswer;

    @Column(nullable = false)
    private Boolean isCorrect = false;

    private LocalDateTime answeredAt;

    private Integer timeSpent; // in seconds

    @Enumerated(EnumType.STRING)
    private DifficultyLevel questionDifficulty;
}
