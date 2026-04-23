package com.exam.repository;

import com.exam.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findBySrn(String srn);

    Optional<Student> findByUsername(String username);

    Optional<Student> findByEmail(String email);

    List<Student> findByCampus(Integer campus);

    List<Student> findByBranch(String branch);

    List<Student> findByAcademicYear(Integer academicYear);
}
