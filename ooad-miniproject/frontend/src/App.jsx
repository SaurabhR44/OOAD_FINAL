import React, { useState, useEffect } from 'react';
import Login from './components/Login';
import Dashboard from './components/Dashboard';
import Exam from './components/Exam';
import AdminDashboard from './components/AdminDashboard';
import Results from './components/Results';

// [PATTERN: Error Boundary] — Last line of defense against White Screen
class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null };
  }
  static getDerivedStateFromError(error) { return { hasError: true, error }; }
  componentDidCatch(error, errorInfo) { console.error("CRITICAL UI CRASH:", error, errorInfo); }
  render() {
    if (this.state.hasError) {
      return (
        <div className="min-h-screen bg-red-950 flex flex-col items-center justify-center p-10 text-white font-sans">
          <h1 className="text-6xl mb-4">🚨</h1>
          <h2 className="text-2xl font-black uppercase tracking-widest mb-4">Interface Failure</h2>
          <pre className="bg-black/50 p-6 rounded-2xl text-xs text-red-400 max-w-2xl overflow-auto border border-red-500/20">
            {this.state.error?.toString()}
          </pre>
          <button onClick={() => window.location.reload()} className="mt-8 bg-white text-red-900 px-8 py-3 rounded-xl font-bold">RELOAD SYSTEM</button>
        </div>
      );
    }
    return this.props.children;
  }
}

const App = () => {
  const [currentScreen, setCurrentScreen] = useState('LOGIN');
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('proctoriq_user');
    return saved ? JSON.parse(saved) : null;
  });
  const [activeExam, setActiveExam] = useState(null);
  const [completedSessionId, setCompletedSessionId] = useState(null);

  const handleLogin = (userData) => {
    const newUser = {
      id: userData.id,
      name: userData.username || 'User',
      regNo: userData.username,
      token: userData.token,
      role: userData.role
    };

    setUser(newUser);
    // [FIX] Save token separately for apiService interceptor
    localStorage.setItem('token', userData.token);
    localStorage.setItem('proctoriq_user', JSON.stringify(newUser));

    if (userData.role === 'ADMIN' || userData.role === 'FACULTY') {
      setCurrentScreen('ADMIN_DASHBOARD');
    } else {
      setCurrentScreen('DASHBOARD');
    }
  };

  const handleLogout = () => {
    setUser(null);
    localStorage.removeItem('token');
    localStorage.removeItem('proctoriq_user');
    setActiveExam(null);
    setCompletedSessionId(null);
    setCurrentScreen('LOGIN');
  };

  const handleStartExam = (exam) => {
    setActiveExam(exam);
    setCurrentScreen('EXAM');
  };

  const handleExamComplete = (sessionId) => {
    setCompletedSessionId(sessionId);
    setCurrentScreen('RESULTS');
  };

  const handleReturnToDashboard = () => {
    setActiveExam(null);
    setCompletedSessionId(null);
    setCurrentScreen('DASHBOARD');
  };

  return (
    <ErrorBoundary>
      <div>
        {currentScreen === 'LOGIN' && (
          <Login onLogin={handleLogin} />
        )}

        {currentScreen === 'DASHBOARD' && user && (
          <Dashboard
            user={user}
            onStartExam={handleStartExam}
            onLogout={handleLogout}
          />
        )}

        {currentScreen === 'ADMIN_DASHBOARD' && user && (
          <AdminDashboard
            user={user}
            onLogout={handleLogout}
          />
        )}

        {currentScreen === 'EXAM' && user && activeExam && (
          <Exam
            user={user}
            exam={activeExam}
            onComplete={handleExamComplete}
          />
        )}

        {currentScreen === 'RESULTS' && user && activeExam && completedSessionId && (
          <Results
            sessionId={completedSessionId}
            exam={activeExam}
            token={user.token}
            onReturn={handleReturnToDashboard}
          />
        )}
      </div>
    </ErrorBoundary>
  );
};

export default App;
