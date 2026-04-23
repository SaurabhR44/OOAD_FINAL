package com.exam.repository;

import com.exam.model.ExamResult;
import com.exam.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    List<ExamResult> findByStudent(Student student);
}
