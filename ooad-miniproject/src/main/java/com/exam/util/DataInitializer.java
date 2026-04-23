package com.exam.util;

import com.exam.model.*;
import com.exam.repository.*;
import com.exam.service.factory.QuestionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Arrays;

// @Component - DISABLED: Use EnhancedDataInitializer instead
//@Component
public class DataInitializer implements CommandLineRunner {

        @Autowired
        private QuestionRepository questionRepository;

        @Autowired
        private StudentRepository studentRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private ExamRepository examRepository;

        @Autowired
        private AdminRepository adminRepository;

        @Autowired
        private ExamBlueprintRepository examBlueprintRepository;

        @Override
        public void run(String... args) throws Exception {
                // H2 Mem database is fresh on every restart, so we don't need manual delete.
                // Manual deletion was causing FK constraint violations on some runs.

                // Create test students for the requested SRN ranges
                seedStudentsInRange("PES1UG23AM", 100);
                seedStudentsInRange("PES2UG23AM", 100);
                seedStudentsInRange("PES1UG23CS", 100);
                seedStudentsInRange("PES2UG23CS", 100);

                // ========== COMPUTER NETWORKS (CN) ==========
                String CN = "COMPUTER_NETWORKS";
                // EASY
                for (int i = 0; i < 10; i++) {
                        questionRepository.save(QuestionFactory.createQuestion(
                                        "CN Easy Question #" + (i + 1) + ": What is the primary purpose of OSI layer "
                                                        + (i + 1) + "?",
                                        DifficultyLevel.EASY,
                                        Arrays.asList("Option A", "Option B", "Option C", "Option D"), 0, CN));
                }
                // MEDIUM
                for (int i = 0; i < 10; i++) {
                        questionRepository.save(QuestionFactory.createQuestion(
                                        "CN Medium Question #" + (i + 1) + ": Explain the protocol " + (i + 1)
                                                        + " in the stack.",
                                        DifficultyLevel.MEDIUM,
                                        Arrays.asList("Option A", "Option B", "Option C", "Option D"), 1, CN));
                }
                // HARD
                for (int i = 0; i < 10; i++) {
                        questionRepository.save(QuestionFactory.createQuestion(
                                        "CN Hard Question #" + (i + 1) + ": Discuss complex routing scenario "
                                                        + (i + 1),
                                        DifficultyLevel.HARD,
                                        Arrays.asList("Option A", "Option B", "Option C", "Option D"), 2, CN));
                }

                // ========== SOFTWARE ENGINEERING (SE) ==========
                String SE = "SOFTWARE_ENGINEERING";
                // EASY
                for (int i = 0; i < 10; i++) {
                        questionRepository.save(QuestionFactory.createQuestion(
                                        "SE Easy Question #" + (i + 1) + ": What is SDLC phase " + (i + 1) + "?",
                                        DifficultyLevel.EASY,
                                        Arrays.asList("Option A", "Option B", "Option C", "Option D"), 0, SE));
                }
                // MEDIUM
                for (int i = 0; i < 10; i++) {
                        questionRepository.save(QuestionFactory.createQuestion(
                                        "SE Medium Question #" + (i + 1) + ": Explain design pattern " + (i + 1),
                                        DifficultyLevel.MEDIUM,
                                        Arrays.asList("Option A", "Option B", "Option C", "Option D"), 1, SE));
                }
                // HARD
                for (int i = 0; i < 10; i++) {
                        questionRepository.save(QuestionFactory.createQuestion(
                                        "SE Hard Question #" + (i + 1) + ": Analyze architectural trade-off " + (i + 1),
                                        DifficultyLevel.HARD,
                                        Arrays.asList("Option A", "Option B", "Option C", "Option D"), 2, SE));
                }

                // ========== DATA STRUCTURES (DS) ==========
                String DS = "DATA_STRUCTURES";
                // EASY
                for (int i = 0; i < 10; i++) {
                        questionRepository.save(QuestionFactory.createQuestion(
                                        "DS Easy Question #" + (i + 1) + ": Time complexity of operation " + (i + 1),
                                        DifficultyLevel.EASY, Arrays.asList("O(1)", "O(n)", "O(log n)", "O(n^2)"), 0,
                                        DS));
                }
                // MEDIUM
                for (int i = 0; i < 10; i++) {
                        questionRepository.save(QuestionFactory.createQuestion(
                                        "DS Medium Question #" + (i + 1) + ": Describe algorithm " + (i + 1),
                                        DifficultyLevel.MEDIUM,
                                        Arrays.asList("Option A", "Option B", "Option C", "Option D"), 1, DS));
                }
                // HARD
                for (int i = 0; i < 10; i++) {
                        questionRepository.save(QuestionFactory.createQuestion(
                                        "DS Hard Question #" + (i + 1) + ": Optimization of data structure " + (i + 1),
                                        DifficultyLevel.HARD,
                                        Arrays.asList("Option A", "Option B", "Option C", "Option D"), 2, DS));
                }

                // ========== EXAM SEEDING ==========

                // 1. Data Structures & Algorithms
                Exam dsExam = new Exam();
                dsExam.setTitle("CS301: Data Structures & Algorithms");
                dsExam.setTopic(DS);
                dsExam.setDescription("Core assessment of algorithmic thinking and data structure implementation.");
                dsExam.setDurationMinutes(45);
                dsExam.setType(Exam.ExamType.ADAPTIVE);
                dsExam = examRepository.save(dsExam);

                ExamBlueprint dsBlueprint = new ExamBlueprint();
                dsBlueprint.setEasyCount(10);
                dsBlueprint.setMediumCount(10);
                dsBlueprint.setHardCount(10);
                dsBlueprint.setExam(dsExam);
                examBlueprintRepository.save(dsBlueprint);

                // 2. Computer Networks
                Exam cnExam = new Exam();
                cnExam.setTitle("CN302: Computer Networks");
                cnExam.setTopic(CN);
                cnExam.setDescription("Advanced networking principles, protocols, and architecture.");
                cnExam.setDurationMinutes(60);
                cnExam.setType(Exam.ExamType.ADAPTIVE);
                cnExam = examRepository.save(cnExam);

                ExamBlueprint cnBlueprint = new ExamBlueprint();
                cnBlueprint.setEasyCount(10);
                cnBlueprint.setMediumCount(10);
                cnBlueprint.setHardCount(10);
                cnBlueprint.setExam(cnExam);
                examBlueprintRepository.save(cnBlueprint);

                // 3. Software Engineering
                Exam seExam = new Exam();
                seExam.setTitle("SE303: Software Engineering");
                seExam.setTopic(SE);
                seExam.setDescription("Modern software development lifecycles, patterns, and principles.");
                seExam.setDurationMinutes(50);
                seExam.setType(Exam.ExamType.ADAPTIVE);
                seExam = examRepository.save(seExam);

                ExamBlueprint seBlueprint = new ExamBlueprint();
                seBlueprint.setEasyCount(10);
                seBlueprint.setMediumCount(10);
                seBlueprint.setHardCount(10);
                seBlueprint.setExam(seExam);
                examBlueprintRepository.save(seBlueprint);

                // 4. Admin Account Seeding
                Admin admin = new Admin();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setEmail("admin@pes.edu");
                admin.setRole(User.Role.ADMIN);
                adminRepository.save(admin);

                System.out.println("--- Adaptive Exam System: Mission Intelligence Initialized ---");
                System.out.println("Available Questions: [POOL READY FOR DS, CN, SE]");
                System.out.println("Test Pilots Ready: PES1UG23AM001-100, PES2UG23CS001-100, etc.");
                System.out.println("Lead Admin Ready: admin / admin");
        }

        private void seedStudentsInRange(String prefix, int count) {
                for (int i = 1; i <= count; i++) {
                        String paddedNum = String.format("%03d", i);
                        String srn = prefix + paddedNum;
                        Student student = new Student();
                        student.setUsername(srn.toLowerCase());
                        student.setPassword(passwordEncoder.encode("pass"));
                        student.setEmail(srn.toLowerCase() + "@pes.edu");
                        student.setRole(User.Role.STUDENT);
                        student.setSrn(srn);
                        student.parseSRN();
                        studentRepository.save(student);
                }
        }
}
