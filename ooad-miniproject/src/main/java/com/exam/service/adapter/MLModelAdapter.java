package com.exam.service.adapter;

public interface MLModelAdapter {
    int countFaces(byte[] imageData);
    boolean detectSpeech(byte[] audioData);
}
