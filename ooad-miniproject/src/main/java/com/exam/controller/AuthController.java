package com.exam.controller;

import com.exam.dto.LoginRequest;
import com.exam.dto.LoginResponse;
import com.exam.model.Student;
import com.exam.model.User;
import com.exam.repository.StudentRepository;
import com.exam.repository.UserRepository;
import com.exam.service.AuthService;
import com.exam.service.AuditLogService;
import com.exam.model.AuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AuditLogService auditLogService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<String> tokenOpt;

        // Try SRN-based login first if SRN is provided
        if (request.getSrn() != null && !request.getSrn().isEmpty()) {
            tokenOpt = authService.authenticateBySRN(request.getSrn(), request.getPassword());
        } else {
            tokenOpt = authService.authenticateUser(request.getUsername(), request.getPassword());
        }

        if (tokenOpt.isPresent()) {
            User user = null;

            // Retrieve user based on authentication method
            if (request.getSrn() != null && !request.getSrn().isEmpty()) {
                user = studentRepository.findBySrn(request.getSrn()).orElse(null);
            } else {
                user = userRepository.findByUsername(request.getUsername()).orElse(null);
            }

            if (user != null) {
                auditLogService.log(user, AuditLog.ActionType.LOGIN, "User logged in");

                return ResponseEntity.ok(new LoginResponse(
                        user.getId(),
                        tokenOpt.get(),
                        user.getUsername(),
                        user.getRole().name(),
                        "Login successful"));
            }
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Student student) {
        try {
            User registered = authService.registerStudent(student);
            return ResponseEntity.ok("Student registered successfully: " + registered.getUsername());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok("Token valid");
        }
        return ResponseEntity.status(401).body("Invalid token");
    }
}
