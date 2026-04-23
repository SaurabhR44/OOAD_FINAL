import React, { useState, useCallback } from 'react';
import { User, Lock, ChevronRight, AlertCircle } from 'lucide-react';
import { authService } from '../services/apiService';

// [PATTERN: MVC Architecture] — View component for user authentication
const Login = ({ onLogin }) => {
    const [formData, setFormData] = useState({ identifier: '', password: '' });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    // [GRASP: Controller] — Handles user input and delegates to AuthService
    const handleSubmit = useCallback(async (e) => {
        e.preventDefault();
        if (loading) return; // Prevent multiple clicks (Fix for 15-click issue)

        setLoading(true);
        setError(null);

        try {
            const isSRN = formData.identifier.trim().toUpperCase().startsWith('PES');
            const payload = isSRN
                ? { srn: formData.identifier.trim(), password: formData.password }
                : { username: formData.identifier.trim(), password: formData.password };

            const res = await authService.login(payload);
            
            // [PRINCIPLE: Abstraction] — Component doesn't care how login happens
            if (!res || !res.token) {
                throw new Error('Authentication failed');
            }

            onLogin(res);
        } catch (err) {
            setError(err.message || 'Connection failed. Please try again.');
        } finally {
            setLoading(false);
        }
    }, [formData, loading, onLogin]);

    return (
        <div className="min-h-screen flex items-center justify-center bg-slate-950 overflow-hidden relative font-sans">
            {/* Rich Aesthetics: Animated Background elements */}
            <div className="absolute top-0 -left-10 w-96 h-96 bg-blue-500/20 rounded-full blur-[120px] animate-pulse"></div>
            <div className="absolute bottom-0 -right-10 w-96 h-96 bg-purple-500/20 rounded-full blur-[120px] animate-pulse delay-700"></div>

            <div className="max-w-md w-full bg-slate-900/40 backdrop-blur-xl rounded-[2rem] shadow-2xl overflow-hidden border border-white/10 relative z-10">
                <div className="px-10 py-12">
                    <div className="text-center mb-10">
                        <div className="inline-flex items-center justify-center w-20 h-20 rounded-3xl bg-gradient-to-tr from-blue-600 to-indigo-600 shadow-2xl shadow-blue-500/40 mb-6 -rotate-6">
                            <Lock size={36} className="text-white" />
                        </div>
                        <h1 className="text-4xl font-black text-white tracking-tight mb-2">Proctor<span className="text-blue-500">IQ</span></h1>
                        <p className="text-slate-400 font-medium tracking-wide">SECURE EXAMINATION GATEWAY</p>
                    </div>

                    <form onSubmit={handleSubmit} className="space-y-6">
                        <div className="space-y-2">
                            <label className="text-[10px] font-black text-slate-500 uppercase tracking-[0.2em] ml-1">Authentication ID</label>
                            <div className="relative group">
                                <User className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-500 group-focus-within:text-blue-500 transition-colors" size={18} />
                                <input
                                    type="text"
                                    required
                                    placeholder="SRN or Username"
                                    className="w-full pl-12 pr-4 py-4 bg-slate-800/50 border border-white/5 rounded-2xl focus:ring-4 focus:ring-blue-500/20 focus:border-blue-500 outline-none transition-all text-white placeholder:text-slate-600 font-medium"
                                    value={formData.identifier}
                                    onChange={e => setFormData({ ...formData, identifier: e.target.value })}
                                />
                            </div>
                        </div>

                        <div className="space-y-2">
                            <label className="text-[10px] font-black text-slate-500 uppercase tracking-[0.2em] ml-1">Access Token</label>
                            <div className="relative group">
                                <Lock className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-500 group-focus-within:text-blue-500 transition-colors" size={18} />
                                <input
                                    type="password"
                                    required
                                    placeholder="••••••••"
                                    className="w-full pl-12 pr-4 py-4 bg-slate-800/50 border border-white/5 rounded-2xl focus:ring-4 focus:ring-blue-500/20 focus:border-blue-500 outline-none transition-all text-white placeholder:text-slate-600 font-medium"
                                    value={formData.password}
                                    onChange={e => setFormData({ ...formData, password: e.target.value })}
                                />
                            </div>
                        </div>

                        {error && (
                            <div className="p-4 bg-red-500/10 text-red-400 text-xs rounded-2xl flex items-center gap-3 border border-red-500/20 animate-in fade-in slide-in-from-top-2">
                                <AlertCircle size={16} />
                                <span className="font-bold">{error}</span>
                            </div>
                        )}

                        <button
                            type="submit"
                            disabled={loading}
                            className="w-full bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-500 hover:to-indigo-500 text-white font-black py-4 rounded-2xl transition-all flex items-center justify-center gap-3 disabled:opacity-50 disabled:cursor-not-allowed shadow-2xl shadow-blue-500/20 group"
                        >
                            {loading ? (
                                <span className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                            ) : (
                                <>
                                    <span>AUTHORIZE ACCESS</span>
                                    <ChevronRight size={18} className="group-hover:translate-x-1 transition-transform" />
                                </>
                            )}
                        </button>
                    </form>
                </div>

                <div className="px-10 py-5 bg-white/5 border-t border-white/5 flex justify-between items-center text-[9px] uppercase font-black tracking-[0.3em] text-slate-500">
                    <div className="flex items-center gap-2">
                        <div className={`w-2 h-2 rounded-full ${loading ? 'bg-amber-500 animate-pulse' : 'bg-emerald-500'}`}></div>
                        {loading ? 'Processing...' : 'Ready'}
                    </div>
                    <span>© 2024 PROCTORIQ</span>
                </div>
            </div>
        </div>
    );
};

export default Login;
