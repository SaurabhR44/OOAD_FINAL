package com.exam.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "exam_blueprints")
@Data
public class ExamBlueprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "blueprint")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Exam exam;

    @Column(nullable = false)
    private Integer easyCount = 0;

    @Column(nullable = false)
    private Integer mediumCount = 0;

    @Column(nullable = false)
    private Integer hardCount = 0;

    private String topicDistribution; // JSON string for topic-wise breakdown

    private LocalDateTime createdAt = LocalDateTime.now();

    public Integer getTotalQuestions() {
        return easyCount + mediumCount + hardCount;
    }
}
