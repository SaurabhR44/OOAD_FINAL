import { useState, useEffect, useRef, useCallback } from 'react';
import { proctoringService } from '../services/apiService';

/**
 * Custom hook for exam proctoring
 */
export const useProctoring = (sessionId, isActive = true) => {
    const [violations, setViolations] = useState([]);
    const [isTabActive, setIsTabActive] = useState(true);
    const [cursorOutside, setCursorOutside] = useState(false);
    const lastViolationTimeRef = useRef({});

    // Standard violation reporting
    const reportViolation = useCallback(async (type, severity, description) => {
        // Essential checks to prevent runtime errors
        if (!sessionId || !isActive) {
            console.log(`ℹ️ Skipping violation report (${type}): Session not ready or inactive`);
            return;
        }

        // Prevent duplicate spam (8s debounce for same type)
        const now = Date.now();
        if (lastViolationTimeRef.current[type] && now - lastViolationTimeRef.current[type] < 8000) {
            return;
        }
        lastViolationTimeRef.current[type] = now;

        const violation = { type, severity, description, timestamp: new Date() };
        setViolations(prev => [...prev, violation]);

        try {
            console.log(`🚨 Flagging Violation: ${type} - ${description}`);
            await proctoringService.logViolation({
                sessionId,
                type,
                severity,
                description
            });
        } catch (err) {
            console.warn('⚠️ Violation sync error (logged locally):', err.message);
        }
    }, [sessionId, isActive]);

    // 1. Visibility & Focus
    useEffect(() => {
        if (!isActive) return;

        const handleVisibilityChange = () => {
            if (typeof document === 'undefined') return;
            const isHidden = document.hidden;
            setIsTabActive(!isHidden);
            if (isHidden) {
                reportViolation('TAB_SWITCH', 'MEDIUM', 'Student switched away from exam tab');
            }
        };

        const handleBlur = () => {
            setIsTabActive(false);
            reportViolation('WINDOW_BLUR', 'MEDIUM', 'Exam window focus lost');
        };

        const handleFocus = () => {
            setIsTabActive(true);
        };

        document.addEventListener('visibilitychange', handleVisibilityChange);
        window.addEventListener('blur', handleBlur);
        window.addEventListener('focus', handleFocus);

        return () => {
            document.removeEventListener('visibilitychange', handleVisibilityChange);
            window.removeEventListener('blur', handleBlur);
            window.removeEventListener('focus', handleFocus);
        };
    }, [isActive, reportViolation]);

    // 2. Keyboard Prevention (Anti-Hack)
    useEffect(() => {
        if (!isActive) return;

        const handleKeyDown = (e) => {
            if (!e) return;

            // PrintScreen
            if (e.key === 'PrintScreen') {
                e.preventDefault();
                reportViolation('SCREENSHOT_ATTEMPT', 'HIGH', 'PrintScreen key pressed');
                return;
            }

            // Ctrl/Meta combos
            if (e.ctrlKey || e.metaKey) {
                const key = e.key ? e.key.toLowerCase() : '';
                if (['c', 'v', 'u', 's', 'p'].includes(key)) {
                    e.preventDefault();
                    reportViolation('COPY_PASTE_ATTEMPT', 'HIGH', `Blocked shortcut: ${key.toUpperCase()}`);
                }
            }

            // DevTools
            if (e.key === 'F12' || (e.ctrlKey && e.shiftKey && (e.key === 'I' || e.key === 'J'))) {
                e.preventDefault();
                reportViolation('DEVTOOLS_ATTEMPT', 'CRITICAL', 'Restricted developer tools access');
            }
        };

        window.addEventListener('keydown', handleKeyDown);
        return () => window.removeEventListener('keydown', handleKeyDown);
    }, [isActive, reportViolation]);

    // 3. Right Click
    useEffect(() => {
        if (!isActive) return;
        const handleContextMenu = (e) => {
            if (e && e.preventDefault) e.preventDefault();
            reportViolation('RESTRICTED_ACTION', 'LOW', 'Right-click menu blocked');
        };
        document.addEventListener('contextmenu', handleContextMenu);
        return () => document.removeEventListener('contextmenu', handleContextMenu);
    }, [isActive, reportViolation]);

    // 4. Cursor Boundary Detection
    useEffect(() => {
        if (!isActive) return;
        const handleMouseLeave = () => {
            setCursorOutside(true);
            reportViolation('SUSPICIOUS_CURSOR', 'LOW', 'Cursor moved outside browser bounds');
        };
        const handleMouseEnter = () => setCursorOutside(false);
        
        if (document && document.documentElement) {
            document.documentElement.addEventListener('mouseleave', handleMouseLeave);
            document.documentElement.addEventListener('mouseenter', handleMouseEnter);
        }
        
        return () => {
            if (document && document.documentElement) {
                document.documentElement.removeEventListener('mouseleave', handleMouseLeave);
                document.documentElement.removeEventListener('mouseenter', handleMouseEnter);
            }
        };
    }, [isActive, reportViolation]);

    // 5. Global Clipboard Hijack
    useEffect(() => {
        if (!isActive) return;
        const handleCopy = (e) => {
            if (e && e.preventDefault) e.preventDefault();
            reportViolation('COPY_PASTE_ATTEMPT', 'HIGH', 'Selection copy attempt');
        };
        const handlePaste = (e) => {
            if (e && e.preventDefault) e.preventDefault();
            reportViolation('COPY_PASTE_ATTEMPT', 'HIGH', 'Clipboard paste attempt');
        };
        document.addEventListener('copy', handleCopy);
        document.addEventListener('paste', handlePaste);
        return () => {
            document.removeEventListener('copy', handleCopy);
            document.removeEventListener('paste', handlePaste);
        };
    }, [isActive, reportViolation]);

    return {
        violations,
        isTabActive,
        cursorOutside,
        reportViolation
    };
};
