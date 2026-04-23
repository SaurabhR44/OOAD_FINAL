package com.exam.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// [PATTERN: Singleton] — Explicit non-Spring Singleton using double-checked locking
public class ProctoringSessionRegistry {

    private static volatile ProctoringSessionRegistry instance;
    private final Map<String, Object> activeSessions;

    private ProctoringSessionRegistry() {
        this.activeSessions = new ConcurrentHashMap<>();
    }

    public static ProctoringSessionRegistry getInstance() {
        if (instance == null) {
            synchronized (ProctoringSessionRegistry.class) {
                if (instance == null) {
                    instance = new ProctoringSessionRegistry();
                }
            }
        }
        return instance;
    }

    public void registerSession(String sessionId, Object sessionData) {
        activeSessions.put(sessionId, sessionData);
    }

    public Object getSession(String sessionId) {
        return activeSessions.get(sessionId);
    }

    public void removeSession(String sessionId) {
        activeSessions.remove(sessionId);
    }
}
