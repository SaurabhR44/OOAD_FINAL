package com.exam.service.adapter;

import org.springframework.stereotype.Component;

@Component
public class PythonVisionAdapter implements MLModelAdapter {
    @Override
    public int countFaces(byte[] imageData) {
        // Mock logic: 1 face if image is provided, 0 otherwise
        return (imageData != null && imageData.length > 0) ? 1 : 0;
    }

    @Override
    public boolean detectSpeech(byte[] audioData) {
        // Mock logic: No speech detected by default
        return false;
    }
}
