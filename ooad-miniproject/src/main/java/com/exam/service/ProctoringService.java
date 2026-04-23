package com.exam.service;

import com.exam.model.Violation;

// [PRINCIPLE: ISP] — Client-specific interface for proctoring operations
public interface ProctoringService {
    void logEvent(String sessionId, String eventType);
    void analyzeVisuals(byte[] imageData, String sessionId);
    void analyzeAudio(byte[] audioData, String sessionId);
}
