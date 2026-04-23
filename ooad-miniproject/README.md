# Proctor IQ - Adaptive Online Examination System

Welcome to the **Proctor IQ** codebase! This is a state-of-the-art Adaptive Online Examination System built with rigorous Object-Oriented Analysis and Design (OOAD) principles and robust software architecture at its core.

## 🚀 Features
- **Adaptive Examination Engine**: Dynamically adjusts the difficulty of questions based on real-time student performance, providing a highly personalized and accurate assessment.
- **Robust Proctoring & Security**: Features real-time violation detection, comprehensive audit logs, and proxy-based access control to ensure maximum exam integrity.
- **Micro-Architected Backend**: Driven by a suite of carefully implemented GoF Design Patterns (Strategy, Observer, Adapter, Proxy, Factory, Singleton, Builder, Facade).
- **Extensible Reporting**: Seamlessly exports exam results into multiple formats (PDF, Excel) using the Adapter pattern without modifying the core business logic.
- **Responsive UI**: A modern React frontend for seamless student, proctor, and admin experiences.

## 🏛️ System Architecture & OOAD Principles

Proctor IQ is engineered as a definitive showcase of Object-Oriented best practices. The system architecture is divided into clear logical spheres.

### 🧩 Design Patterns Implemented
- **Strategy Pattern (`AdaptiveEngine`)**: Dynamically swaps between `EasyStrategy`, `MediumStrategy`, and `HardStrategy` depending on live accuracy.
- **Observer Pattern**: Real-time alerts and audit logs decoupled and triggered asynchronously via `ViolationDetectedEvent`.
- **Adapter Pattern (`ReportExporter`)**: Flexible report generation using `PdfReportAdapter` and `ExcelReportAdapter`.
- **Proxy Pattern (`ProctoringServiceProxy`)**: Wraps core proctoring for strict access control and logging.
- **Factory Pattern (`ExamSessionFactory`)**: Encapsulates complex exam session instantiation and isolates creation logic.
- **Singleton Pattern**: Ensures efficient resource management for stateless, system-wide services like `ViolationService` and `JwtUtil`.
- **Facade Pattern (`ExamServiceImpl`)**: Simplifies the complex orchestration of exam initialization, databases, and engine setups behind a single interface.
- **Builder Pattern**: Used extensively in `Question` and `ExamSession` for safe and readable construction of complex objects.

### 📐 SOLID & GRASP Principles
- **SRP & High Cohesion**: Services like `JwtUtil` and `ViolationService` do exactly one thing, leading to highly cohesive modules.
- **Open-Closed (OCP)**: The difficulty calculation engine is open for new strategies but completely closed to modification.
- **Dependency Inversion (DIP)**: Core modules and controllers rely entirely on abstractions (interfaces) rather than hardcoded implementations.
- **Information Expert & Creator**: Responsibilities for logic execution and object creation are meticulously assigned to the classes that possess the necessary data (e.g., `AdaptiveEngine`, `ExamServiceImpl`).
- **Low Coupling**: The React frontend communicates strictly through a centralized `apiService.js`, decoupling UI components from raw fetch logic.

## 🛠️ Technology Stack
- **Backend**: Java, Spring Boot, Spring Security (JWT)
- **Frontend**: React, Vite
- **Database**: Relational Database (via JPA/Hibernate)

## ⚙️ Quick Start

**1. Clone the repository:**
```bash
git clone https://github.com/SaurabhR44/OOAD_FINAL.git
cd ooad-miniproject
```

**2. Start the Backend:**
Ensure you have Maven and JDK 17+ installed. Navigate to the root directory and run:
```bash
mvn spring-boot:run
```

**3. Start the Frontend:**
Open a new terminal, navigate to the `frontend` directory, install dependencies, and run:
```bash
cd frontend
npm install
npm run dev
```

---
*Developed as a comprehensive OOAD Project.*
