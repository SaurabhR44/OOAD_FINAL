package com.exam.controller;

import com.exam.model.*;
import com.exam.repository.*;
import com.exam.service.*;
import com.exam.service.adapter.ReportExporter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

// [PATTERN: Singleton] — Managed as a single bean instance by Spring IoC
// [GRASP: Controller] — Receives administrative events and delegates to subsystem facades
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;
    private final QuestionRepository questionRepository;
    private final ExamService examService;
    private final ViolationService violationService;
    private final ViolationLogRepository violationLogRepository;
    private final ExamResultRepository examResultRepository;
    private final ReportExporter excelExporter;
    private final ReportExporter pdfExporter;

    // [PRINCIPLE: DIP] — All dependencies injected via constructor interfaces
    public AdminController(
            ExamRepository examRepository,
            StudentRepository studentRepository,
            QuestionRepository questionRepository,
            ExamService examService,
            ViolationService violationService,
            ViolationLogRepository violationLogRepository,
            ExamResultRepository examResultRepository,
            @Qualifier("excelReportAdapter") ReportExporter excelExporter,
            @Qualifier("pdfReportAdapter") ReportExporter pdfExporter) {
        this.examRepository = examRepository;
        this.studentRepository = studentRepository;
        this.questionRepository = questionRepository;
        this.examService = examService;
        this.violationService = violationService;
        this.violationLogRepository = violationLogRepository;
        this.examResultRepository = examResultRepository;
        this.excelExporter = excelExporter;
        this.pdfExporter = pdfExporter;
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<?> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", studentRepository.count());
        stats.put("totalExams", examRepository.count());
        stats.put("totalQuestions", questionRepository.count());
        stats.put("activeSessions", examService.getActiveExamSessions().size());
        stats.put("unreviewedViolations", violationService.getUnreviewedViolations().size());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/live-sessions")
    public ResponseEntity<?> getLiveSessions() {
        return ResponseEntity.ok(examService.getActiveExamSessions());
    }

    // [PATTERN: Adapter] — Export endpoints
    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportExcel() {
        List<Map<String, Object>> data = prepareExportData();
        byte[] bytes = excelExporter.export(data);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes);
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf() {
        List<Map<String, Object>> data = prepareExportData();
        byte[] bytes = pdfExporter.export(data);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }

    private List<Map<String, Object>> prepareExportData() {
        List<Map<String, Object>> data = new ArrayList<>();
        examResultRepository.findAll().forEach(r -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("SRN", r.getExamSession().getStudent().getSrn());
            row.put("Exam", r.getExamSession().getExam().getTitle());
            row.put("Score", r.getScore());
            row.put("Date", r.getExamSession().getSubmittedAt());
            data.add(row);
        });
        return data;
    }

    @GetMapping("/violations/all")
    public ResponseEntity<?> getAllViolations() {
        return ResponseEntity.ok(violationLogRepository.findAll());
    }

    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents() {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    @GetMapping("/exams")
    public ResponseEntity<?> getAllExams() {
        return ResponseEntity.ok(examRepository.findAll());
    }

    @PostMapping("/exams")
    public ResponseEntity<?> createExam(@RequestBody Exam exam) {
        return ResponseEntity.ok(examRepository.save(exam));
    }

    @GetMapping("/questions")
    public ResponseEntity<?> getAllQuestions() {
        return ResponseEntity.ok(questionRepository.findAll());
    }

    @GetMapping("/results")
    public ResponseEntity<?> getAllResults() {
        return ResponseEntity.ok(examResultRepository.findAll());
    }
}
