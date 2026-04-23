import React from 'react';
import { AlertCircle, AlertTriangle, ShieldAlert } from 'lucide-react';

// [PRINCIPLE: Polymorphism] — Component renders differently based on severity type
const ViolationBadge = ({ violation }) => {
    const config = {
        LOW: {
            color: 'bg-blue-500/10 text-blue-400 border-blue-500/20',
            icon: AlertCircle,
            label: 'Advisory'
        },
        MEDIUM: {
            color: 'bg-amber-500/10 text-amber-400 border-amber-500/20',
            icon: AlertTriangle,
            label: 'Warning'
        },
        HIGH: {
            color: 'bg-red-500/10 text-red-400 border-red-500/20',
            icon: ShieldAlert,
            label: 'CRITICAL'
        }
    };

    const style = config[violation.severity] || config.MEDIUM;
    const Icon = style.icon;

    return (
        <div className={`px-3 py-1.5 rounded-lg border flex items-center gap-2 ${style.color}`}>
            <Icon size={14} />
            <span className="text-[10px] font-black uppercase tracking-widest">{style.label}: {violation.type}</span>
        </div>
    );
};

export default ViolationBadge;
