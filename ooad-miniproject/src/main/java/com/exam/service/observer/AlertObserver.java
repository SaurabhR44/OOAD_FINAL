package com.exam.service.observer;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// [PATTERN: Observer] — Listens for ViolationDetectedEvent to trigger alerts
@Component
public class AlertObserver {

    @EventListener
    public void onViolation(ViolationDetectedEvent event) {
        // [PRINCIPLE: SRP] — This observer only handles alerting concerns
        if (event.getViolation().getType().equals("CRITICAL")) {
            System.err.println("ALERT: Critical violation detected in session " + event.getSessionId());
            // In a real app, this would trigger a WebSocket push to the faculty dashboard
        }
    }
}
