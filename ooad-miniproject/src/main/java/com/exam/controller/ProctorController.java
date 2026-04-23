package com.exam.controller;

import com.exam.service.ProctoringService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// [PATTERN: Singleton] — Managed as a single bean instance by Spring IoC
// [GRASP: Controller] — Delegates proctoring feed analysis to the ProctoringService
@RestController
@RequestMapping("/api/proctor")
public class ProctorController {

    private final ProctoringService proctoringService;

    // [PRINCIPLE: DIP] — Constructor injection for proctoring service
    public ProctorController(ProctoringService proctoringService) {
        this.proctoringService = proctoringService;
    }

    @PostMapping("/analyze-visual")
    public ResponseEntity<?> analyzeVisual(@RequestParam("frame") byte[] frame, @RequestParam("sessionId") String sessionId) {
        proctoringService.analyzeVisuals(frame, sessionId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/analyze-audio")
    public ResponseEntity<?> analyzeAudio(@RequestParam("audio") byte[] audio, @RequestParam("sessionId") String sessionId) {
        proctoringService.analyzeAudio(audio, sessionId);
        return ResponseEntity.ok().build();
    }
}
