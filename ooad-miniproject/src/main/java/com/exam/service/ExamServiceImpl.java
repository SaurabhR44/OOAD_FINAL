package com.exam.service;

import com.exam.factory.ExamSessionFactory;
import com.exam.model.*;
import com.exam.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// [PATTERN: Singleton] — Managed as a single bean instance by Spring IoC
// [PATTERN: Facade] — Orchestrates complex logic (creation, validation, audit, engine init) behind a simple interface
@Service
public class ExamServiceImpl implements ExamService {

    private final ExamSessionRepository examSessionRepository;
    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;
    private final AuditLogService auditLogService;
    private final ResultService resultService;
    private final ExamSessionFactory examSessionFactory;
    private final AdaptiveEngine adaptiveEngine;

    // [PRINCIPLE: DIP] — All dependencies injected via constructor interfaces
    public ExamServiceImpl(
            ExamSessionRepository examSessionRepository,
            ExamRepository examRepository,
            StudentRepository studentRepository,
            AuditLogService auditLogService,
            ResultService resultService,
            ExamSessionFactory examSessionFactory,
            AdaptiveEngine adaptiveEngine) {
        this.examSessionRepository = examSessionRepository;
        this.examRepository = examRepository;
        this.studentRepository = studentRepository;
        this.auditLogService = auditLogService;
        this.resultService = resultService;
        this.examSessionFactory = examSessionFactory;
        this.adaptiveEngine = adaptiveEngine;
    }

    @Override
    public List<Exam> getAllExams() {
        // [PATTERN: Iterator] — Stream API iterates collection elements
        return examRepository.findAll().stream().toList();
    }

    @Override
    @Transactional
    public ExamSession startExam(Long examId, Long studentId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Optional<ExamSession> existing = examSessionRepository.findByStudentIdAndExamId(studentId, examId);
        if (existing.isPresent() && existing.get().getState() != ExamSession.ExamState.SUBMITTED) {
            return existing.get();
        }

        // [GRASP: Creator] — ExamService creates ExamSession because it has the required data
        // [PATTERN: Builder] — readable, safe object construction
        ExamSession session = ExamSession.builder()
                .exam(exam)
                .student(student)
                .state(ExamSession.ExamState.ACTIVE)
                .timeRemaining(exam.getDurationMinutes() * 60)
                .currentDifficulty(DifficultyLevel.EASY)
                .violationScore(0)
                .warningCount(0)
                .isTerminated(false)
                .actualStartTime(java.time.LocalDateTime.now())
                .answers(new java.util.ArrayList<>()) // Explicit initialization to prevent null/lazy issues
                .proctoringLogs(new java.util.ArrayList<>())
                .build();

        ExamSession saved = examSessionRepository.save(session);

        auditLogService.log(student, saved, AuditLog.ActionType.EXAM_START, "Exam started: " + exam.getTitle());

        return saved;
    }

    @Override
    @Transactional
    public void submitExam(Long sessionId) {
        ExamSession session = examSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setState(ExamSession.ExamState.SUBMITTED);
        session.setSubmittedAt(LocalDateTime.now());

        examSessionRepository.save(session);
        auditLogService.log(session.getStudent(), session, AuditLog.ActionType.EXAM_SUBMIT, "Exam submitted");

        try {
            resultService.evaluateExam(sessionId);
        } catch (Exception e) {
            // Log evaluation failure
        }
    }

    @Override
    @Transactional
    public ExamSession pauseExam(Long sessionId) {
        ExamSession session = examSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getExam().getAllowPauseResume()) {
            throw new RuntimeException("Pause not allowed for this exam");
        }

        session.setState(ExamSession.ExamState.PAUSED);
        return examSessionRepository.save(session);
    }

    @Override
    @Transactional
    public ExamSession resumeExam(Long sessionId) {
        ExamSession session = examSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setState(ExamSession.ExamState.RESUMED);
        return examSessionRepository.save(session);
    }

    @Override
    @Transactional
    public ExamSession terminateExam(Long sessionId, String reason) {
        ExamSession session = examSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setState(ExamSession.ExamState.TERMINATED);
        session.setIsTerminated(true);
        session.setTerminationReason(reason);
        session.setSubmittedAt(LocalDateTime.now());

        ExamSession saved = examSessionRepository.save(session);
        auditLogService.log(session.getStudent(), session, AuditLog.ActionType.EXAM_TERMINATED, "Exam terminated: " + reason);

        return saved;
    }

    @Override
    public List<ExamSession> getActiveExamSessions() {
        return examSessionRepository.findByStateAndIsTerminatedFalse(ExamSession.ExamState.ACTIVE);
    }

    @Override
    public List<ExamSession> getStudentSessions(Long studentId) {
        return examSessionRepository.findByStudentId(studentId);
    }
}
