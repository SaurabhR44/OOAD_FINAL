import React from 'react';
import { Copy, Check } from 'lucide-react';

export default function CodeBlock({ code, language = 'java' }) {
    const [copied, setCopied] = React.useState(false);

    const handleCopy = () => {
        navigator.clipboard.writeText(code);
        setCopied(true);
        setTimeout(() => setCopied(false), 2000);
    };

    return (
        <div className="my-6 rounded-2xl overflow-hidden border border-slate-200 bg-slate-900">
            <div className="flex items-center justify-between px-6 py-3 bg-slate-800 border-b border-slate-700">
                <span className="text-xs font-bold text-slate-300 uppercase tracking-widest">{language}</span>
                <button
                    onClick={handleCopy}
                    className="flex items-center gap-2 px-3 py-1 bg-slate-700 hover:bg-slate-600 text-slate-300 text-xs font-bold rounded transition-colors"
                >
                    {copied ? (
                        <>
                            <Check size={14} />
                            Copied
                        </>
                    ) : (
                        <>
                            <Copy size={14} />
                            Copy
                        </>
                    )}
                </button>
            </div>
            <pre className="p-6 overflow-x-auto text-slate-100 text-sm leading-relaxed font-mono">
                <code>{code}</code>
            </pre>
        </div>
    );
}
