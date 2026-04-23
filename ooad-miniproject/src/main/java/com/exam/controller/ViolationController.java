package com.exam.controller;

import com.exam.model.ViolationLog;
import com.exam.service.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/violations")
@CrossOrigin(origins = "*")
public class ViolationController {

    @Autowired
    private ViolationService violationService;

    @PostMapping("/log")
    public ResponseEntity<?> logViolation(@RequestParam Long sessionId,
            @RequestParam ViolationLog.ViolationType type,
            @RequestParam ViolationLog.Severity severity,
            @RequestParam String description,
            @RequestParam(required = false) String evidencePath) {
        try {
            ViolationLog violation = violationService.logViolation(sessionId, type, severity, description,
                    evidencePath);
            return ResponseEntity.ok(violation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to log violation: " + e.getMessage());
        }
    }

    @GetMapping("/session/{sessionId}")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
    public ResponseEntity<?> getSessionViolations(@PathVariable Long sessionId) {
        List<ViolationLog> violations = violationService.getSessionViolations(sessionId);
        return ResponseEntity.ok(violations);
    }

    @GetMapping("/unreviewed")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
    public ResponseEntity<?> getUnreviewedViolations() {
        List<ViolationLog> violations = violationService.getUnreviewedViolations();
        return ResponseEntity.ok(violations);
    }

    @PostMapping("/{violationId}/review")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
    public ResponseEntity<?> reviewViolation(@PathVariable Long violationId,
            @RequestParam String reviewNotes) {
        try {
            ViolationLog violation = violationService.reviewViolation(violationId, reviewNotes);
            return ResponseEntity.ok(violation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
