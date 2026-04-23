import { useState, useEffect, useRef, useCallback } from 'react';

/**
 * Enhanced Proctoring Hook
 * Features:
 * - Tab switching detection
 * - Cursor tracking
 * - Window blur detection
 * - Fullscreen exit detection
 * - Copy/paste blocking
 * - Right-click prevention
 * - Audio anomaly detection
 * - Screenshot prevention
 * - Keylogger detection
 */
export const useEnhancedProctoring = (sessionId, isActive = true) => {
    const [violations, setViolations] = useState([]);
    const [isTabActive, setIsTabActive] = useState(true);
    const [cursorOutside, setCursorOutside] = useState(false);
    const [suspiciousActivity, setSuspiciousActivity] = useState(false);
    const violationTimeoutRef = useRef(null);
    const keyboardActivityRef = useRef(0);

    const reportViolation = useCallback((type, severity, description) => {
        if (!sessionId || !isActive) return;

        const violation = { 
            type, 
            severity, 
            description, 
            timestamp: new Date(),
            userAgent: navigator.userAgent,
            screenRes: `${window.screen.width}x${window.screen.height}`
        };
        
        setViolations(prev => [...prev, violation]);
        
        if (severity === 'CRITICAL') {
            setSuspiciousActivity(true);
        }

        const params = new URLSearchParams({
            sessionId,
            type,
            severity,
            description,
            userAgent: navigator.userAgent
        });

        const token = localStorage.getItem('token');
        fetch(`http://localhost:8080/api/violations/log?${params}`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${token}` }
        }).catch(err => console.warn('Violation Log Error:', err));
    }, [sessionId, isActive]);

    // ========== TAB VISIBILITY DETECTION ==========
    useEffect(() => {
        if (!isActive) return;

        const handleVisibilityChange = () => {
            const isHidden = document.hidden;
            setIsTabActive(!isHidden);

            if (isHidden) {
                reportViolation('TAB_SWITCH', 'CRITICAL', 'Student switched away from exam tab');
            } else {
                reportViolation('TAB_FOCUS_RETURN', 'LOW', 'Student returned to exam tab');
            }
        };

        document.addEventListener('visibilitychange', handleVisibilityChange);
        return () => document.removeEventListener('visibilitychange', handleVisibilityChange);
    }, [isActive, reportViolation]);

    // ========== WINDOW BLUR DETECTION ==========
    useEffect(() => {
        if (!isActive) return;

        const handleBlur = () => {
            reportViolation('WINDOW_BLUR', 'CRITICAL', 'Exam window lost focus');
        };

        window.addEventListener('blur', handleBlur);
        return () => window.removeEventListener('blur', handleBlur);
    }, [isActive, reportViolation]);

    // ========== FULLSCREEN EXIT DETECTION ==========
    useEffect(() => {
        if (!isActive) return;

        const handleFullscreenChange = () => {
            if (!document.fullscreenElement) {
                reportViolation('FULLSCREEN_EXIT', 'HIGH', 'Student exited fullscreen mode');
            }
        };

        document.addEventListener('fullscreenchange', handleFullscreenChange);
        return () => document.removeEventListener('fullscreenchange', handleFullscreenChange);
    }, [isActive, reportViolation]);

    // ========== CURSOR POSITION MONITORING ==========
    useEffect(() => {
        if (!isActive) return;

        let cursorOutsideTime = 0;
        const CURSOR_THRESHOLD = 3000;

        const handleMouseMove = (e) => {
            const examArea = document.getElementById('exam-container');
            if (!examArea) return;

            const rect = examArea.getBoundingClientRect();
            const isOutside = e.clientX < rect.left || e.clientX > rect.right ||
                            e.clientY < rect.top || e.clientY > rect.bottom;

            setCursorOutside(isOutside);

            if (isOutside) {
                cursorOutsideTime += 100;
                if (cursorOutsideTime >= CURSOR_THRESHOLD && !violationTimeoutRef.current) {
                    violationTimeoutRef.current = setTimeout(() => {
                        reportViolation('SUSPICIOUS_CURSOR', 'MEDIUM', 
                            'Cursor outside exam area for extended period');
                        violationTimeoutRef.current = null;
                    }, 1000);
                }
            } else {
                cursorOutsideTime = 0;
                if (violationTimeoutRef.current) {
                    clearTimeout(violationTimeoutRef.current);
                    violationTimeoutRef.current = null;
                }
            }
        };

        document.addEventListener('mousemove', handleMouseMove);
        return () => document.removeEventListener('mousemove', handleMouseMove);
    }, [isActive, reportViolation]);

    // ========== COPY/PASTE BLOCKING ==========
    useEffect(() => {
        if (!isActive) return;

        const handleCopy = (e) => {
            e.preventDefault();
            reportViolation('COPY_PASTE_ATTEMPT', 'HIGH', 'Copy action detected during exam');
        };

        const handlePaste = (e) => {
            e.preventDefault();
            reportViolation('COPY_PASTE_ATTEMPT', 'HIGH', 'Paste action detected during exam');
        };

        const handleCut = (e) => {
            e.preventDefault();
            reportViolation('CUT_ATTEMPT', 'MEDIUM', 'Cut action detected');
        };

        document.addEventListener('copy', handleCopy);
        document.addEventListener('paste', handlePaste);
        document.addEventListener('cut', handleCut);

        return () => {
            document.removeEventListener('copy', handleCopy);
            document.removeEventListener('paste', handlePaste);
            document.removeEventListener('cut', handleCut);
        };
    }, [isActive, reportViolation]);

    // ========== RIGHT-CLICK & SCREENSHOT PREVENTION ==========
    useEffect(() => {
        if (!isActive) return;

        const handleContextMenu = (e) => {
            e.preventDefault();
            reportViolation('SCREENSHOT_ATTEMPT', 'MEDIUM', 
                'Right-click detected (possible screenshot attempt)');
        };

        const handlePrintScreen = (e) => {
            if (e.key === 'PrintScreen') {
                e.preventDefault();
                reportViolation('PRINTSCREEN_ATTEMPT', 'HIGH', 
                    'Print Screen key pressed - screenshot attempted');
            }
        };

        document.addEventListener('contextmenu', handleContextMenu);
        document.addEventListener('keydown', handlePrintScreen);

        return () => {
            document.removeEventListener('contextmenu', handleContextMenu);
            document.removeEventListener('keydown', handlePrintScreen);
        };
    }, [isActive, reportViolation]);

    // ========== KEYBOARD ACTIVITY MONITORING ==========
    useEffect(() => {
        if (!isActive) return;

        const handleKeyPress = (e) => {
            keyboardActivityRef.current++;
            
            // Check for suspicious key combinations
            if (e.ctrlKey || e.metaKey) {
                if (e.key === 'c' || e.key === 'v' || e.key === 'x' || e.key === 's') {
                    reportViolation('SUSPICIOUS_KEYS', 'HIGH', 
                        `Suspicious keyboard combination detected: ${e.key}`);
                }
            }

            // F12 (DevTools)
            if (e.key === 'F12') {
                e.preventDefault();
                reportViolation('DEVTOOLS_ATTEMPT', 'CRITICAL', 
                    'Developer Tools access attempted (F12 pressed)');
            }
        };

        document.addEventListener('keydown', handleKeyPress);
        return () => document.removeEventListener('keydown', handleKeyPress);
    }, [isActive, reportViolation]);

    // ========== AUDIO MONITORING ==========
    useEffect(() => {
        if (!isActive) return;

        let audioContext;
        let analyser;
        let microphone;
        let scriptProcessor;
        const NOISE_THRESHOLD = 0.5;

        const startAudioMonitoring = async () => {
            try {
                const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
                if (!stream) throw new Error('No audio stream');

                audioContext = new (window.AudioContext || window.webkitAudioContext)();
                analyser = audioContext.createAnalyser();
                microphone = audioContext.createMediaStreamSource(stream);
                scriptProcessor = audioContext.createScriptProcessor(2048, 1, 1);

                analyser.smoothingTimeConstant = 0.8;
                analyser.fftSize = 1024;

                microphone.connect(analyser);
                analyser.connect(scriptProcessor);
                scriptProcessor.connect(audioContext.destination);

                scriptProcessor.onaudioprocess = () => {
                    const array = new Uint8Array(analyser.frequencyBinCount);
                    analyser.getByteFrequencyData(array);
                    const values = array.reduce((a, b) => a + b, 0);
                    const average = values / array.length / 255;

                    if (average > NOISE_THRESHOLD) {
                        reportViolation('AUDIO_ANOMALY', 'MEDIUM', 
                            'High background noise or speech detected');
                    }
                };
            } catch (err) {
                console.warn('Audio monitoring unavailable:', err);
            }
        };

        startAudioMonitoring();

        return () => {
            if (audioContext) audioContext.close();
            if (scriptProcessor) scriptProcessor.disconnect();
            if (microphone) microphone.disconnect();
        };
    }, [isActive, reportViolation]);

    return {
        violations,
        isTabActive,
        cursorOutside,
        suspiciousActivity,
        reportViolation,
        violationCount: violations.length,
        criticalViolations: violations.filter(v => v.severity === 'CRITICAL').length,
        lastViolation: violations[violations.length - 1] || null
    };
};

export default useEnhancedProctoring;
