# Proctor IQ - Adaptive Online Examination System
## Final Project Report & OOAD Architecture

This document outlines the entire architecture of the **Proctor IQ** system, explicitly detailing the Object-Oriented Analysis and Design (OOAD) principles and Design Patterns utilized. It provides a highly specific division of labor and presentation responsibilities for a 3-member development team.

---

## 👥 Team Division & Presentation Responsibilities

To effectively present and defend the architecture of Proctor IQ, the system is divided into three distinct spheres. **Each member is strictly responsible for explaining their assigned files and their assigned Design Patterns during the presentation.**

### 🧑‍💻 Member 1: Reporting, UI & Event Handling (Data Flow Lead)
**Role:** Handled the Frontend UI workflow and the backend report generation and event alerting mechanisms.
*   **Files to Explain:**
    *   `Exam.jsx` (React UI and violation catching)
    *   `apiService.js` (Demonstrating **Low Coupling** and **Controller** communication)
    *   `ReportExporter.java`, `PdfReportAdapter.java`, `ExcelReportAdapter.java`
    *   `AlertObserver.java`, `AuditLogObserver.java`
*   **Assigned Design Patterns:**
    1.  **Adapter Pattern:** Must explain how `PdfReportAdapter` and `ExcelReportAdapter` implement the `ReportExporter` interface to convert raw exam results into different downloadable formats without changing the core result logic.
    2.  **Observer Pattern:** Must explain how `AlertObserver` and `AuditLogObserver` listen for `ViolationDetectedEvent` to trigger real-time alerts and audit logs decoupling the event trigger from the action.

### 🧑‍💻 Member 2: Core Exam Lifecycle & Adaptive Engine (Architecture Lead)
**Role:** Handled the core exam logic, dynamic difficulty scaling, and complex object creation.
*   **Files to Explain:**
    *   `AdaptiveEngine.java` (Demonstrating **Information Expert**)
    *   `DifficultyStrategy.java`, `EasyStrategy.java`, `HardStrategy.java` (Demonstrating **Open-Closed Principle (OCP)**)
    *   `ExamServiceImpl.java` (Demonstrating **Creator**)
    *   `Question.java`
*   **Assigned Design Patterns:**
    1.  **Strategy Pattern:** Must explain how `AdaptiveEngine` dynamically swaps between `EasyStrategy`, `MediumStrategy`, and `HardStrategy` at runtime based on the student's real-time accuracy percentage.
    2.  **Builder Pattern:** Must explain how `Question.builder()` and `ExamSession.builder()` are used in `ExamServiceImpl` to safely construct complex objects with multiple parameters.
    3.  **Facade Pattern:** Must explain how `ExamServiceImpl.startExam()` acts as a Facade, hiding the complex orchestration of databases, audit logs, and engine initialization behind a single method call.

### 🧑‍💻 Member 3: Security, Proctoring & System Integrity (Security Lead)
**Role:** Handled biometric tracking, JWT authentication, access control, and centralized service instantiation.
*   **Files to Explain:**
    *   `ProctoringServiceProxy.java`, `ProctoringServiceImpl.java`
    *   `ExamSessionFactory.java`
    *   `ViolationService.java` (Demonstrating **High Cohesion** and **SRP**)
    *   `JwtUtil.java` (Demonstrating **SRP**)
    *   `User.java` (Demonstrating **Inheritance** and **Polymorphism**)
*   **Assigned Design Patterns:**
    1.  **Proxy Pattern:** Must explain how `ProctoringServiceProxy` wraps the actual `ProctoringServiceImpl` to intercept method calls, enabling strict access control and logging before the actual proctoring logic executes.
    2.  **Factory Pattern:** Must explain how `ExamSessionFactory` encapsulates the creation logic of an exam session, separating creation logic from the business logic.
    3.  **Singleton Pattern:** Must explain how Spring's IoC container manages `ViolationService` and `JwtUtil` as Singletons to ensure only one global instance exists in memory, saving resources.

---

## 🏛️ OOAD Principles & Patterns Implementation Mapping

### 1. General OOP Concepts
| Principle | File / Implementation | Description |
| :--- | :--- | :--- |
| **Encapsulation** | `Question.java` | All fields are strictly private, accessible only via Lombok `@Getter`/`@Setter` and initialized securely via Builders. |
| **Inheritance** | `User.java` | Acts as an abstract base class. Extended by concrete roles like `Student` and `Faculty`, while implementing Spring Security's `UserDetails`. |
| **Polymorphism** | `User.java` | The `getAuthorities()` method polymorphic behavior allows Spring Security to handle diverse role authorizations fluidly. |

### 2. SOLID Principles
| Principle | File / Implementation | Description |
| :--- | :--- | :--- |
| **Single Responsibility (SRP)** | `JwtUtil.java` | Handles *only* token parsing/generation. `ViolationService` handles *only* integrity breaches. |
| **Open-Closed (OCP)** | `DifficultyStrategy.java` | The difficulty logic is open for extension (e.g., adding `ExtremeStrategy`) but closed for modification. |
| **Liskov Substitution (LSP)** | `EasyStrategy` | Any strategy class can seamlessly substitute the `DifficultyStrategy` interface inside `AdaptiveEngine`. |
| **Interface Segregation (ISP)** | `ExamController.java` | Controllers are injected with highly specific, segregated interfaces rather than a monolithic service class. |
| **Dependency Inversion (DIP)** | `AnswerService.java` | High-level modules depend on abstractions via constructor injection rather than concrete implementations. |

### 3. GRASP Principles
| Principle | File / Implementation | Description |
| :--- | :--- | :--- |
| **Controller** | `ExamController.java` | Acts as the primary UI-to-Backend router, receiving HTTP requests and delegating to services. |
| **Creator** | `ExamServiceImpl.java` | Responsible for instantiating the `ExamSession` object because it possesses the required initializing data. |
| **Information Expert** | `AdaptiveEngine.java` | Tasked with fetching the next question because it processes the session's answer history to make that decision. |
| **High Cohesion** | `ViolationService.java` | All methods are strictly related to managing exam integrity, avoiding unrelated bloat. |
| **Low Coupling** | `apiService.js` | React components do not make raw `fetch` calls, delegating to a centralized API service. |
