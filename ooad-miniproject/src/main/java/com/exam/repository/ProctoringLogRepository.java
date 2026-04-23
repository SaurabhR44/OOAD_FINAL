package com.exam.repository;

import com.exam.model.ProctoringLog;
import com.exam.model.Exam;
import com.exam.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProctoringLogRepository extends JpaRepository<ProctoringLog, Long> {
    List<ProctoringLog> findByExamAndStudent(Exam exam, Student student);
}
