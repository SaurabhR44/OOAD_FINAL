import React, { useEffect } from 'react';
import CodeBlock from './CodeBlock';

export default function QuestionRenderer({ question, selectedOption, textAnswer, onOptionSelect, onTextChange, submitting }) {
    useEffect(() => {
        if (question) {
            console.log('📝 QuestionRenderer rendering question:', question.id, question.type);
        }
    }, [question]);

    if (!question) {
        return (
            <div className="p-8 rounded-3xl bg-amber-500/10 border border-amber-500/20 text-amber-500 flex items-center gap-4">
                <span className="text-2xl">⚠️</span>
                <span className="font-bold">Invalid question data received.</span>
            </div>
        );
    }

    const parseContent = (content) => {
        if (!content) return [{ type: 'text', content: 'No question content provided.' }];
        const parts = [];
        const codeBlockRegex = /```(\w+)\n([\s\S]*?)```/g;
        let lastIndex = 0;
        let match;
        while ((match = codeBlockRegex.exec(content)) !== null) {
            if (match.index > lastIndex) {
                parts.push({ type: 'text', content: content.substring(lastIndex, match.index).trim() });
            }
            parts.push({ type: 'code', language: match[1], content: match[2].trim() });
            lastIndex = codeBlockRegex.lastIndex;
        }
        if (lastIndex < content.length) {
            const remaining = content.substring(lastIndex).trim();
            if (remaining) parts.push({ type: 'text', content: remaining });
        }
        return parts.length > 0 ? parts : [{ type: 'text', content }];
    };

    const contentParts = parseContent(question.content);

    return (
        <div className="space-y-10 animate-in fade-in duration-500 p-8 rounded-[2rem] bg-white/[0.02] border border-white/5 shadow-2xl">
            {/* Badge */}
            <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-xl bg-blue-500/20 border border-blue-500/30 flex items-center justify-center text-xl">
                    {question.type === 'SUBJECTIVE' ? '📝' : '⌨️'}
                </div>
                <span className="px-3 py-1 bg-blue-500/10 border border-blue-500/20 rounded-lg text-[10px] font-black uppercase tracking-widest text-blue-400">
                    {question.type} - {question.difficulty}
                </span>
            </div>

            {/* Content */}
            <div className="space-y-6">
                {contentParts.map((part, idx) => (
                    <div key={idx}>
                        {part.type === 'text' && (
                            <h2 className="text-3xl font-extrabold text-white leading-relaxed tracking-tight">
                                {part.content}
                            </h2>
                        )}
                        {part.type === 'code' && (
                            <div className="my-8 rounded-2xl overflow-hidden border border-white/10 shadow-2xl">
                                <CodeBlock code={part.content} language={part.language} />
                            </div>
                        )}
                    </div>
                ))}
            </div>

            {/* Interaction */}
            <div className="pt-8">
                {(question.type === 'MCQ' || question.type === 'TRUE_FALSE') && (
                    <div className="grid grid-cols-1 gap-4">
                        {question.options?.map((opt, idx) => (
                            <button
                                key={opt.id}
                                onClick={() => !submitting && onOptionSelect(opt.id)}
                                disabled={submitting}
                                className={`group relative w-full text-left p-6 rounded-2xl border-2 transition-all duration-300 flex items-center gap-6 ${
                                    selectedOption === opt.id
                                        ? 'border-blue-500 bg-blue-600/20'
                                        : 'border-white/5 bg-white/5 hover:border-white/20 hover:bg-white/10'
                                }`}
                            >
                                <div className={`w-12 h-12 rounded-xl border-2 flex items-center justify-center transition-all duration-300 font-black text-sm ${
                                    selectedOption === opt.id ? 'bg-blue-600 border-blue-500 text-white' : 'bg-white/5 border-white/10 text-slate-500 group-hover:text-white'
                                }`}>
                                    {String.fromCharCode(65 + idx)}
                                </div>
                                <span className={`text-xl font-bold transition-colors ${selectedOption === opt.id ? 'text-white' : 'text-slate-400 group-hover:text-white'}`}>
                                    {opt.text}
                                </span>
                            </button>
                        ))}
                    </div>
                )}

                {question.type === 'FILL_IN_BLANK' && (
                    <div className="space-y-4">
                        <label className="text-[10px] font-black text-slate-500 uppercase tracking-widest ml-1">Type your exact answer</label>
                        <input
                            type="text"
                            value={textAnswer || ''}
                            onChange={(e) => onTextChange(e.target.value)}
                            disabled={submitting}
                            placeholder="Type here..."
                            className="w-full bg-white/5 border-2 border-white/10 p-6 rounded-2xl text-2xl font-black text-white focus:outline-none focus:border-blue-500 transition-all"
                        />
                    </div>
                )}

                {question.type === 'SUBJECTIVE' && (
                    <div className="space-y-4">
                        <label className="text-[10px] font-black text-slate-500 uppercase tracking-widest ml-1">Provide detailed explanation</label>
                        <textarea
                            value={textAnswer || ''}
                            onChange={(e) => onTextChange(e.target.value)}
                            disabled={submitting}
                            placeholder="Type your explanation here..."
                            className="w-full h-80 bg-white/5 border-2 border-white/10 p-6 rounded-2xl text-lg font-medium text-white focus:outline-none focus:border-blue-500 transition-all resize-none font-mono"
                        />
                    </div>
                )}
            </div>

            <div className="p-4 rounded-2xl bg-amber-500/5 border border-amber-500/10 flex gap-4 items-center">
                <span className="text-amber-500/50 text-xs">🔒</span>
                <p className="text-[10px] font-bold text-amber-500/50 uppercase tracking-wider">AI Integrity Monitoring Active</p>
            </div>
        </div>
    );
}
