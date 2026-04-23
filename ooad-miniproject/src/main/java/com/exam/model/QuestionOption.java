package com.exam.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "question_options")
@Data
public class QuestionOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Question question;
}
