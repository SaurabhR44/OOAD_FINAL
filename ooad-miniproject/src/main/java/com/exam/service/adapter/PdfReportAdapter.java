package com.exam.service.adapter;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

// [PATTERN: Adapter] — Adapts raw data into PDF format
@Component
public class PdfReportAdapter implements ReportExporter {

    @Override
    public byte[] export(List<Map<String, Object>> data) {
        // [PRINCIPLE: Abstraction] — Simplified implementation for demonstration
        StringBuilder sb = new StringBuilder();
        sb.append("PROCTOR IQ - EXAM REPORT\n");
        sb.append("========================\n\n");
        
        for (Map<String, Object> entry : data) {
            sb.append(entry.toString()).append("\n");
        }
        
        return sb.toString().getBytes();
    }
}
