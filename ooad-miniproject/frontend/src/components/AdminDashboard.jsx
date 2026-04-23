import React, { useState, useEffect } from 'react';
import { Users, BookOpen, AlertCircle, BarChart2, Activity, LogOut, Search, Monitor, Database, Download, FileText } from 'lucide-react';
import { adminService } from '../services/apiService';

// [PATTERN: MVC Architecture] — View component for administrative functions
const AdminNavItem = ({ icon: Icon, label, active, onClick }) => (
    <button
        onClick={onClick}
        className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-black transition-all ${active
            ? 'bg-blue-600 text-white shadow-lg shadow-blue-500/20'
            : 'text-slate-500 hover:bg-white/5 hover:text-white'
            }`}
    >
        <Icon size={18} />
        <span className="tracking-widest uppercase text-[10px]">{label}</span>
    </button>
);

const AdminDashboard = ({ onLogout }) => {
    const [activeTab, setActiveTab] = useState('overview');
    const [stats, setStats] = useState(null);
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadData = async () => {
            try {
                const [statsData, resultsData] = await Promise.all([
                    adminService.getDashboardStats(),
                    adminService.getResults()
                ]);
                setStats(statsData);
                setResults(resultsData);
            } catch (err) {
                console.error('Failed to load admin data:', err);
            } finally {
                setLoading(false);
            }
        };
        loadData();
    }, []);

    // [PATTERN: Adapter] — Triggers export via backend adapters
    const handleExport = async (type) => {
        try {
            const response = type === 'excel' ? await adminService.exportExcel() : await adminService.exportPdf();
            const url = window.URL.createObjectURL(new Blob([response]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `ProctorIQ_Report_${new Date().getTime()}.${type === 'excel' ? 'xlsx' : 'pdf'}`);
            document.body.appendChild(link);
            link.click();
            link.remove();
        } catch (err) {
            alert('Export failed: ' + err.message);
        }
    };

    return (
        <div className="min-h-screen bg-slate-950 flex text-slate-200">
            {/* Sidebar */}
            <aside className="w-80 border-r border-white/5 bg-slate-900/50 flex flex-col p-8">
                <div className="flex items-center gap-3 mb-12">
                    <div className="w-10 h-10 bg-blue-600 rounded-xl flex items-center justify-center shadow-lg shadow-blue-600/20">
                        <Monitor size={20} className="text-white" />
                    </div>
                    <h2 className="text-xl font-black tracking-tight">Proctor<span className="text-blue-500">IQ</span></h2>
                </div>

                <nav className="flex-1 space-y-2">
                    <AdminNavItem icon={Activity} label="Command Center" active={activeTab === 'overview'} onClick={() => setActiveTab('overview')} />
                    <AdminNavItem icon={BarChart2} label="Analytics" active={activeTab === 'results'} onClick={() => setActiveTab('results')} />
                </nav>

                <button onClick={onLogout} className="mt-auto flex items-center gap-3 px-4 py-3 rounded-xl text-slate-500 hover:text-red-400 transition-colors">
                    <LogOut size={18} />
                    <span className="text-[10px] font-black uppercase tracking-widest">Terminate Session</span>
                </button>
            </aside>

            {/* Main */}
            <main className="flex-1 p-12 overflow-y-auto">
                <header className="flex justify-between items-end mb-12">
                    <div>
                        <p className="text-[10px] font-black text-blue-500 uppercase tracking-[0.2em] mb-2">Systems Status</p>
                        <h1 className="text-4xl font-bold text-white tracking-tight">
                            {activeTab === 'overview' ? 'Live Operations' : 'Assessment Analytics'}
                        </h1>
                    </div>

                    <div className="flex gap-4">
                        <button 
                            onClick={() => handleExport('excel')}
                            className="flex items-center gap-2 bg-white/5 hover:bg-white/10 border border-white/10 px-6 py-3 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all"
                        >
                            <Download size={14} />
                            Export XLSX
                        </button>
                        <button 
                            onClick={() => handleExport('pdf')}
                            className="flex items-center gap-2 bg-white/5 hover:bg-white/10 border border-white/10 px-6 py-3 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all"
                        >
                            <FileText size={14} />
                            Export PDF
                        </button>
                    </div>
                </header>

                {activeTab === 'overview' && stats && (
                    <div className="grid grid-cols-3 gap-8 mb-12">
                        <div className="p-8 bg-white/5 border border-white/10 rounded-3xl">
                            <p className="text-slate-500 text-[10px] font-black uppercase tracking-widest mb-4">Total Students</p>
                            <p className="text-5xl font-bold text-white">{stats.totalStudents}</p>
                        </div>
                        <div className="p-8 bg-blue-600/10 border border-blue-500/20 rounded-3xl">
                            <p className="text-blue-400 text-[10px] font-black uppercase tracking-widest mb-4">Active Exams</p>
                            <p className="text-5xl font-bold text-blue-500">{stats.activeSessions}</p>
                        </div>
                        <div className="p-8 bg-red-500/10 border border-red-500/20 rounded-3xl">
                            <p className="text-red-400 text-[10px] font-black uppercase tracking-widest mb-4">Anomalies</p>
                            <p className="text-5xl font-bold text-red-500">{stats.unreviewedViolations}</p>
                        </div>
                    </div>
                )}

                {activeTab === 'results' && (
                    <div className="bg-white/5 border border-white/10 rounded-[2rem] overflow-hidden">
                        <table className="w-full text-left border-collapse">
                            <thead>
                                <tr className="border-b border-white/5 text-[10px] font-black uppercase tracking-widest text-slate-500">
                                    <th className="px-8 py-6">Identity (SRN)</th>
                                    <th className="px-8 py-6">Competency Area</th>
                                    <th className="px-8 py-6">Performance Metric</th>
                                    <th className="px-8 py-6">Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                {results.map(r => (
                                    <tr key={r.id} className="border-b border-white/5 hover:bg-white/[0.02] transition-colors">
                                        <td className="px-8 py-6 font-bold text-white">{r.examSession?.student?.srn}</td>
                                        <td className="px-8 py-6 text-slate-400">{r.examSession?.exam?.topic}</td>
                                        <td className="px-8 py-6">
                                            <span className="text-blue-400 font-black font-mono">{r.score}</span>
                                        </td>
                                        <td className="px-8 py-6">
                                            <span className="px-3 py-1 bg-emerald-500/10 text-emerald-500 text-[10px] font-black uppercase tracking-widest rounded-lg border border-emerald-500/20">VALIDATED</span>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </main>
        </div>
    );
};

export default AdminDashboard;
