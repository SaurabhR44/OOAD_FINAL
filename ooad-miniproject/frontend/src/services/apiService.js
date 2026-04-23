import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

// [GRASP: Low Coupling] — Frontend components talk only to this service, never direct fetch
const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Request Interceptor for Auth
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}, (error) => Promise.reject(error));

// Response Interceptor for Errors
api.interceptors.response.use(
    (response) => response.data,
    (error) => {
        const message = error.response?.data?.message || error.message || 'API Request Failed';
        return Promise.reject(new Error(message));
    }
);

export const authService = {
    login: (credentials) => api.post('/auth/login', credentials),
    register: (studentData) => api.post('/auth/register', studentData),
};

export const examService = {
    getAllExams: () => api.get('/exam/all'),
    startExam: (examId, studentId) => api.post(`/exam/start?examId=${examId}&studentId=${studentId}`),
    submitAnswer: (sessionId, questionId, selectedOptionId, textAnswer) => {
        let url = `/exam/submit-answer?sessionId=${sessionId}&questionId=${questionId}`;
        if (selectedOptionId) url += `&selectedOptionId=${selectedOptionId}`;
        if (textAnswer) url += `&textAnswer=${encodeURIComponent(textAnswer)}`;
        return api.post(url);
    },
    submitExam: (sessionId) => api.post(`/exam/submit?sessionId=${sessionId}`),
    terminateExam: (sessionId, reason) => api.post(`/exam/terminate?sessionId=${sessionId}&reason=${encodeURIComponent(reason)}`),
    getStudentSessions: (studentId) => api.get(`/exam/sessions/student/${studentId}`),
};

export const proctoringService = {
    logViolation: (data) => {
        const params = new URLSearchParams(data);
        return api.post(`/violations/log?${params.toString()}`);
    },
    getLiveSessions: () => api.get('/admin/live-sessions'),
};

export const adminService = {
    getDashboardStats: () => api.get('/admin/dashboard/stats'),
    getResults: () => api.get('/admin/results'),
    // Export functions
    exportExcel: () => api.get('/admin/export/excel', { responseType: 'blob' }),
    exportPdf: () => api.get('/admin/export/pdf', { responseType: 'blob' }),
};

export default api;
