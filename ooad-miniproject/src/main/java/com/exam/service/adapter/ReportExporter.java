package com.exam.service.adapter;

import java.util.List;
import java.util.Map;

// [PATTERN: Adapter] — Target interface for exporting reports
public interface ReportExporter {
    byte[] export(List<Map<String, Object>> data);
}
