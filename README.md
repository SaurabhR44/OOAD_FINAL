# 🎓 Proctor IQ: Intelligent Adaptive Examination System

![Banner](assets/banner.png)

Proctor IQ is a cutting-edge **Online Examination Ecosystem** designed to revolutionize digital assessments. By merging **Adaptive Testing Algorithms** with **AI-Powered Proctoring**, it ensures both academic integrity and a personalized testing experience. Built with a strict adherence to **Object-Oriented Analysis and Design (OOAD)** principles, it serves as a gold standard for scalable, maintainable software architecture.

---

## 🌟 Key Features

### 🧠 Adaptive Question Engine
- **Dynamic Difficulty**: Real-time adjustment of question difficulty based on student performance using the **Strategy Pattern**.
- **Performance Feedback**: Instant analysis of strengths and weaknesses.

### 👁️ AI-Powered Proctoring
- **Face Recognition**: Real-time monitoring to prevent impersonation.
- **Environment Analysis**: Audio and tab-switching detection using the **Observer Pattern**.
- **Secure Browser**: Proxy-based control to restrict unauthorized access.

### 📊 Enterprise Reporting
- **Multi-Format Export**: Generate professional performance reports in **PDF** and **Excel** using the **Adapter Pattern**.
- **Detailed Analytics**: Visual representation of class and individual performance.

### 🔐 Robust Security
- **JWT Authentication**: Secure, stateless authentication with Role-Based Access Control (RBAC).
- **Audit Trails**: Comprehensive logging of all system activities.

---

## 🏗️ Architecture & Design Patterns

This project is a masterclass in software design, implementing over **11 Design Patterns** to ensure decoupled and reusable code.

### 📐 OOAD Compliance
- **SOLID Principles**: 100% adherence to Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, and Dependency Inversion.
- **GRASP Patterns**: Expert, Creator, Low Coupling, High Cohesion, and Controller.

### 🧩 Implemented Patterns
| Pattern | Implementation |
| :--- | :--- |
| **Strategy** | For swapping adaptive difficulty algorithms (Easy, Medium, Hard). |
| **Observer** | For proctoring alerts and audit log notifications. |
| **Proxy** | For secure proctoring service access and validation. |
| **Adapter** | For converting data models into Excel/PDF formats. |
| **Factory** | For dynamic creation of Question and Exam objects. |
| **Singleton** | For global configuration and database connection pools. |
| **Chain of Responsibility** | For multi-layer authentication and authorization. |

---

## 🛠 Tech Stack

**Backend:**
- **Framework**: Spring Boot 3.2.x
- **Security**: Spring Security, JWT
- **Database**: H2 (In-memory) / MySQL
- **ORM**: Spring Data JPA / Hibernate
- **Reporting**: Apache POI (Excel), iText (PDF)

**Frontend:**
- **Framework**: React 18
- **Styling**: Tailwind CSS
- **Animations**: Framer Motion
- **Icons**: Lucide React

---

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Node.js 18+
- Maven 3.8+

### Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone https://github.com/SaurabhR44/OOAD_FINAL.git
   cd OOAD_FINAL
   ```

2. **Run Backend**
   ```bash
   cd ooad-miniproject
   mvn spring-boot:run
   ```

3. **Run Frontend**
   ```bash
   cd ooad-miniproject/frontend
   npm install
   npm run dev
   ```

---

## 📸 Dashboard Preview

![Dashboard](assets/dashboard.png)

---

*Developed with ❤️ for the OOAD Mini-Project Submission 2024.*
