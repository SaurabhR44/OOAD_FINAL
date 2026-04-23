import React, { useState, useEffect, useCallback, useRef } from 'react';
import { examService } from '../services/apiService';
import { useProctoring } from '../hooks/useProctoring';
import WebcamProctor from './WebcamProctor';
import QuestionRenderer from './QuestionRenderer';

const KICK_THRESHOLD = 8;
const WARN_THRESHOLD = 5;

const Exam = ({ user, exam, onComplete }) => {
    const [questions, setQuestions] = useState([]);
    const [answers, setAnswers] = useState({});
    const [textAnswers, setTextAnswers] = useState({});
    const [currentIndex, setCurrentIndex] = useState(0);
    const [timeLeft, setTimeLeft] = useState((exam?.durationMinutes || 60) * 60);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [submitting, setSubmitting] = useState(false);
    const [sessionId, setSessionId] = useState(null);
    const [isTerminated, setIsTerminated] = useState(false);
    const [terminationReason, setTerminationReason] = useState('');
    const [showWarning, setShowWarning] = useState(false);
    const [violations, setViolations] = useState([]);
    const warnedAt = useRef(0);

    const handleTerminate = useCallback((reason) => {
        setIsTerminated(true);
        setTerminationReason(reason);
        if (sessionId) {
            examService.terminateExam(sessionId, reason).catch(() => {});
        }
    }, [sessionId]);

    // Violation handler — works from the very first second, no backend needed
    const handleViolation = useCallback((type, severity, description) => {
        if (isTerminated) return;

        // Flash the screen red
        document.body.style.transition = 'background 0.1s';
        document.body.style.background = '#7f1d1d';
        setTimeout(() => { document.body.style.background = ''; }, 600);

        setViolations(prev => {
            const updated = [...prev, { type, severity, description, time: new Date().toLocaleTimeString() }];

            if (updated.length >= KICK_THRESHOLD) {
                handleTerminate(`EXAM AUTOMATICALLY TERMINATED: ${updated.length} integrity violations detected. Your institution has been notified.`);
            } else if (updated.length >= WARN_THRESHOLD && updated.length > warnedAt.current) {
                warnedAt.current = updated.length;
                setShowWarning(true);
            }

            return updated;
        });

        // Log to backend if session exists
        if (sessionId) {
            import('../services/apiService').then(({ proctoringService }) => {
                proctoringService.logViolation({ sessionId, type, severity, description }).catch(() => {});
            });
        }
    }, [isTerminated, sessionId, handleTerminate]);

    // Tab switch detection
    useEffect(() => {
        const onBlur = () => handleViolation('TAB_SWITCH', 'HIGH', 'Student switched away from exam tab');
        const onCopy = (e) => { e.preventDefault(); handleViolation('COPY_PASTE', 'HIGH', 'Clipboard copy action blocked'); };
        const onPaste = (e) => { e.preventDefault(); handleViolation('COPY_PASTE', 'HIGH', 'Clipboard paste action blocked'); };
        const onContextMenu = (e) => { e.preventDefault(); handleViolation('RIGHT_CLICK', 'MEDIUM', 'Right-click menu blocked'); };
        const onKeyDown = (e) => {
            if (e.ctrlKey && ['c', 'v', 'u', 'a', 'p', 's'].includes(e.key.toLowerCase())) {
                e.preventDefault();
                handleViolation('KEYBOARD_SHORTCUT', 'HIGH', `Blocked shortcut: Ctrl+${e.key.toUpperCase()}`);
            }
        };

        window.addEventListener('blur', onBlur);
        document.addEventListener('copy', onCopy);
        document.addEventListener('paste', onPaste);
        document.addEventListener('contextmenu', onContextMenu);
        document.addEventListener('keydown', onKeyDown);

        return () => {
            window.removeEventListener('blur', onBlur);
            document.removeEventListener('copy', onCopy);
            document.removeEventListener('paste', onPaste);
            document.removeEventListener('contextmenu', onContextMenu);
            document.removeEventListener('keydown', onKeyDown);
        };
    }, [handleViolation]);

    // Timer
    useEffect(() => {
        const timer = setInterval(() => {
            setTimeLeft(prev => {
                if (prev <= 1) {
                    handleTerminate('Time limit reached. Exam auto-submitted.');
                    return 0;
                }
                return prev - 1;
            });
        }, 1000);
        return () => clearInterval(timer);
    }, [handleTerminate]);

    // Load exam
    const initExam = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            console.log('Starting exam:', exam.id, 'for student:', user.id);
            const data = await examService.startExam(exam.id, user.id);
            console.log('Exam start response:', data);
            if (data?.session) {
                setSessionId(data.session.id);
                if (data.firstQuestion) {
                    setQuestions([data.firstQuestion]);
                    setError(null);
                } else {
                    setError('The exam has no questions yet. Please contact your administrator.');
                }
            } else {
                setError('Could not start exam session. Server error.');
            }
        } catch (err) {
            console.error('Exam start error:', err);
            setError(`Connection failed: ${err.message}`);
        } finally {
            setLoading(false);
        }
    }, [exam.id, user.id]);

    useEffect(() => {
        initExam();
    }, [initExam]);

    const handleNext = async () => {
        const currentQuestion = questions[currentIndex];
        if (!currentQuestion || !sessionId || isTerminated) return;

        setSubmitting(true);
        try {
            const response = await examService.submitAnswer(
                sessionId,
                currentQuestion.id,
                answers[currentIndex],
                textAnswers[currentIndex]
            );
            if (response?.nextQuestion) {
                setQuestions(prev => [...prev, response.nextQuestion]);
                setCurrentIndex(prev => prev + 1);
            } else {
                await examService.submitExam(sessionId);
                onComplete(sessionId);
            }
        } catch (err) {
            console.error('Submit error:', err);
        } finally {
            setSubmitting(false);
        }
    };

    // ---- TERMINATED SCREEN ----
    if (isTerminated) {
        return (
            <div className="min-h-screen bg-black flex flex-col items-center justify-center text-white p-10 text-center">
                <div className="text-8xl mb-8">🚫</div>
                <h1 className="text-5xl font-black text-red-500 mb-6 tracking-tighter">EXAM TERMINATED</h1>
                <div className="bg-red-900/30 border-2 border-red-500/50 p-10 rounded-3xl max-w-2xl mb-10">
                    <p className="text-slate-300 text-lg leading-relaxed">{terminationReason}</p>
                </div>
                <p className="text-slate-500 text-sm mb-8">This incident has been logged and reported to your institution.</p>
                <button onClick={() => onComplete(sessionId)} className="bg-white text-black px-12 py-4 rounded-2xl font-black hover:bg-slate-100 transition-all">
                    EXIT TO DASHBOARD
                </button>
            </div>
        );
    }

    // ---- WARNING MODAL ----
    if (showWarning) {
        return (
            <div className="fixed inset-0 z-[999] bg-black/95 flex items-center justify-center p-6">
                <div className="bg-[#0f0f0f] border-2 border-amber-500 p-12 rounded-3xl max-w-lg text-center">
                    <div className="text-6xl mb-6">⚠️</div>
                    <h2 className="text-3xl font-black text-amber-400 mb-4">INTEGRITY WARNING</h2>
                    <p className="text-slate-400 mb-4 leading-relaxed">
                        <span className="text-white font-black text-xl">{violations.length}</span> violations have been recorded.
                    </p>
                    <p className="text-slate-500 mb-10 text-sm">
                        At <span className="text-red-400 font-bold">{KICK_THRESHOLD} violations</span>, your exam will be automatically terminated and reported to your institution.
                    </p>
                    <button
                        onClick={() => setShowWarning(false)}
                        className="w-full bg-amber-500 text-black font-black py-4 rounded-2xl hover:bg-amber-400 transition-all text-lg"
                    >
                        I UNDERSTAND — RESUME EXAM
                    </button>
                </div>
            </div>
        );
    }

    // ---- LOADING / ERROR SCREEN ----
    if (loading || (!questions.length && !error)) {
        return (
            <div className="min-h-screen bg-[#020617] flex flex-col items-center justify-center text-white">
                <div className="w-16 h-16 border-4 border-blue-600/30 border-t-blue-500 rounded-full animate-spin mb-6"></div>
                <p className="text-blue-400 font-bold tracking-widest text-sm uppercase animate-pulse">Loading Exam...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="min-h-screen bg-[#020617] flex flex-col items-center justify-center text-white p-10 text-center">
                <div className="text-6xl mb-6">❌</div>
                <h2 className="text-2xl font-black text-red-400 mb-4">Failed to Load Questions</h2>
                <p className="text-slate-400 mb-8 max-w-md">{error}</p>
                <button onClick={initExam} className="bg-blue-600 px-10 py-4 rounded-2xl font-bold hover:bg-blue-500 transition-all">
                    Retry
                </button>
            </div>
        );
    }

    const currentQuestion = questions[currentIndex];
    const mins = Math.floor(timeLeft / 60);
    const secs = (timeLeft % 60).toString().padStart(2, '0');

    // ---- MAIN EXAM SCREEN ----
    return (
        <div className="min-h-screen bg-[#020617] text-white font-sans flex flex-col">

            {/* TOP BAR */}
            <header className="h-16 border-b border-white/5 px-8 flex items-center justify-between bg-slate-900/60 backdrop-blur-xl sticky top-0 z-50 shrink-0">
                <div className="flex items-center gap-4">
                    <span className="bg-red-600 text-white text-[10px] font-black px-3 py-1 rounded tracking-widest animate-pulse">LIVE PROCTORING</span>
                    <h1 className="text-sm font-bold text-slate-400 uppercase tracking-wider hidden sm:block">{exam.title}</h1>
                </div>
                <div className="flex items-center gap-6">
                    <div className={`text-xs font-black px-3 py-1 rounded-lg border ${violations.length === 0 ? 'border-emerald-500/30 text-emerald-400' : 'border-red-500/50 text-red-400 animate-pulse'}`}>
                        {violations.length} VIOLATIONS
                    </div>
                    <div className={`font-mono text-2xl font-black ${timeLeft < 300 ? 'text-red-500 animate-pulse' : 'text-white'}`}>
                        {mins}:{secs}
                    </div>
                </div>
            </header>

            <div className="flex-1 flex overflow-hidden">

                {/* LEFT: Question Navigator */}
                <aside className="w-64 shrink-0 border-r border-white/5 p-6 bg-slate-950 overflow-y-auto">
                    <p className="text-[10px] font-black text-slate-600 uppercase tracking-widest mb-4">Questions</p>
                    <div className="grid grid-cols-4 gap-2">
                        {Array.from({ length: 30 }).map((_, i) => (
                            <button
                                key={i}
                                disabled={i >= questions.length}
                                onClick={() => i < questions.length && setCurrentIndex(i)}
                                className={`h-10 rounded-lg text-xs font-black border transition-all ${
                                    i === currentIndex
                                        ? 'bg-blue-600 border-blue-400 text-white'
                                        : (answers[i] !== undefined || textAnswers[i])
                                        ? 'bg-emerald-600/20 border-emerald-500/30 text-emerald-400'
                                        : i < questions.length
                                        ? 'bg-white/5 border-white/10 text-slate-500 hover:text-white'
                                        : 'opacity-10 cursor-not-allowed'
                                }`}
                            >
                                {i + 1}
                            </button>
                        ))}
                    </div>

                    {/* Violation dots */}
                    <div className="mt-8 pt-6 border-t border-white/5">
                        <p className="text-[10px] font-black text-slate-600 uppercase tracking-widest mb-3">Integrity</p>
                        <div className="flex flex-wrap gap-1.5">
                            {Array.from({ length: KICK_THRESHOLD }).map((_, i) => (
                                <div key={i} className={`w-3 h-3 rounded-full ${i < violations.length ? 'bg-red-500' : 'bg-white/10'}`} />
                            ))}
                        </div>
                        {violations.length > 0 && (
                            <p className="text-red-400 text-[10px] font-bold mt-2">{violations.length}/{KICK_THRESHOLD} — {KICK_THRESHOLD - violations.length} remaining</p>
                        )}
                    </div>
                </aside>

                {/* CENTER: Question */}
                <main className="flex-1 overflow-y-auto p-10">
                    <div className="max-w-3xl mx-auto">
                        <div className="flex items-center gap-3 mb-8">
                            <span className="text-blue-500 text-xs font-black uppercase tracking-widest">Question {currentIndex + 1}</span>
                        </div>

                        <QuestionRenderer
                            question={currentQuestion}
                            selectedOption={answers[currentIndex]}
                            textAnswer={textAnswers[currentIndex]}
                            onOptionSelect={(id) => setAnswers(prev => ({ ...prev, [currentIndex]: id }))}
                            onTextChange={(text) => setTextAnswers(prev => ({ ...prev, [currentIndex]: text }))}
                            submitting={submitting}
                        />

                        <div className="mt-10 flex justify-between items-center">
                            <button
                                onClick={() => setCurrentIndex(p => p - 1)}
                                disabled={currentIndex === 0 || submitting}
                                className="px-6 py-3 text-slate-500 font-bold text-sm hover:text-white transition-all disabled:opacity-0"
                            >
                                ← Back
                            </button>
                            <button
                                onClick={handleNext}
                                disabled={(answers[currentIndex] === undefined && !textAnswers[currentIndex]) || submitting}
                                className="bg-blue-600 hover:bg-blue-500 text-white px-10 py-4 rounded-2xl font-black text-sm tracking-wide transition-all active:scale-95 disabled:opacity-30 disabled:cursor-not-allowed"
                            >
                                {submitting ? 'Saving...' : 'Save & Continue →'}
                            </button>
                        </div>
                    </div>
                </main>

                {/* RIGHT: Webcam */}
                <aside className="w-80 shrink-0 border-l border-white/5 p-6 bg-slate-900/30 flex flex-col gap-6">
                    <div>
                        <div className="flex items-center justify-between mb-3">
                            <p className="text-[10px] font-black text-slate-500 uppercase tracking-widest">Biometric Feed</p>
                            <span className="text-[9px] font-bold text-red-500 animate-pulse">● LIVE</span>
                        </div>
                        <div className="rounded-2xl overflow-hidden bg-black border border-white/10 aspect-video">
                            <WebcamProctor onViolation={handleViolation} isActive={!isTerminated} />
                        </div>
                    </div>

                    <div className="flex-1 overflow-hidden flex flex-col">
                        <p className="text-[10px] font-black text-slate-500 uppercase tracking-widest mb-3">Violation Log</p>
                        <div className="flex-1 overflow-y-auto space-y-2 pr-1">
                            {violations.length === 0 ? (
                                <div className="p-4 rounded-xl border border-dashed border-white/5 text-center text-slate-600 text-xs">No violations detected</div>
                            ) : (
                                violations.slice().reverse().map((v, i) => (
                                    <div key={i} className="p-3 rounded-xl bg-red-900/20 border border-red-500/20">
                                        <p className="text-[9px] font-black text-red-400 uppercase">{v.type}</p>
                                        <p className="text-[9px] text-slate-500 mt-0.5">{v.time}</p>
                                    </div>
                                ))
                            )}
                        </div>
                    </div>
                </aside>
            </div>
        </div>
    );
};

export default Exam;
