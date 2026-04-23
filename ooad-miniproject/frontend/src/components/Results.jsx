import React, { useState, useEffect } from 'react';
import { BarChart, Bar, LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts';
import { Award, TrendingUp, Clock, AlertCircle, Download } from 'lucide-react';

const Results = ({ sessionId, exam, token, onReturn }) => {
    const [results, setResults] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchResults = async () => {
            try {
                const response = await fetch(
                    `http://localhost:8080/api/exam/results?sessionId=${sessionId}`,
                    { headers: { 'Authorization': `Bearer ${token}` } }
                );
                if (!response.ok) throw new Error('Failed to fetch results');
                const data = await response.json();
                setResults(data);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };
        fetchResults();
    }, [sessionId, token]);

    if (loading) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-50 flex items-center justify-center">
                <div className="text-center">
                    <div className="w-16 h-16 border-4 border-blue-200 border-t-blue-600 rounded-full animate-spin mx-auto mb-4"></div>
                    <p className="text-slate-600 font-medium">Evaluating your performance...</p>
                </div>
            </div>
        );
    }

    if (error || !results) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-50 p-6">
                <div className="max-w-4xl mx-auto">
                    <div className="bg-red-50 border-2 border-red-200 rounded-2xl p-8 text-center">
                        <AlertCircle className="w-12 h-12 text-red-600 mx-auto mb-4" />
                        <h2 className="text-2xl font-bold text-red-800 mb-2">Unable to Load Results</h2>
                        <p className="text-red-700 mb-6">{error || 'Results not available at this time'}</p>
                        <button
                            onClick={onReturn}
                            className="px-6 py-3 bg-slate-600 hover:bg-slate-700 text-white font-bold rounded-xl transition-colors"
                        >
                            Return to Dashboard
                        </button>
                    </div>
                </div>
            </div>
        );
    }

    const isPassed = results.score >= exam.passingScore;
    const accuracy = Math.round((results.correct / results.totalQuestions) * 100);
    const timeUsedPercent = Math.round((results.timeSpentSeconds / (exam.durationMinutes * 60)) * 100);

    // Prepare chart data
    const difficultyData = results.performanceByDifficulty && Object.entries(results.performanceByDifficulty).map(([level, stats]) => ({
        name: level,
        Correct: stats.correct,
        Incorrect: stats.total - stats.correct
    }));

    const topicData = results.performanceByTopic && Object.entries(results.performanceByTopic).map(([topic, stats]) => ({
        name: topic,
        value: Math.round((stats.correct / stats.total) * 100)
    }));

    const COLORS = ['#10b981', '#3b82f6', '#f59e0b', '#ef4444'];

    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-50 p-6">
            <div className="max-w-7xl mx-auto">
                {/* Header */}
                <div className="mb-8">
                    <h1 className="text-4xl font-black text-slate-900 mb-2">Exam Results</h1>
                    <p className="text-slate-600">{exam.title} - {new Date(results.submittedAt).toLocaleDateString()}</p>
                </div>

                {/* Result Card */}
                <div className={`rounded-3xl p-8 mb-8 border-2 shadow-xl ${
                    isPassed 
                        ? 'bg-gradient-to-br from-green-50 to-emerald-50 border-green-200' 
                        : 'bg-gradient-to-br from-amber-50 to-orange-50 border-amber-200'
                }`}>
                    <div className="flex items-center gap-6 mb-6">
                        <div className={`w-24 h-24 rounded-full flex items-center justify-center text-4xl font-black ${
                            isPassed 
                                ? 'bg-green-200 text-green-700' 
                                : 'bg-amber-200 text-amber-700'
                        }`}>
                            {results.score}
                        </div>
                        <div>
                            <h2 className={`text-3xl font-black mb-2 ${
                                isPassed ? 'text-green-700' : 'text-amber-700'
                            }`}>
                                {isPassed ? '✅ PASSED' : '⚠️ NEEDS IMPROVEMENT'}
                            </h2>
                            <p className={`text-sm font-bold uppercase tracking-widest ${
                                isPassed ? 'text-green-600' : 'text-amber-600'
                            }`}>
                                Score: {results.score} / 100 | Passing: {exam.passingScore}%
                            </p>
                        </div>
                    </div>

                    {results.competency && (
                        <div className="bg-white rounded-xl p-4 border border-slate-100">
                            <p className="text-xs uppercase font-bold text-slate-500 tracking-widest mb-1">Your Level</p>
                            <p className="text-lg font-bold text-slate-900">{results.competency}</p>
                        </div>
                    )}
                </div>

                {/* Key Metrics */}
                <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8">
                    <div className="bg-white rounded-2xl p-6 border border-slate-200 shadow-sm">
                        <div className="flex items-center gap-3 mb-3">
                            <Award className="text-blue-600" size={20} />
                            <span className="text-xs uppercase font-bold text-slate-500 tracking-widest">Accuracy</span>
                        </div>
                        <p className="text-3xl font-black text-slate-900">{accuracy}%</p>
                        <p className="text-sm text-slate-600 mt-1">{results.correct} / {results.totalQuestions} correct</p>
                    </div>

                    <div className="bg-white rounded-2xl p-6 border border-slate-200 shadow-sm">
                        <div className="flex items-center gap-3 mb-3">
                            <Clock className="text-green-600" size={20} />
                            <span className="text-xs uppercase font-bold text-slate-500 tracking-widest">Time Used</span>
                        </div>
                        <p className="text-3xl font-black text-slate-900">{timeUsedPercent}%</p>
                        <p className="text-sm text-slate-600 mt-1">{Math.floor(results.timeSpentSeconds / 60)} / {exam.durationMinutes} min</p>
                    </div>

                    <div className="bg-white rounded-2xl p-6 border border-slate-200 shadow-sm">
                        <div className="flex items-center gap-3 mb-3">
                            <TrendingUp className="text-purple-600" size={20} />
                            <span className="text-xs uppercase font-bold text-slate-500 tracking-widest">Difficulty</span>
                        </div>
                        <p className="text-3xl font-black text-slate-900">{results.finalDifficulty || 'N/A'}</p>
                        <p className="text-sm text-slate-600 mt-1">Adaptive engine reached this level</p>
                    </div>

                    <div className="bg-white rounded-2xl p-6 border border-slate-200 shadow-sm">
                        <div className="flex items-center gap-3 mb-3">
                            <AlertCircle className="text-orange-600" size={20} />
                            <span className="text-xs uppercase font-bold text-slate-500 tracking-widest">Violations</span>
                        </div>
                        <p className="text-3xl font-black text-slate-900">{results.violationCount || 0}</p>
                        <p className="text-sm text-slate-600 mt-1">Proctoring incidents</p>
                    </div>
                </div>

                {/* Charts */}
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
                    {/* Performance by Difficulty */}
                    {difficultyData && (
                        <div className="bg-white rounded-2xl p-6 border border-slate-200 shadow-sm">
                            <h3 className="text-lg font-bold text-slate-900 mb-4">Performance by Difficulty</h3>
                            <ResponsiveContainer width="100%" height={300}>
                                <BarChart data={difficultyData}>
                                    <CartesianGrid strokeDasharray="3 3" stroke="#e2e8f0" />
                                    <XAxis dataKey="name" />
                                    <YAxis />
                                    <Tooltip />
                                    <Legend />
                                    <Bar dataKey="Correct" fill="#10b981" />
                                    <Bar dataKey="Incorrect" fill="#ef4444" />
                                </BarChart>
                            </ResponsiveContainer>
                        </div>
                    )}

                    {/* Performance by Topic */}
                    {topicData && (
                        <div className="bg-white rounded-2xl p-6 border border-slate-200 shadow-sm">
                            <h3 className="text-lg font-bold text-slate-900 mb-4">Topic-wise Accuracy</h3>
                            <ResponsiveContainer width="100%" height={300}>
                                <PieChart>
                                    <Pie
                                        data={topicData}
                                        cx="50%"
                                        cy="50%"
                                        labelLine={false}
                                        label={({ name, value }) => `${name}: ${value}%`}
                                        outerRadius={100}
                                        fill="#8884d8"
                                        dataKey="value"
                                    >
                                        {topicData.map((entry, index) => (
                                            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                        ))}
                                    </Pie>
                                </PieChart>
                            </ResponsiveContainer>
                        </div>
                    )}
                </div>

                {/* Feedback */}
                <div className="bg-white rounded-2xl p-8 border border-slate-200 shadow-sm mb-8">
                    <h3 className="text-xl font-bold text-slate-900 mb-4">📋 Performance Feedback</h3>
                    <div className="space-y-3">
                        {accuracy >= 80 && (
                            <p className="text-green-700 bg-green-50 border border-green-200 p-4 rounded-lg">
                                ✅ Excellent performance! You demonstrated strong mastery of the subject matter.
                            </p>
                        )}
                        {accuracy >= 60 && accuracy < 80 && (
                            <p className="text-blue-700 bg-blue-50 border border-blue-200 p-4 rounded-lg">
                                ℹ️ Good effort! Review the concepts where you struggled for improvement.
                            </p>
                        )}
                        {accuracy < 60 && (
                            <p className="text-amber-700 bg-amber-50 border border-amber-200 p-4 rounded-lg">
                                ⚠️ Continue studying the material. Practice more questions and revisit weak areas.
                            </p>
                        )}
                        {results.violationCount > 5 && (
                            <p className="text-orange-700 bg-orange-50 border border-orange-200 p-4 rounded-lg">
                                ⚠️ Multiple proctoring violations detected. Future exam attempts may be subject to review.
                            </p>
                        )}
                    </div>
                </div>

                {/* Actions */}
                <div className="flex gap-4">
                    <button
                        onClick={onReturn}
                        className="flex-1 px-6 py-4 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-xl transition-colors flex items-center justify-center gap-2"
                    >
                        ← Return to Dashboard
                    </button>
                    <button
                        onClick={() => window.print()}
                        className="px-6 py-4 bg-slate-600 hover:bg-slate-700 text-white font-bold rounded-xl transition-colors flex items-center justify-center gap-2"
                    >
                        <Download size={18} />
                        Download Report
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Results;
