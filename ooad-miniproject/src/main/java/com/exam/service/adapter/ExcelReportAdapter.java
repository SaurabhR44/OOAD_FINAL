package com.exam.service.adapter;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

// [PATTERN: Adapter] — Adapts raw data into Excel format using Apache POI
@Component
public class ExcelReportAdapter implements ReportExporter {

    @Override
    public byte[] export(List<Map<String, Object>> data) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Report");
            
            if (data.isEmpty()) return new byte[0];

            // Create header
            Row headerRow = sheet.createRow(0);
            int col = 0;
            for (String key : data.get(0).keySet()) {
                headerRow.createCell(col++).setCellValue(key);
            }

            // Create rows
            int rowIdx = 1;
            for (Map<String, Object> entry : data) {
                Row row = sheet.createRow(rowIdx++);
                int c = 0;
                for (Object value : entry.values()) {
                    row.createCell(c++).setCellValue(value != null ? value.toString() : "");
                }
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Excel", e);
        }
    }
}
