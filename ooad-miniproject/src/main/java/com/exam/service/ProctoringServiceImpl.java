package com.exam.service;

import com.exam.model.Violation;
import com.exam.service.adapter.MLModelAdapter;
import com.exam.service.observer.ViolationDetectedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

// [PATTERN: Singleton] — Managed as a single bean instance by Spring IoC
// [PATTERN: Facade] — Orchestrates ML analysis and event publishing behind a clean interface
@Service("proctoringServiceImpl")
public class ProctoringServiceImpl implements ProctoringService {

    private final MLModelAdapter mlAdapter;
    private final ApplicationEventPublisher eventPublisher;

    // [PRINCIPLE: DIP] — Constructor injection for ML adapter and event publisher
    public ProctoringServiceImpl(MLModelAdapter mlAdapter, ApplicationEventPublisher eventPublisher) {
        this.mlAdapter = mlAdapter;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void logEvent(String sessionId, String eventType) {
        // [PRINCIPLE: Abstraction] — Hides complexity of event processing
        System.out.println("Proctoring session [" + sessionId + "] event: " + eventType);
    }

    @Override
    public void analyzeVisuals(byte[] imageData, String sessionId) {
        int faceCount = mlAdapter.countFaces(imageData);
        if (faceCount == 0) {
            publishViolation(sessionId, "NO_FACE", "No face detected in frame");
        } else if (faceCount > 1) {
            publishViolation(sessionId, "MULTIPLE_FACES", "Multiple faces detected");
        }
    }

    @Override
    public void analyzeAudio(byte[] audioData, String sessionId) {
        if (mlAdapter.detectSpeech(audioData)) {
            publishViolation(sessionId, "SPEECH", "Speech detected in environment");
        }
    }

    private void publishViolation(String sessionId, String type, String message) {
        // [PATTERN: Observer] — Publishing event to decoupled listeners
        Violation violation = new Violation(type, message, LocalDateTime.now());
        eventPublisher.publishEvent(new ViolationDetectedEvent(this, sessionId, violation));
    }
}
