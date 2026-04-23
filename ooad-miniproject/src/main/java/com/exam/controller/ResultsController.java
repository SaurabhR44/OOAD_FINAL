package com.exam.controller;

import com.exam.model.*;
import com.exam.repository.*;
import com.exam.service.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

// [PATTERN: Singleton] — Managed as a single bean instance by Spring IoC
// [GRASP: Controller] — Delegates result computation and retrieval to domain objects
@RestController
@RequestMapping("/api/exam")
@CrossOrigin(origins = "*")
public class ResultsController {

    private final ExamSessionRepository examSessionRepository;
    private final AnswerRepository answerRepository;
    private final ExamResultRepository examResultRepository;
    private final ExamService examService;

    // [PRINCIPLE: DIP] — Dependencies injected via constructor
    public ResultsController(
            ExamSessionRepository examSessionRepository,
            AnswerRepository answerRepository,
            ExamResultRepository examResultRepository,
            ExamService examService) {
        this.examSessionRepository = examSessionRepository;
        this.answerRepository = answerRepository;
        this.examResultRepository = examResultRepository;
        this.examService = examService;
    }

    @GetMapping("/results")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getResults(@RequestParam Long sessionId) {
        try {
            ExamSession session = examSessionRepository.findById(sessionId)
                    .orElseThrow(() -> new RuntimeException("Session not found"));

            List<Answer> answers = answerRepository.findByExamSessionId(sessionId);
            long correctCount = answers.stream().filter(Answer::getIsCorrect).count();
            
            int totalScore = 0;
            for (Answer answer : answers) {
                if (answer.getIsCorrect()) {
                    switch (answer.getQuestionDifficulty()) {
                        case EASY -> totalScore += 1;
                        case MEDIUM -> totalScore += 3;
                        case HARD -> totalScore += 5;
                    }
                }
            }
            
            int maxScore = (10 * 1) + (10 * 3) + (10 * 5); 
            int finalScore = Math.min(100, (totalScore * 100) / maxScore);
            
            Map<String, Object> results = new LinkedHashMap<>();
            results.put("sessionId", sessionId);
            results.put("score", finalScore);
            results.put("correct", correctCount);
            results.put("totalQuestions", answers.size());
            results.put("accuracy", Math.round((correctCount * 100.0) / answers.size()));
            results.put("timeSpentSeconds", session.getTimeRemaining() == null ? 0 : 
                    (session.getExam().getDurationMinutes() * 60) - session.getTimeRemaining());
            results.put("finalDifficulty", session.getCurrentDifficulty());
            results.put("violationCount", session.getProctoringLogs().size());
            results.put("submittedAt", session.getSubmittedAt());
            
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching results: " + e.getMessage());
        }
    }

    @GetMapping("/results/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllResults() {
        try {
            List<ExamResult> results = examResultRepository.findAll();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/results/student/{studentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<?> getStudentResults(@PathVariable Long studentId) {
        try {
            List<ExamSession> sessions = examService.getStudentSessions(studentId);
            
            List<Map<String, Object>> resultsList = new ArrayList<>();
            for (ExamSession session : sessions) {
                List<Answer> answers = answerRepository.findByExamSessionId(session.getId());
                long correct = answers.stream().filter(Answer::getIsCorrect).count();
                
                Map<String, Object> result = new HashMap<>();
                result.put("exam", session.getExam().getTitle());
                result.put("score", correct);
                result.put("total", answers.size());
                result.put("date", session.getSubmittedAt());
                resultsList.add(result);
            }
            
            return ResponseEntity.ok(resultsList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
