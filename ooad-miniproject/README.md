# 🎓 Proctor IQ - Adaptive Online Examination System

![Proctor IQ Overview](https://img.shields.io/badge/Status-Completed-brightgreen.svg)
![React](https://img.shields.io/badge/Frontend-React.js-blue.svg)
![Spring Boot](https://img.shields.io/badge/Backend-Spring_Boot-green.svg)
![OOAD](https://img.shields.io/badge/Design-OOAD_Driven-orange.svg)

Proctor IQ is a professional, enterprise-grade Adaptive Online Examination System equipped with live AI biometric proctoring. It dynamically adjusts exam difficulty based on real-time student performance and mathematically ensures academic integrity.

---

## ✨ Core Features

*   **🧠 Adaptive Difficulty Engine:** Utilizes the Strategy Pattern to dynamically serve Easy, Medium, or Hard questions based on the student's real-time accuracy percentage.
*   **👁️ Live AI Biometric Proctoring:** Client-side facial recognition (via `face-api.js`) that immediately detects multiple faces or absence of a face.
*   **🛡️ Anti-Cheat & Malpractice Detection:** Actively tracks and logs tab switching, right-clicking, dev tools usage, and blocks copy/pasting.
*   **⚖️ Automated Termination:** Automatically terminates exams upon reaching an 8-violation "Kick Threshold" and logs the incident to the backend.
*   **📊 Comprehensive Analytics:** Post-exam dashboard showing accuracy, time usage, performance by difficulty, topic breakdown, and competency level.

---

## 🛠️ Technology Stack

*   **Frontend:** React.js, Vite, Tailwind CSS, Recharts, `face-api.js`
*   **Backend:** Java 17, Spring Boot, Spring Security (JWT), Hibernate / JPA
*   **Database:** H2 (In-Memory) for rapid development & testing

---

## 🚀 Quick Start Guide

### Option 1: One-Click Run (Windows)
Simply execute the included launcher script from the root directory:
```bash
.\launch-proctor-iq.bat
```

### Option 2: Run Separately
**Backend:**
```bash
cd ooad-miniproject
mvn spring-boot:run
```

**Frontend:**
```bash
cd ooad-miniproject/frontend
npm install
npm run dev
```

### 🔑 Test Credentials
*   **Student Accounts:** `pes1ug23am001` to `pes1ug23am100` (Password: `pass`)
*   **Admin Account:** `admin` (Password: `admin`)

---

## 🏛️ OOAD & Design Patterns Matrix

This project was built strictly adhering to Object-Oriented Analysis and Design (OOAD) principles.

### General OOP Concepts
*   **Encapsulation:** Implemented in `Question.java` (Private fields, Builder pattern).
*   **Inheritance & Polymorphism:** Implemented in `User.java` (Abstract base class extended by `Student`/`Faculty`).

### SOLID Principles
*   **Single Responsibility (SRP):** `JwtUtil.java` (Token parsing only) and `ViolationService.java` (Integrity tracking only).
*   **Open-Closed (OCP):** `DifficultyStrategy.java` (Engine is open for new difficulty algorithms, closed for modification).
*   **Liskov Substitution (LSP):** `EasyStrategy`, `MediumStrategy` implement `DifficultyStrategy` interchangeably.
*   **Dependency Inversion (DIP):** Service layer uses interface-based constructor injection.

### GRASP Principles
*   **Controller:** `ExamController.java` (Primary UI-to-Backend router).
*   **Creator:** `ExamServiceImpl.java` (Responsible for instantiating complex `ExamSession` entities).
*   **Information Expert:** `AdaptiveEngine.java` (Selects next question based on its internal session history data).
*   **Low Coupling:** `apiService.js` (Frontend networking separated from UI).

### GoF Design Patterns
*   **Strategy Pattern:** `AdaptiveEngine.java` dynamically swaps `DifficultyStrategy` algorithms at runtime based on the score.
*   **Builder Pattern:** `ExamServiceImpl.java` builds the massive `ExamSession` object safely.
*   **Facade Pattern:** `ExamServiceImpl.startExam()` hides database/audit orchestration behind a single call.
*   **Proxy Pattern:** `ProctoringServiceProxy.java` intercepts calls for logging/security.
*   **Singleton Pattern:** Spring IoC container globally manages `@Service` classes like `ViolationService`.

---

## 👥 Development Team
*   **Member 1 (Frontend & Data Flow):** React UI, Reporting Adapters, Observer Events.
*   **Member 2 (Architecture & Adaptive Engine):** Spring Boot Core, Strategy Pattern, Builder Pattern.
*   **Member 3 (Security & DB Integrity):** JWT Auth, Biometric Proctoring, Proxy Pattern.

---

*This project was developed as a university mini-project demonstrating mastery over Software Engineering and Design Patterns.*
