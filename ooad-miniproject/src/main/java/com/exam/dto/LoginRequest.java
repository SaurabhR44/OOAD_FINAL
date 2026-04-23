package com.exam.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private String srn; // For SRN-based login
}
