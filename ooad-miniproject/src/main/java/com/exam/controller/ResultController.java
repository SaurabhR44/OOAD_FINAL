package com.exam.controller;

import com.exam.model.ExamResult;
import com.exam.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "*")
public class ResultController {

    @Autowired
    private ResultService resultService;

    @PostMapping("/evaluate/{sessionId}")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
    public ResponseEntity<?> evaluateExam(@PathVariable Long sessionId) {
        try {
            ExamResult result = resultService.evaluateExam(sessionId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Evaluation failed: " + e.getMessage());
        }
    }

    @PostMapping("/publish/{resultId}")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
    public ResponseEntity<?> publishResult(@PathVariable Long resultId) {
        try {
            ExamResult result = resultService.publishResult(resultId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/student/{srn}")
    @PreAuthorize("hasAnyRole('STUDENT', 'FACULTY', 'ADMIN')")
    public ResponseEntity<?> getResultsBySrn(@PathVariable String srn) {
        try {
            return ResponseEntity.ok(resultService.getResultsBySrn(srn));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
