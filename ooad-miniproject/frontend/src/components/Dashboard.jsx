import React, { useState, useEffect } from 'react';
import { Play, Clock, BarChart2, BookOpen, User, LogOut, CheckCircle, Monitor } from 'lucide-react';
import { examAPI } from '../services/api';


const NavItem = ({ icon: Icon, label, active, onClick }) => (
    <button
        onClick={onClick}
        className={`w-full flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium transition-colors ${active
            ? 'bg-blue-50 text-blue-700'
            : 'text-slate-600 hover:bg-slate-50 hover:text-slate-900'
            }`}
    >
        <Icon size={20} />
        {label}
    </button>
);

const StatCard = ({ label, value, icon: Icon, color }) => {
    const colors = {
        blue: 'bg-blue-50 text-blue-600',
        green: 'bg-green-50 text-green-600',
        purple: 'bg-purple-50 text-purple-600'
    };

    return (
        <div className="bg-white p-6 rounded-xl border border-slate-200 shadow-sm flex items-center gap-4">
            <div className={`w-12 h-12 rounded-lg flex items-center justify-center ${colors[color]}`}>
                <Icon size={24} />
            </div>
            <div>
                <p className="text-sm text-slate-500 font-medium">{label}</p>
                <p className="text-2xl font-bold text-slate-900">{value}</p>
            </div>
        </div>
    );
};

