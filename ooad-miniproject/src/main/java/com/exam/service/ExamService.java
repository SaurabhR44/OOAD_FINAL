package com.exam.service;

import com.exam.model.Exam;
import com.exam.model.ExamSession;
import com.exam.model.Question;
import java.util.List;

// [PRINCIPLE: ISP] — Client-specific interface for exam operations
public interface ExamService {
    List<Exam> getAllExams();
    ExamSession startExam(Long examId, Long studentId);
    void submitExam(Long sessionId);
    ExamSession pauseExam(Long sessionId);
    ExamSession resumeExam(Long sessionId);
    ExamSession terminateExam(Long sessionId, String reason);
    List<ExamSession> getActiveExamSessions();
    List<ExamSession> getStudentSessions(Long studentId);
}
