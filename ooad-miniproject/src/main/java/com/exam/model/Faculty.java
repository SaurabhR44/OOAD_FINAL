package com.exam.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "faculty")
@Data
@EqualsAndHashCode(callSuper = true)
public class Faculty extends User {

    @Column(nullable = false)
    private String department;

    @Column(unique = true, nullable = false)
    private String employeeId;

    private String designation;

    public Faculty() {
        super();
        this.setRole(Role.FACULTY);
    }
}