const Dashboard = ({ user, onStartExam, onLogout }) => {
    const [activeTab, setActiveTab] = useState('dashboard');
    const [stats, setStats] = useState({ completed: 0, average: 0, pending: 0 });
    const [exams, setExams] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchExams = async () => {
            try {
                const token = user.token || localStorage.getItem('token');
                const data = await examAPI.getAllExams(token);
                setExams(data || []);
                setStats(prev => ({ ...prev, pending: data?.length || 0 }));
            } catch (err) {
                console.error('Failed to fetch exams:', err);
            } finally {
                setLoading(false);
            }
        };
        fetchExams();
    }, [user]);

    return (
        <div className="min-h-screen bg-slate-50 flex overflow-hidden">
            {/* Sidebar */}
            <div className="w-72 glass border-r border-white/20 flex flex-col relative z-20 shadow-2xl">
                <div className="p-8 border-b border-slate-100/50">
                    <div className="flex items-center gap-3">
                        <div className="w-10 h-10 rounded-2xl bg-blue-600 flex items-center justify-center shadow-lg shadow-blue-500/20 rotate-3">
                            <Monitor className="text-white" size={24} />
                        </div>
                        <h2 className="text-2xl font-black text-slate-900 tracking-tighter">
                            ProctorIQ
                        </h2>
                    </div>
                </div>

                <nav className="flex-1 p-6 space-y-2">
                    <NavItem
                        icon={BookOpen}
                        label="Learning Hub"
                        active={activeTab === 'dashboard'}
                        onClick={() => setActiveTab('dashboard')}
                    />
                    <NavItem
                        icon={BarChart2}
                        label="Insight Board"
                        active={activeTab === 'results'}
                        onClick={() => setActiveTab('results')}
                    />
                </nav>

                <div className="p-6 border-t border-slate-100/50">
                    <div className="flex items-center gap-4 bg-slate-50/50 p-3 rounded-2xl border border-slate-200/50">
                        <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-blue-500 to-indigo-600 flex items-center justify-center text-white font-bold shadow-md">
                            {user.name?.charAt(0) || 'U'}
                        </div>
                        <div className="flex-1 min-w-0">
                            <p className="text-sm font-bold text-slate-900 truncate uppercase tracking-tight">{user.name}</p>
                            <p className="text-[10px] text-slate-500 font-bold uppercase tracking-widest truncate">{user.regNo}</p>
                        </div>
                        <button
                            onClick={onLogout}
                            className="p-2.5 text-slate-400 hover:text-red-600 hover:bg-red-50 rounded-xl transition-all"
                        >
                            <LogOut size={20} />
                        </button>
                    </div>
                </div>
            </div>

            {/* Main Content */}
            <div className="flex-1 overflow-y-auto relative bg-[#f8fafc]">
                {/* Decorative background element */}
                <div className="absolute top-0 right-0 w-[500px] h-[500px] bg-blue-100/30 rounded-full blur-[120px] -mr-48 -mt-48 pointer-events-none"></div>

                <header className="px-10 py-10 relative z-10">
                    <div className="flex justify-between items-end">
                        <div>
                            <p className="text-blue-600 font-bold text-xs uppercase tracking-[0.2em] mb-2">Systems Online</p>
                            <h1 className="text-4xl font-extrabold text-slate-900 tracking-tight leading-none">
                                {activeTab === 'dashboard' ? `Welcome Back, ${user.name.split(' ')[0]}` : 'Performance Insights'}
                            </h1>
                        </div>
                        <div className="flex items-center gap-2 bg-white px-4 py-2 rounded-2xl border border-slate-200 shadow-sm text-sm font-bold text-slate-600">
                            <Clock size={16} className="text-blue-500" />
                            {new Date().toLocaleDateString('en-US', { weekday: 'long', month: 'short', day: 'numeric' })}
                        </div>
                    </div>
                </header>

                <main className="px-10 pb-10 relative z-10">
                    {activeTab === 'dashboard' && (
                        <div className="space-y-10">
                            {/* Stats Row */}
                            <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                                <StatCard label="Live Assessments" value={stats.pending} icon={Clock} color="blue" />
                                <StatCard label="Success Rate" value={`${stats.completed}%`} icon={CheckCircle} color="green" />
                                <StatCard label="Avg Score" value={`${stats.average}%`} icon={BarChart2} color="purple" />
                            </div>

                            <div>
                                <h3 className="text-sm font-bold text-slate-400 uppercase tracking-widest mb-6 px-1">Active Modules</h3>
                                {loading ? (
                                    <div className="flex justify-center p-20 glass rounded-3xl">
                                        <div className="w-12 h-12 border-4 border-blue-100 border-t-blue-600 rounded-full animate-spin"></div>
                                    </div>
                                ) : exams.length > 0 ? (
                                    <div className="grid grid-cols-1 gap-6">
                                        {exams.map(exam => (
                                            <div key={exam.id} className="group bg-white rounded-3xl border border-slate-200 shadow-sm hover:shadow-xl hover:border-blue-200 transition-all p-8 flex flex-col md:flex-row gap-8 items-center">
                                                <div className="w-16 h-16 rounded-2xl bg-blue-50 flex items-center justify-center text-blue-600 group-hover:bg-blue-600 group-hover:text-white transition-colors duration-500">
                                                    <BookOpen size={28} />
                                                </div>

                                                <div className="flex-1 text-center md:text-left">
                                                    <div className="flex flex-wrap gap-2 mb-3 justify-center md:justify-start">
                                                        <span className="px-3 py-1 bg-blue-50 text-blue-700 rounded-full text-[10px] font-bold uppercase tracking-wider border border-blue-100">
                                                            Adaptive Engine v3
                                                        </span>
                                                        <span className="px-3 py-1 bg-green-50 text-green-700 rounded-full text-[10px] font-bold uppercase tracking-wider border border-green-100">
                                                            Proctoring Active
                                                        </span>
                                                    </div>
                                                    <h3 className="text-2xl font-bold text-slate-800 tracking-tight">{exam.title}</h3>
                                                    <p className="text-slate-500 font-medium mt-1 leading-relaxed">{exam.description}</p>
                                                </div>

                                                <div className="flex flex-col gap-4 items-center md:items-end min-w-[200px]">
                                                    <div className="flex items-center gap-4 text-slate-400 font-bold text-xs uppercase tracking-widest">
                                                        <div className="flex items-center gap-1.5">
                                                            <Clock size={14} className="text-blue-500" />
                                                            {exam.durationMinutes}m
                                                        </div>
                                                        <div className="w-1 h-1 rounded-full bg-slate-300"></div>
                                                        <span>{exam.type}</span>
                                                    </div>

                                                    <button
                                                        onClick={() => onStartExam(exam)}
                                                        className="w-full bg-slate-900 hover:bg-blue-600 text-white px-8 py-4 rounded-2xl font-bold transition-all shadow-xl hover:shadow-blue-500/20 flex items-center justify-center gap-3 group/btn"
                                                    >
                                                        Start Session
                                                        <Play size={18} className="group-hover/btn:translate-x-1 transition-transform" fill="currentColor" />
                                                    </button>
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                ) : (
                                    <div className="glass rounded-[3rem] border border-slate-100 p-20 text-center flex flex-col items-center gap-4">
                                        <div className="w-20 h-20 rounded-full bg-slate-100 flex items-center justify-center text-slate-300">
                                            <Monitor size={40} />
                                        </div>
                                        <p className="text-slate-400 font-bold uppercase tracking-[0.2em]">Zero Live Cycles Detected</p>
                                        <p className="text-slate-400 text-sm max-w-xs">No assessments are currently available for your authorization level.</p>
                                    </div>
                                )}
                            </div>
                        </div>
                    )}

                    {activeTab === 'results' && (
                        <div className="glass rounded-[3rem] p-24 text-center max-w-2xl mx-auto shadow-2xl border border-white/40">
                            <div className="w-24 h-24 bg-purple-50 rounded-full flex items-center justify-center mx-auto mb-8 text-purple-600">
                                <BarChart2 size={48} className="opacity-40" />
                            </div>
                            <h2 className="text-2xl font-black text-slate-900 mb-4 tracking-tight">Analytics Processing Incoming</h2>
                            <p className="text-slate-500 font-medium leading-relaxed">Insight Board requires at least one completed session cycle to generate behavioral and performance metrics.</p>
                            <button onClick={() => setActiveTab('dashboard')} className="mt-10 text-blue-600 font-bold uppercase tracking-widest text-xs hover:text-blue-700 transition-colors">
                                ← Return to Command Center
                            </button>
                        </div>
                    )}
                </main>
            </div>
        </div>
    );
};

export default Dashboard;
