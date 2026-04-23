package com.exam.controller;

import com.exam.dto.AdaptiveResponse;
import com.exam.model.*;
import com.exam.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam")
@CrossOrigin(origins = "*")
// [PRINCIPLE: MVC Architecture] — Controller routes HTTP requests, delegates logic, and React renders the view
// [GRASP: Controller] — Receives requests, delegates all logic to ExamService
// [PRINCIPLE: ISP] — Controller depends only on the specific service interfaces it needs
public class ExamController {

    private final ExamService examService;
    private final AnswerService answerService;
    private final AdaptiveEngine adaptiveEngine;

    public ExamController(ExamService examService, AnswerService answerService, AdaptiveEngine adaptiveEngine) {
        this.examService = examService;
        this.answerService = answerService;
        this.adaptiveEngine = adaptiveEngine;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('STUDENT', 'FACULTY', 'ADMIN')")
    public ResponseEntity<List<Exam>> getAllExams() {
        return ResponseEntity.ok(examService.getAllExams());
    }

    @PostMapping("/start")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> startExam(@RequestParam Long examId, @RequestParam Long studentId) {
        try {
            ExamSession session = examService.startExam(examId, studentId);
            Question firstQuestion = adaptiveEngine.selectNextQuestion(session);

            System.out.println("=== EXAM START ===");
            System.out.println("Session ID: " + session.getId());
            System.out.println("First Question: " + (firstQuestion != null ? firstQuestion.getId() + " | " + firstQuestion.getContent().substring(0, Math.min(50, firstQuestion.getContent().length())) : "NULL - NO QUESTIONS IN DB!"));

            return ResponseEntity.ok(new ExamStartResponse(session, firstQuestion));
        } catch (Exception e) {
            System.err.println("=== START EXAM ERROR: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to start exam: " + e.getMessage());
        }
    }

    @PostMapping("/submit-answer")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitAnswer(@RequestParam Long sessionId,
            @RequestParam Long questionId,
            @RequestParam(required = false) Long selectedOptionId,
            @RequestParam(required = false) String textAnswer) {
        try {
            AdaptiveResponse adaptiveResponse = answerService.submitAnswer(sessionId, questionId, selectedOptionId, textAnswer);
            return ResponseEntity.ok(adaptiveResponse);
        } catch (Exception e) {
            System.err.println("=== SUBMIT ANSWER ERROR: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to submit answer: " + e.getMessage());
        }
    }

    @PostMapping("/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitExam(@RequestParam Long sessionId) {
        try {
            examService.submitExam(sessionId);
            return ResponseEntity.ok("Exam submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to submit exam: " + e.getMessage());
        }
    }

    @PostMapping("/pause")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> pauseExam(@RequestParam Long sessionId) {
        try {
            ExamSession session = examService.pauseExam(sessionId);
            return ResponseEntity.ok(session);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resume")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> resumeExam(@RequestParam Long sessionId) {
        try {
            ExamSession session = examService.resumeExam(sessionId);
            return ResponseEntity.ok(session);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/sessions/student/{studentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'FACULTY', 'ADMIN')")
    public ResponseEntity<?> getStudentSessions(@PathVariable Long studentId) {
        List<ExamSession> sessions = examService.getStudentSessions(studentId);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/sessions/active")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
    public ResponseEntity<?> getActiveSessions() {
        List<ExamSession> sessions = examService.getActiveExamSessions();
        return ResponseEntity.ok(sessions);
    }

    @PostMapping("/terminate")
    @PreAuthorize("hasAnyRole('STUDENT', 'FACULTY', 'ADMIN')")
    public ResponseEntity<?> terminateExam(@RequestParam("sessionId") Long sessionId, @RequestParam("reason") String reason) {
        try {
            ExamSession session = examService.terminateExam(sessionId, reason);
            return ResponseEntity.ok(session);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    static class ExamStartResponse {
        public ExamSession session;
        public Question firstQuestion;

        public ExamStartResponse(ExamSession session, Question firstQuestion) {
            this.session = session;
            this.firstQuestion = firstQuestion;
        }
    }
}
