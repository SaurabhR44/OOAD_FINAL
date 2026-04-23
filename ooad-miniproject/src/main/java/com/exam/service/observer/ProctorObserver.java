package com.exam.service.observer;

import com.exam.model.Violation;

public interface ProctorObserver {
    void onViolation(Violation violation);
}
