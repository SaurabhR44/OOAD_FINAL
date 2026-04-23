package com.exam.repository;

import com.exam.model.ExamBlueprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamBlueprintRepository extends JpaRepository<ExamBlueprint, Long> {
}
