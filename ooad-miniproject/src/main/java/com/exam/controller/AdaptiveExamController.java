package com.exam.controller;

import com.exam.dto.AdaptiveResponse;
import com.exam.model.Question;
import com.exam.service.AdaptiveExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/adaptive-exam")
public class AdaptiveExamController {

    private final AdaptiveExamService adaptiveExamService;

    public AdaptiveExamController(AdaptiveExamService adaptiveExamService) {
        this.adaptiveExamService = adaptiveExamService;
    }

    /**
     * Endpoint to submit an answer and compute the Strategy-based next difficulty.
     * Expects:
     * {
     *   "sessionId": 1,
     *   "questionId": 5,
     *   "selectedOptionId": 12
     * }
     */
    @PostMapping("/submit-and-get-next")
    public ResponseEntity<?> submitAnswerAndGetNext(@RequestBody Map<String, Long> payload) {
        try {
            Long sessionId = payload.get("sessionId");
            Long questionId = payload.get("questionId");
            Long selectedOptionId = payload.get("selectedOptionId");

            if (sessionId == null || questionId == null || selectedOptionId == null) {
                return ResponseEntity.badRequest().body("Requires sessionId, questionId, and selectedOptionId.");
            }

            AdaptiveResponse adaptiveResponse = adaptiveExamService.submitAnswerAndGetNext(sessionId, questionId, selectedOptionId);
            
            // send adaptive metadata + next question to UI
            return ResponseEntity.ok(adaptiveResponse);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
