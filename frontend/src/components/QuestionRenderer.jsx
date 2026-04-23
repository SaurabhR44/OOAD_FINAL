import React, { useState, useEffect } from 'react';
import CodeBlock from './CodeBlock';
import { AlertCircle, Edit3, Keyboard } from 'lucide-react';

/**
 * QuestionRenderer displays different question types with proper formatting
 */
export default function QuestionRenderer({ question, selectedOption, textAnswer, onOptionSelect, onTextChange, submitting }) {
    if (!question) return null;

    // Parse question content to extract code blocks (marked with ```language ... ```)
    const parseContent = (content) => {
        if (!content) return [];

        const parts = [];
        const codeBlockRegex = /```(\w+)\n([\s\S]*?)```/g;
        let lastIndex = 0;
        let match;

        while ((match = codeBlockRegex.exec(content)) !== null) {
            // Add text before code block
            if (match.index > lastIndex) {
                parts.push({
                    type: 'text',
                    content: content.substring(lastIndex, match.index).trim()
                });
            }

            // Add code block
            parts.push({
                type: 'code',
                language: match[1],
                content: match[2].trim()
            });

            lastIndex = codeBlockRegex.lastIndex;
        }

        // Add remaining text
        if (lastIndex < content.length) {
            const remaining = content.substring(lastIndex).trim();
            if (remaining) {
                parts.push({
                    type: 'text',
                    content: remaining
                });
            }
        }

        return parts.length > 0 ? parts : [{ type: 'text', content }];
    };

    const contentParts = parseContent(question.content);

    return (
        <div className="space-y-8 animate-in fade-in slide-in-from-bottom-4 duration-700">
            {/* Question Header & Badge */}
            <div className="flex items-center justify-between gap-4">
                <div className="flex items-center gap-3">
                    <div className="w-10 h-10 rounded-xl bg-blue-600/10 border border-blue-500/20 flex items-center justify-center text-blue-400">
                        {question.type === 'SUBJECTIVE' ? <Edit3 size={20} /> : <Keyboard size={20} />}
                    </div>
                    <div>
                        <span className={`px-3 py-1 rounded-lg text-[10px] font-black uppercase tracking-widest border ${
                            question.type === 'MCQ' ? 'bg-blue-600/20 text-blue-400 border-blue-500/30' :
                            question.type === 'TRUE_FALSE' ? 'bg-purple-600/20 text-purple-400 border-purple-500/30' :
                            'bg-amber-600/20 text-amber-400 border-amber-500/30'
                        }`}>
                            {question.type === 'MCQ' ? 'Multiple Choice' :
                             question.type === 'TRUE_FALSE' ? 'True / False' :
                             question.type === 'FILL_IN_BLANK' ? 'Objective Response' :
                             'Detailed Subjective'}
                        </span>
                    </div>
                </div>
            </div>

            {/* Question Content - FIXED VISIBILITY (WHITE TEXT) */}
            <div className="space-y-6">
                {contentParts.map((part, idx) => (
                    <div key={idx} className="transition-all duration-500">
                        {part.type === 'text' && (
                            <h2 className="text-3xl font-extrabold text-white leading-[1.4] tracking-tight drop-shadow-sm">
                                {part.content}
                            </h2>
                        )}
                        {part.type === 'code' && (
                            <div className="my-6 rounded-2xl overflow-hidden border border-white/10 shadow-2xl">
                                <CodeBlock code={part.content} language={part.language} />
                            </div>
                        )}
                    </div>
                ))}
            </div>

            {/* Interactive Area */}
            <div className="mt-10">
                {/* Options for MCQ / TF */}
                {(question.type === 'MCQ' || question.type === 'TRUE_FALSE') && (
                    <div className="grid grid-cols-1 gap-4">
                        {question.options.map((opt, idx) => (
                            <button
                                key={opt.id}
                                onClick={() => !submitting && onOptionSelect(opt.id)}
                                disabled={submitting}
                                className={`group relative w-full text-left p-6 rounded-2xl border-2 transition-all duration-300 flex items-center gap-6 ${
                                    selectedOption === opt.id
                                        ? 'border-blue-500 bg-blue-600/10 shadow-[0_0_30px_rgba(37,99,235,0.1)]'
                                        : 'border-white/5 bg-white/5 hover:border-white/20 hover:bg-white/10'
                                }`}
                            >
                                <div className={`w-12 h-12 rounded-xl border-2 flex items-center justify-center transition-all duration-300 font-black text-sm ${
                                    selectedOption === opt.id
                                        ? 'bg-blue-600 border-blue-500 text-white rotate-3 shadow-lg shadow-blue-500/50'
                                        : 'bg-white/5 border-white/10 text-slate-500 group-hover:border-white/30 group-hover:text-white'
                                }`}>
                                    {String.fromCharCode(65 + idx)}
                                </div>
                                <span className={`text-lg font-bold transition-colors ${
                                    selectedOption === opt.id ? 'text-white' : 'text-slate-400 group-hover:text-white'
                                }`}>
                                    {opt.text}
                                </span>
                            </button>
                        ))}
                    </div>
                )}

                {/* Objective Response (Fill in the blank) */}
                {question.type === 'FILL_IN_BLANK' && (
                    <div className="space-y-4">
                        <label className="text-xs font-black text-slate-500 uppercase tracking-widest">Type your response below</label>
                        <input
                            type="text"
                            value={textAnswer || ''}
                            onChange={(e) => onTextChange(e.target.value)}
                            disabled={submitting}
                            placeholder="Enter the correct term or value..."
                            className="w-full bg-white/5 border-2 border-white/10 p-6 rounded-2xl text-xl font-bold text-white focus:outline-none focus:border-blue-500 transition-all placeholder:text-slate-700"
                        />
                    </div>
                )}

                {/* Detailed Subjective */}
                {question.type === 'SUBJECTIVE' && (
                    <div className="space-y-4">
                        <label className="text-xs font-black text-slate-500 uppercase tracking-widest">Type your detailed explanation</label>
                        <textarea
                            value={textAnswer || ''}
                            onChange={(e) => onTextChange(e.target.value)}
                            disabled={submitting}
                            placeholder="Explain your reasoning, approach, and conclusion..."
                            className="w-full h-64 bg-white/5 border-2 border-white/10 p-6 rounded-2xl text-lg font-medium text-white focus:outline-none focus:border-blue-500 transition-all resize-none font-mono placeholder:text-slate-700 leading-relaxed"
                        />
                    </div>
                )}
            </div>

            {/* Integrity Warning */}
            <div className="p-4 rounded-2xl bg-amber-500/5 border border-amber-500/20 flex gap-4 items-start">
                <AlertCircle className="text-amber-500 flex-shrink-0" size={20} />
                <div className="text-sm">
                    <p className="font-bold text-amber-500">Security Note</p>
                    <p className="text-amber-500/70 mt-1">Your response is being tracked in real-time. Avoid switching tabs or using unauthorized shortcuts while typing.</p>
                </div>
            </div>
        </div>
    );
}
