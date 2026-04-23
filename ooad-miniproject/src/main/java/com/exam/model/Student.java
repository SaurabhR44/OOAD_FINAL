package com.exam.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

// [PRINCIPLE: Inheritance] — Extends User
// [PRINCIPLE: Encapsulation] — All fields are private, accessed via Lombok getters/setters
@Entity
@Table(name = "students")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Student extends User {

    @Column(unique = true, nullable = false)
    @Pattern(regexp = "PES[1-2]UG(23|24|25)(AM|CS|EC)\\d{3}", message = "SRN must match format: PES[1-2]UG[23-25][AM|CS|EC]XXX")
    private String srn; // Student Registration Number

    @Column(nullable = false)
    private Integer campus; // 1 or 2

    @Column(name = "academic_year", nullable = false)
    private Integer academicYear; // 23, 24, 25

    @Column(nullable = false)
    private String branch; // AM, CS, EC

    private String section;

    public Student() {
        super();
        this.setRole(Role.STUDENT);
    }

    // Helper method to parse SRN
    public void parseSRN() {
        if (srn != null && srn.matches("PES[1-2]UG(23|24|25)(AM|CS|EC)\\d{3}")) {
            this.campus = Integer.parseInt(srn.substring(3, 4));
            this.academicYear = Integer.parseInt(srn.substring(6, 8));
            this.branch = srn.substring(8, 10);
        }
    }
}

