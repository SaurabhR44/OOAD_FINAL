package com.exam.service;

import com.exam.model.Violation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// [PATTERN: Singleton] — Managed as a single bean instance by Spring IoC
// [PATTERN: Proxy] — Wraps the real ProctoringServiceImpl to add cross-cutting concerns (rate limiting)
@Service
@Primary
public class ProctoringServiceProxy implements ProctoringService {

    private final ProctoringService realService;
    private final Map<String, Long> lastViolationTime = new ConcurrentHashMap<>();
    private static final long RATE_LIMIT_MS = 5000; // 5 seconds between logs

    public ProctoringServiceProxy(@Qualifier("proctoringServiceImpl") ProctoringService realService) {
        this.realService = realService;
    }

    @Override
    public void logEvent(String sessionId, String eventType) {
        realService.logEvent(sessionId, eventType);
    }

    @Override
    public void analyzeVisuals(byte[] imageData, String sessionId) {
        if (checkRateLimit(sessionId)) {
            realService.analyzeVisuals(imageData, sessionId);
        }
    }

    @Override
    public void analyzeAudio(byte[] audioData, String sessionId) {
        if (checkRateLimit(sessionId)) {
            realService.analyzeAudio(audioData, sessionId);
        }
    }

    private boolean checkRateLimit(String sessionId) {
        long now = System.currentTimeMillis();
        long last = lastViolationTime.getOrDefault(sessionId, 0L);
        if (now - last > RATE_LIMIT_MS) {
            lastViolationTime.put(sessionId, now);
            return true;
        }
        return false;
    }
}
