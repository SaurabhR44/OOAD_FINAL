package com.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Violation {
    private String type;
    private String description;
    private LocalDateTime timestamp;
}
