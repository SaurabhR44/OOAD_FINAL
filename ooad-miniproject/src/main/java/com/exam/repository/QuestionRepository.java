package com.exam.repository;

import com.exam.model.DifficultyLevel;
import com.exam.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    @Query("SELECT q FROM Question q WHERE q.topic = :topic AND q.difficulty = :difficulty ORDER BY RAND()")
    Question findRandomByDifficultyAndTopic(@Param("difficulty") DifficultyLevel difficulty, @Param("topic") String topic);

    @Query("SELECT q FROM Question q WHERE q.id NOT IN :excludedIds AND q.topic = :topic AND q.difficulty = :difficulty ORDER BY RAND()")
    Question findRandomExcluding(@Param("excludedIds") List<Long> excludedIds, @Param("difficulty") DifficultyLevel difficulty, @Param("topic") String topic);

    @Query("SELECT q FROM Question q WHERE q.topic = :topic ORDER BY RAND()")
    Question findRandomByTopic(@Param("topic") String topic);
}
