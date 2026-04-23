package com.exam.service;

import com.exam.model.Student;
import com.exam.model.User;
import com.exam.repository.StudentRepository;
import com.exam.repository.UserRepository;
import com.exam.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public Optional<String> authenticateUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword()) && user.getIsActive()) {
                String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
                return Optional.of(token);
            }
        }
        return Optional.empty();
    }

    public Optional<String> authenticateBySRN(String srn, String password) {
        Optional<Student> studentOpt = studentRepository.findBySrn(srn);

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (passwordEncoder.matches(password, student.getPassword()) && student.getIsActive()) {
                String token = jwtUtil.generateToken(student.getUsername(), student.getRole().name());
                return Optional.of(token);
            }
        }
        return Optional.empty();
    }

    public User registerStudent(Student student) {
        // Validate SRN format
        if (!isValidSRN(student.getSrn())) {
            throw new IllegalArgumentException("Invalid SRN format");
        }

        // Parse SRN to extract campus, year, branch
        student.parseSRN();

        // Hash password
        student.setPassword(passwordEncoder.encode(student.getPassword()));

        return studentRepository.save(student);
    }

    public boolean isValidSRN(String srn) {
        return srn != null && srn.matches("PES[1-2]UG(23|24|25)(AM|CS|EC)\\d{3}");
    }
}
