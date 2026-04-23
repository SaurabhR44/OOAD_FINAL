package com.exam.service.observer;

import com.exam.model.Violation;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

// [PATTERN: Observer] — Event object used to notify multiple listeners
@Getter
public class ViolationDetectedEvent extends ApplicationEvent {
    private final String sessionId;
    private final Violation violation;

    public ViolationDetectedEvent(Object source, String sessionId, Violation violation) {
        super(source);
        this.sessionId = sessionId;
        this.violation = violation;
    }
}
