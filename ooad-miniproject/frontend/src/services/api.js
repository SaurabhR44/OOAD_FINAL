// API Configuration
const API_BASE_URL = 'http://localhost:8080/api';

// Auth API
export const authAPI = {
    login: async (credentials) => {
        try {
            const response = await fetch(`${API_BASE_URL}/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(credentials)
            });

            const data = await response.json();

            if (!response.ok) {
                const message = typeof data === 'string' ? data : data?.message || 'Authentication failed';
                throw new Error(message);
            }

            if (!data || !data.token) {
                throw new Error('Authentication succeeded but token not provided by server');
            }

            return data;
        } catch (err) {
            // Distinguish network problems (e.g. backend down / wrong port) from auth errors
            const message = (err instanceof TypeError && err.message === 'Failed to fetch')
                ? 'Unable to reach backend server. Ensure backend is running on http://localhost:8080.'
                : err.message;
            throw new Error(message);
        }
    },

    register: async (studentData) => {
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(studentData)
        });
        return response.json();
    }
};

// Exam API
export const examAPI = {
    getAllExams: async (token) => {
        const response = await fetch(`${API_BASE_URL}/exam/all`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        return response.json();
    },
    startExam: async (examId, studentId, token) => {
        const response = await fetch(`${API_BASE_URL}/exam/start?examId=${examId}&studentId=${studentId}`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${token}` }
        });
        return response.json();
    },

    submitAnswer: async (sessionId, questionId, selectedOptionId, token) => {
        const response = await fetch(
            `${API_BASE_URL}/exam/submit-answer?sessionId=${sessionId}&questionId=${questionId}&selectedOptionId=${selectedOptionId}`,
            {
                method: 'POST',
                headers: { 'Authorization': `Bearer ${token}` }
            }
        );
        return response.json();
    },

    submitExam: async (sessionId, token) => {
        const response = await fetch(`${API_BASE_URL}/exam/submit?sessionId=${sessionId}`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${token}` }
        });
        return response.json();
    },

    terminateExam: async (sessionId, reason, token) => {
        const response = await fetch(`${API_BASE_URL}/exam/terminate?sessionId=${sessionId}&reason=${encodeURIComponent(reason)}`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${token}` }
        });
        return response.json();
    },

    getStudentSessions: async (studentId, token) => {
        const response = await fetch(`${API_BASE_URL}/exam/sessions/student/${studentId}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        return response.json();
    }
};

// Violation API
export const violationAPI = {
    logViolation: async (sessionId, type, severity, description, evidencePath, token) => {
        const params = new URLSearchParams({
            sessionId,
            type,
            severity,
            description,
            ...(evidencePath && { evidencePath })
        });

        const response = await fetch(`${API_BASE_URL}/violations/log?${params}`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${token}` }
        });
        return response.json();
    }
};

// Admin API
export const adminAPI = {
    getDashboardStats: async (token) => {
        const response = await fetch(`${API_BASE_URL}/admin/dashboard/stats`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        return response.json();
    },

    getLiveSessions: async (token) => {
        const response = await fetch(`${API_BASE_URL}/admin/live-sessions`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        return response.json();
    },

    getAllViolations: async (token) => {
        const response = await fetch(`${API_BASE_URL}/admin/violations/all`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        return response.json();
    },

    getAllStudents: async (token) => {
        const response = await fetch(`${API_BASE_URL}/admin/students`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        return response.json();
    },

    getExams: async (token) => {
        const response = await fetch(`${API_BASE_URL}/admin/exams`, { headers: { 'Authorization': `Bearer ${token}` } });
        return response.json();
    },
    createExam: async (examData, token) => {
        const response = await fetch(`${API_BASE_URL}/admin/exams`, {
            method: 'POST', headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
            body: JSON.stringify(examData)
        });
        return response.json();
    },
    getQuestions: async (token) => {
        const response = await fetch(`${API_BASE_URL}/admin/questions`, { headers: { 'Authorization': `Bearer ${token}` } });
        return response.json();
    },
    createQuestion: async (questionData, token) => {
        const response = await fetch(`${API_BASE_URL}/admin/questions`, {
            method: 'POST', headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
            body: JSON.stringify(questionData)
        });
        return response.json();
    },
    getResults: async (token) => {
        const response = await fetch(`${API_BASE_URL}/admin/results`, { headers: { 'Authorization': `Bearer ${token}` } });
        return response.json();
    }
};
