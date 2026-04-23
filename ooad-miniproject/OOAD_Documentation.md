# Proctor IQ: OOAD Implementation Documentation

## 1. Design Patterns Mapping

| Pattern | Implementation Class | Rationale |
| :--- | :--- | :--- |
| **Singleton** | All Spring `@Service` / `@Component` beans | Managed by Spring IoC container as unique instances. |
| **Explicit Singleton** | `ProctoringSessionRegistry` | Manual thread-safe implementation using double-checked locking. |
| **Strategy** | `DifficultyStrategy` (Easy, Medium, Hard) | Decouples the difficulty adjustment algorithm from the `AdaptiveEngine`. |
| **Factory Method** | `ExamSessionFactory`, `QuestionFactory` | Encapsulates the creation logic for complex entities. |
| **Facade** | `ExamService`, `ProctoringService` | Provides a simplified interface to a complex set of subsystem operations. |
| **Proxy** | `ProctoringServiceProxy` | Adds cross-cutting concerns (rate-limiting) to the proctoring service. |
| **Observer** | `ViolationDetectedEvent`, `AuditLogObserver` | Decouples violation detection from reaction logic (logging, alerts). |
| **Adapter** | `ExcelReportAdapter`, `PdfReportAdapter` | Adapts system data to external report formats (POI, iText). |
| **Chain of Responsibility** | `JwtAuthenticationFilter`, `SecurityConfig` | Security filters process requests in a sequential chain. |
| **Iterator** | `ExamServiceImpl`, `AdaptiveEngine` | Uses Java Stream API to iterate over internal collections. |
| **Builder** | `User`, `Question`, `ExamSession` | Provides a fluent API for object construction (via Lombok). |

## 2. SOLID Principles Mapping

| Principle | Evidence in Code | Rationale |
| :--- | :--- | :--- |
| **Single Responsibility (SRP)** | `JwtUtil`, `AuthService` | Each class has one reason to change (JWT logic vs Auth logic). |
| **Open/Closed (OCP)** | `AdaptiveEngine` with `DifficultyStrategy` | New difficulty algorithms can be added without modifying the engine. |
| **Liskov Substitution (LSP)** | `Student` extends `User` | Students can be treated as `UserDetails` in Spring Security without side effects. |
| **Interface Segregation (ISP)** | `ExamService`, `ProctoringService` | Clients only depend on the methods they actually use. |
| **Dependency Inversion (DIP)** | All Controllers & Services | High-level modules depend on abstractions (Interfaces), not concretions. |

## 3. GRASP Principles Mapping

| Principle | Evidence in Code | Rationale |
| :--- | :--- | :--- |
| **Information Expert** | `AdaptiveEngine` | Contains the knowledge of question banks and session history. |
| **Creator** | `ExamSessionFactory` | Responsible for creating `ExamSession` objects. |
| **Controller** | `ExamController`, `AuthController` | First point of contact for UI events; delegates to services. |
| **Low Coupling** | `apiService.js` (Frontend) | Frontend components don't know about raw backend URLs. |
| **High Cohesion** | `ExamServiceImpl` | Focused strictly on exam lifecycle management. |
| **Polymorphism** | `ViolationBadge.jsx` (Frontend) | Renders differently based on the violation severity type. |

## 4. Academic novelty
The system integrates **AI-driven real-time proctoring** with a **dynamic adaptive difficulty engine**. The use of a **Proxy** for rate-limiting proctoring events ensures system stability during high-load examinations, while the **Observer** pattern allows for extensible reaction mechanisms (e.g., auto-termination vs. warning) without core service modification.
