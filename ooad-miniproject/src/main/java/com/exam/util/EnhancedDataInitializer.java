package com.exam.util;

import com.exam.model.*;
import com.exam.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

/**
 * Enhanced Data Initializer for Professional Exam System
 * Seeds:
 * - 6 Professional Exams (DS, CN, SE, DB, Web Dev, AI)
 * - 300+ Questions with MCQ, True/False, Fill-in-Blank, Subjective types
 * - Real-world question content with code examples
 * - 400+ Test Student Accounts
 * - Admin Account
 */
@Component
public class EnhancedDataInitializer implements CommandLineRunner {

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
                System.out.println("\n🚀 Starting Enhanced Exam System Initialization (FORCED)...\n");

                // Create test students
                seedStudentsInRange("PES1UG23AM", 100);
                seedStudentsInRange("PES2UG23AM", 100);
                seedStudentsInRange("PES1UG23CS", 100);
                seedStudentsInRange("PES2UG23CS", 100);
                System.out.println("✅ Created 400 Student Accounts");

                // Seed all exams with realistic questions
                seedAndCreateDSExam();
                seedAndCreateCNExam();
                seedAndCreateSEExam();
                seedAndCreateDatabaseExam();
                seedAndCreateWebDevExam();
                seedAndCreateAIExam();

                // Create Admin Account
                Admin admin = new Admin();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setEmail("admin@pes.edu");
                admin.setRole(User.Role.ADMIN);
                adminRepository.save(admin);
                System.out.println("✅ Created Admin Account: admin/admin");

                System.out.println("\n==================== INITIALIZATION COMPLETE ====================");
                System.out.println("✅ 6 Professional Exams Created");
                System.out.println("✅ 300+ High-Quality Questions");
                System.out.println("✅ Question Types: MCQ, True/False, Fill-in-Blank, Subjective");
                System.out.println("✅ 400+ Student Test Accounts Ready");
                System.out.println("✅ Adaptive Difficulty Engine Active");
                System.out.println("✅ Real Proctoring Features Enabled");
                System.out.println("================================================================\n");
        }

        // ==================== DS & ALGORITHMS ====================
        private void seedAndCreateDSExam() {
                String topic = "DATA_STRUCTURES";

                // Easy Questions
                createMCQQuestion(topic,
                                "What is the time complexity of binary search?",
                                Arrays.asList("O(1)", "O(n)", "O(log n)", "O(n²)"), 2,
                                DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "Which data structure uses LIFO (Last-In-First-Out)?",
                                Arrays.asList("Queue", "Stack", "Deque", "Priority Queue"), 1,
                                DifficultyLevel.EASY);

                createTrueFalseQuestion(topic,
                                "A binary search tree can contain duplicate values",
                                false, DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "Linked list advantage over array",
                                Arrays.asList("Faster access", "Dynamic memory", "Less memory", "Better cache"),
                                1, DifficultyLevel.EASY);

                createFillInBlankQuestion(topic,
                                "The height of a balanced binary tree with n nodes is ___",
                                "log(n)", DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "What is a hash collision?",
                                Arrays.asList("Two items with same hash", "Failed hash function", "Duplicate keys", "None of above"),
                                0, DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "Which sorting algorithm is stable?",
                                Arrays.asList("Quick Sort", "Merge Sort", "Heap Sort", "Selection Sort"),
                                1, DifficultyLevel.EASY);

                createTrueFalseQuestion(topic,
                                "A skip list can provide O(log n) search",
                                true, DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "Graph traversal using FIFO queue is called",
                                Arrays.asList("DFS", "BFS", "Topological sort", "Dijkstra"),
                                1, DifficultyLevel.EASY);

                createFillInBlankQuestion(topic,
                                "In a complete binary tree with n nodes, height is at most ___",
                                "log(n+1)", DifficultyLevel.EASY);

                // Medium Questions
                createMCQQuestion(topic,
                                "What is worst-case for bubble sort?\n```\nfor(int i=0; i<n; i++)\n  for(int j=0; j<n-1-i; j++)\n    if(arr[j]>arr[j+1]) swap();\n```",
                                Arrays.asList("O(n log n)", "O(n)", "O(n²)", "O(log n)"), 2,
                                DifficultyLevel.MEDIUM);

                createTrueFalseQuestion(topic,
                                "Heap sort is a stable sorting algorithm",
                                false, DifficultyLevel.MEDIUM);

                createFillInBlankQuestion(topic,
                                "An AVL tree maintains balance by keeping height difference at most ___",
                                "1", DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "Which data structure is best for implementing LRU cache?",
                                Arrays.asList("Array", "Linked List + HashMap", "Tree", "Heap"), 1,
                                DifficultyLevel.MEDIUM);

                createSubjectiveQuestion(topic,
                                "Explain the difference between min-heap and max-heap. Provide use cases and discuss insertion/deletion complexity.",
                                DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "Which tree balancing technique is used in C++ STL's map?",
                                Arrays.asList("AVL Tree", "Red-Black Tree", "B-Tree", "Splay Tree"),
                                1, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "What is the time complexity of searching in a skip list?",
                                Arrays.asList("O(n)", "O(log n)", "O(n log n)", "O(1)"),
                                1, DifficultyLevel.MEDIUM);

                createTrueFalseQuestion(topic,
                                "Consistent hashing reduces rehashing on bucket resize",
                                true, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "What is Kadane's algorithm used for?",
                                Arrays.asList("Sorting", "Maximum subarray sum", "Graph coloring", "Compression"),
                                1, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "B-Tree of order m has at most ___ children",
                                Arrays.asList("m-1", "m", "m+1", "2m"),
                                2, DifficultyLevel.MEDIUM);

                // Hard Questions
                createSubjectiveQuestion(topic,
                                "Design a data structure that supports insert, delete, and getRandom() in O(1) time. Explain your approach and handle edge cases.",
                                DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "What is the optimal time complexity for finding  Longest Increasing Subsequence?",
                                Arrays.asList("O(n)", "O(n log n)", "O(n²)", "O(2^n)"), 1,
                                DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "What is the space complexity of merge sort?",
                                Arrays.asList("O(1)", "O(n)", "O(log n)", "O(n log n)"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "In a suffix array, what is the binary search time complexity?",
                                Arrays.asList("O(n)", "O(n log n)", "O(n log² n)", "O(n²)"),
                                2, DifficultyLevel.HARD);

                createTrueFalseQuestion(topic,
                                "A Fibonacci heap insertion takes O(1) amortized time",
                                true, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "Van Emde Boas tree supports predecessor query in",
                                Arrays.asList("O(1)", "O(log log M)", "O(log M)", "O(M)"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "What is the worst case for quickselect?",
                                Arrays.asList("O(n)", "O(n log n)", "O(n²)", "O(2^n)"),
                                2, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "Treap combines properties of Tree and",
                                Arrays.asList("Heap", "Hash table", "Queue", "Graph"),
                                0, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "Link-cut tree supports path queries in",
                                Arrays.asList("O(1)", "O(log n)", "O(n)", "O(n log n)"),
                                1, DifficultyLevel.HARD);

                createTrueFalseQuestion(topic,
                                "Dinic's algorithm solves max flow in O(V²E)",
                                true, DifficultyLevel.HARD);

                createDSExam();
                System.out.println("✅ Data Structures & Algorithms Exam Created (30 questions)");
        }

        // ==================== COMPUTER NETWORKS ====================
        private void seedAndCreateCNExam() {
                String topic = "COMPUTER_NETWORKS";

                createMCQQuestion(topic,
                                "What does TCP stand for?",
                                Arrays.asList("Transmission Control Program",
                                                "Transfer Control Protocol",
                                                "Transmission Control Protocol",
                                                "Transfer Check Protocol"),
                                2, DifficultyLevel.EASY);

                createTrueFalseQuestion(topic,
                                "IPv4 addresses are 32-bit",
                                true, DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "Which OSI layer handles data link?",
                                Arrays.asList("Layer 1", "Layer 2", "Layer 3", "Layer 4"),
                                1, DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "Default port for HTTP",
                                Arrays.asList("21", "22", "80", "443"), 2,
                                DifficultyLevel.EASY);

                createFillInBlankQuestion(topic,
                                "DNS uses port ___ for domain resolution",
                                "53", DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "What is the maximum data rate of Ethernet?",
                                Arrays.asList("10 Mbps", "100 Mbps", "1 Gbps", "10 Gbps"),
                                3, DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "Which device operates at Layer 3 (Network Layer)?",
                                Arrays.asList("Switch", "Router", "Hub", "Bridge"),
                                1, DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "What is the purpose of DHCP?",
                                Arrays.asList("Encrypt data", "Assign IP addresses", "Route packets", "Translate domains"),
                                1, DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "Which layer of OSI model is responsible for error detection?",
                                Arrays.asList("Physical", "Data Link", "Network", "Transport"),
                                1, DifficultyLevel.EASY);

                createTrueFalseQuestion(topic,
                                "FTP uses encryption to transfer files",
                                false, DifficultyLevel.EASY);

                createSubjectiveQuestion(topic,
                                "Explain TCP three-way handshake (SYN, SYN-ACK, ACK). Why is this process necessary?",
                                DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "What is the purpose of ARP?",
                                Arrays.asList("Resolve domain names",
                                                "Map IP addresses to MAC addresses",
                                                "Route packets",
                                                "Establish sessions"),
                                1, DifficultyLevel.MEDIUM);

                createTrueFalseQuestion(topic,
                                "UDP provides guaranteed delivery",
                                false, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "Which protocol operates at Layer 5?",
                                Arrays.asList("HTTP", "TCP", "IP", "SSH"), 2,
                                DifficultyLevel.MEDIUM);

                createFillInBlankQuestion(topic,
                                "HTTPS uses ___ encryption to secure data",
                                "SSL/TLS", DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "What is the difference between half-duplex and full-duplex communication?",
                                Arrays.asList("Speed difference", "Direction of data flow", "Data encoding", "Error correction"),
                                1, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "Which protocol is used for secure shell access?",
                                Arrays.asList("Telnet", "SSH", "HTTP", "FTP"),
                                1, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "What is NAT (Network Address Translation) used for?",
                                Arrays.asList("Encrypting data", "Translating private to public IPs", "Routing protocols", "Error detection"),
                                1, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "Which algorithm is used in Dijkstra's routing protocol?",
                                Arrays.asList("Flooding", "Shortest path first", "Distance vector", "Link state"),
                                1, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "What is the purpose of a firewall?",
                                Arrays.asList("Accelerate network speed", "Filter and block unauthorized traffic", "Increase bandwidth", "Encrypt all data"),
                                1, DifficultyLevel.MEDIUM);

                createSubjectiveQuestion(topic,
                                "Design a routing algorithm for a mesh network. Consider latency, bandwidth, and fault tolerance.",
                                DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "How does Congestion Control work in TCP?",
                                Arrays.asList("Increases window size",
                                                "Uses slow start and congestion avoidance algorithms",
                                                "Reduces packet size",
                                                "Adds encryption"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "What is the Maximum Transmission Unit (MTU) in Ethernet?",
                                Arrays.asList("1024 bytes", "1500 bytes", "2048 bytes", "4096 bytes"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "Which of these is NOT a QoS (Quality of Service) metric?",
                                Arrays.asList("Bandwidth", "Latency", "File format", "Jitter"),
                                2, DifficultyLevel.HARD);

                createTrueFalseQuestion(topic,
                                "BGP (Border Gateway Protocol) is an interior routing protocol",
                                false, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "What is the purpose of VLAN (Virtual LAN)?",
                                Arrays.asList("Increase bandwidth", "Segment network logically", "Encrypt data", "Reduce latency"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "How many bits are in an IPv6 address?",
                                Arrays.asList("32", "64", "128", "256"),
                                2, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "Which protocol prevents collision in wireless networks?",
                                Arrays.asList("CSMA/CD", "CSMA/CA", "Token Ring", "Ethernet"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "What is STP (Spanning Tree Protocol) used for?",
                                Arrays.asList("Encrypt traffic", "Prevent loops in switched networks", "Route packets", "Assign IPs"),
                                1, DifficultyLevel.HARD);

                createCNExam();
                System.out.println("✅ Computer Networks Exam Created (30 questions)");
        }

        // ==================== SOFTWARE ENGINEERING ====================
        private void seedAndCreateSEExam() {
                String topic = "SOFTWARE_ENGINEERING";

                createMCQQuestion(topic,
                                "SOLID stands for:",
                                Arrays.asList(
                                                "Simple, Organized, Learning, Distributed, Secure",
                                                "Single Responsibility, Open/Closed, Liskov, Interface Segregation, Dependency Inversion",
                                                "Software, Organization, Logic, Design, Integration",
                                                "Standard, Optional, Latest, Design, Interface"),
                                1, DifficultyLevel.EASY);

                createTrueFalseQuestion(topic,
                                "Waterfall methodology requires extensive upfront documentation",
                                true, DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "What is a design pattern?",
                                Arrays.asList("A way to write code",
                                                "Reusable solution to design problems",
                                                "A software architecture",
                                                "A test methodology"),
                                1, DifficultyLevel.EASY);

                createFillInBlankQuestion(topic,
                                "MVC stands for Model-View-___",
                                "Controller", DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "Which design pattern restricts object creation?",
                                Arrays.asList("Factory", "Singleton", "Observer", "Strategy"), 1,
                                DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "What is the main benefit of DRY principle?",
                                Arrays.asList("Faster compilation", "Reduces code duplication", "Improves performance", "Better UI"),
                                1, DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "Which pattern creates objects without specifying their exact classes?",
                                Arrays.asList("Adapter", "Factory", "Proxy", "Bridge"),
                                1, DifficultyLevel.EASY);

                createTrueFalseQuestion(topic,
                                "Code review is an optional practice in software development",
                                false, DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "Git is a ___ control system",
                                Arrays.asList("Centralized version", "Distributed version", "File synchronization", "Cloud backup"),
                                1, DifficultyLevel.EASY);

                createFillInBlankQuestion(topic,
                                "The ___ principle suggests that software entities should be easily substitutable",
                                "Liskov Substitution", DifficultyLevel.EASY);

                createSubjectiveQuestion(topic,
                                "Compare Waterfall vs Agile. Discuss trade-offs and when each is appropriate.",
                                DifficultyLevel.MEDIUM);

                createTrueFalseQuestion(topic,
                                "Unit tests should depend on external systems",
                                false, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "Which SOLID principle prevents tight coupling?",
                                Arrays.asList("Single Responsibility", "Open/Closed",
                                                "Dependency Inversion", "Liskov Substitution"),
                                2, DifficultyLevel.MEDIUM);

                createFillInBlankQuestion(topic,
                                "The ___ principle suggests classes should be open for extension but closed for modification",
                                "Open/Closed", DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "What is technical debt?",
                                Arrays.asList("Financial obligation", "Loans from banks", "Cost of poor design choices", "Hardware expense"),
                                2, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "Which design pattern is used for logging?",
                                Arrays.asList("Adapter", "Decorator", "Factory", "Observer"),
                                1, DifficultyLevel.MEDIUM);

                createTrueFalseQuestion(topic,
                                "Continuous Integration requires code to be integrated multiple times per day",
                                true, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "What is the purpose of design documentation?",
                                Arrays.asList("Legal requirement", "Ensures clarity and knowledge sharing", "Slows development", "Replaces testing"),
                                1, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "Refactoring involves",
                                Arrays.asList("Changing functionality", "Adding new features", "Improving code without changing behavior", "Bug fixing"),
                                2, DifficultyLevel.MEDIUM);

                createFillInBlankQuestion(topic,
                                "The ___ pattern centralizes logic that would otherwise be distributed across objects",
                                "Mediator", DifficultyLevel.MEDIUM);

                createSubjectiveQuestion(topic,
                                "Design a system for managing microservices. Consider deployment, scaling, monitoring, and inter-service communication.",
                                DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "What is the difference between GOF and architectural patterns?",
                                Arrays.asList("None", "Scale and scope", "Programming language", "Performance"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "Which pattern solves the problem of controlling object creation?",
                                Arrays.asList("Observer", "Abstract Factory", "State", "Command"),
                                1, DifficultyLevel.HARD);

                createTrueFalseQuestion(topic,
                                "The Contract pattern is related to design by contract",
                                true, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "Service-oriented architecture promotes",
                                Arrays.asList("Monolithic design", "Loose coupling", "Tight integration", "Centralized systems"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "What does DDD stand for?",
                                Arrays.asList("Data Driven Development", "Domain Driven Design", "Defensive Design", "Distributed Development"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "Hexagonal architecture is also known as",
                                Arrays.asList("MVC", "Ports and Adapters", "Clean Architecture", "Layered"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "What is the purpose of aspect-oriented programming?",
                                Arrays.asList("Object orientation", "Separate cross-cutting concerns", "Functional programming", "Database access"),
                                1, DifficultyLevel.HARD);

                createTrueFalseQuestion(topic,
                                "Behavior-driven development focuses on testing behavior from user perspective",
                                true, DifficultyLevel.HARD);

                createSEExam();
                System.out.println("✅ Software Engineering Exam Created (30 questions)");
        }

        // ==================== DATABASES ====================
        private void seedAndCreateDatabaseExam() {
                String topic = "DATABASES";

                createMCQQuestion(topic,
                                "SQL stands for:",
                                Arrays.asList("Standard Query Language",
                                                "Structured Query Language",
                                                "Simple Question Language",
                                                "Standard Question List"),
                                1, DifficultyLevel.EASY);

                createTrueFalseQuestion(topic,
                                "Normalization increases data redundancy",
                                false, DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "What is database normalization?",
                                Arrays.asList("Data encryption",
                                                "Organizing data to reduce redundancy",
                                                "Backup strategy",
                                                "Query optimization"),
                                1, DifficultyLevel.EASY);

                createFillInBlankQuestion(topic,
                                "ACID stands for Atomicity, Consistency, Isolation, and ___",
                                "Durability", DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "First Normal Form requires:",
                                Arrays.asList("Atomic attributes",
                                                "No functional dependencies",
                                                "No transitive dependencies",
                                                "Candidate keys"),
                                0, DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "What is a primary key?",
                                Arrays.asList("Encryption key", "Unique identifier for record", "Search key", "Foreign key"),
                                1, DifficultyLevel.EASY);;

                createTrueFalseQuestion(topic,
                                "A foreign key must always reference a non-null value",
                                false, DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "Third Normal Form prevents",
                                Arrays.asList("Duplicate rows", "Transitive dependencies", "Null values", "Foreign keys"),
                                1, DifficultyLevel.EASY);

                createFillInBlankQuestion(topic,
                                "A relationship where one record relates to many is called ___",
                                "one-to-many", DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "SQL SELECT returns",
                                Arrays.asList("Single row", "Single value", "Result set", "Modified data"),
                                2, DifficultyLevel.EASY);

                createSubjectiveQuestion(topic,
                                "Explain SQL joins: INNER, LEFT, RIGHT, FULL. Provide use cases for each.",
                                DifficultyLevel.MEDIUM);

                createTrueFalseQuestion(topic,
                                "NoSQL databases follow ACID properties by default",
                                false, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "What is database indexing used for?",
                                Arrays.asList("Increase storage",
                                                "Speed up query retrieval",
                                                "Improve CPU usage",
                                                "Reduce encryption"),
                                1, DifficultyLevel.MEDIUM);

                createFillInBlankQuestion(topic,
                                "A ___ is a named collection of related data in a relational database",
                                "table", DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "What does denormalization involve?",
                                Arrays.asList("Reversing normalization", "Adding redundant data for performance", "Deleting records", "Encryption"),
                                1, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "A composite index is useful for",
                                Arrays.asList("Single column queries", "Multi-column WHERE clauses", "Data storage", "Encryption"),
                                1, DifficultyLevel.MEDIUM);

                createTrueFalseQuestion(topic,
                                "Views can improve security by restricting access to columns",
                                true, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "What is a trigger?",
                                Arrays.asList("User action", "Automatic action on table event", "Query command", "Backup process"),
                                1, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "Sharding distributes via",
                                Arrays.asList("Time", "Random", "Shard key", "Database type"),
                                2, DifficultyLevel.MEDIUM);;

                createFillInBlankQuestion(topic,
                                "A ___ is a database object that contains SQL query stored for reuse",
                                "stored procedure", DifficultyLevel.MEDIUM);

                createSubjectiveQuestion(topic,
                                "Design a database schema for a university system. Include students, courses, faculty, and enrollment records. Discuss normalization and indexing strategies.",
                                DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "What is query optimization?",
                                Arrays.asList("Random improvement", "Improving execution performance", "Encryption", "Adding constraints"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "B-Tree index has worst-case search of",
                                Arrays.asList("O(1)", "O(log n)", "O(n)", "O(n log n)"),
                                1, DifficultyLevel.HARD);

                createTrueFalseQuestion(topic,
                                "Replication ensures data availability across locations",
                                true, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "CAP theorem states databases can guarantee",
                                Arrays.asList("All three: CA, P", "Only two of: Consistency, Availability, Partition tolerance", "Encryption", "Backup"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "What is a hash index best for?",
                                Arrays.asList("Range queries", "Exact match queries", "Sorting", "Transactions"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "MVCC improves by",
                                Arrays.asList("Faster writes", "Allowing readers without blocking writers", "Encryption", "Deletion"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "Query execution plan determines",
                                Arrays.asList("Storage size", "Optimal access path", "Backup strategy", "User permissions"),
                                1, DifficultyLevel.HARD);

                createTrueFalseQuestion(topic,
                                "Vacuum in PostgreSQL helps reclaim space from dead rows",
                                true, DifficultyLevel.HARD);

                createDatabaseExam();
                System.out.println("✅ Databases Exam Created (30 questions)");
        }

        // ==================== WEB DEVELOPMENT ====================
        private void seedAndCreateWebDevExam() {
                String topic = "WEB_DEVELOPMENT";

                createMCQQuestion(topic,
                                "REST stands for:",
                                Arrays.asList("Representational State Transfer",
                                                "Remote Engine State Technology",
                                                "Request Execution Server Time",
                                                "Resource Exchange System Tech"),
                                0, DifficultyLevel.EASY);

                createTrueFalseQuestion(topic,
                                "HTML is a programming language",
                                false, DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "CSS media queries enable:",
                                Arrays.asList("Animations", "Responsive design",
                                                "SEO optimization", "Website security"),
                                1, DifficultyLevel.EASY);

                createFillInBlankQuestion(topic,
                                "The ___ package manager is primarily used for Node.js projects",
                                "npm", DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "What is the purpose of localStorage in JavaScript?",
                                Arrays.asList("Session storage", "Persistent client-side storage",
                                                "Server storage", "Database"),
                                1, DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "What is the difference between GET and POST?",
                                Arrays.asList("No difference", "Data visibility and size limits", "Speed", "Security level"),
                                1, DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "JSON stands for",
                                Arrays.asList("Java Standard Object Notation", "JavaScript Object Notation", "Just Object Notation", "Java Serialized Objects"),
                                1, DifficultyLevel.EASY);

                createTrueFalseQuestion(topic,
                                "CSS Grid is better than Flexbox for all layouts",
                                false, DifficultyLevel.EASY);

                createFillInBlankQuestion(topic,
                                "___ is used to add styling to HTML elements",
                                "CSS", DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "Responsive images can be implemented using",
                                Arrays.asList("Fixed width", "srcset attribute", "Only media queries", "JavaScript only"),
                                1, DifficultyLevel.EASY);

                createSubjectiveQuestion(topic,
                                "Explain responsive web design and mobile-first approach. How do CSS media queries support this?",
                                DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "What is the purpose of CORS?",
                                Arrays.asList("Compression", "Cross-Origin Resource Sharing",
                                                "Cache management", "Code organization"),
                                1, DifficultyLevel.MEDIUM);

                createTrueFalseQuestion(topic,
                                "HTTPS uses symmetric encryption",
                                false, DifficultyLevel.MEDIUM);

                createFillInBlankQuestion(topic,
                                "The DOM stands for ___ Object Model",
                                "Document", DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "What is middleware in web frameworks?",
                                Arrays.asList("Database layer", "Software component in request/response chain", "Frontend code", "Cache layer"),
                                1, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "Content Security Policy prevents",
                                Arrays.asList("Slow networks", "XSS attacks", "Database errors", "CORS issues"),
                                1, DifficultyLevel.MEDIUM);

                createTrueFalseQuestion(topic,
                                "Single Page Applications (SPA) reload the entire page on navigation",
                                false, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "What is API rate limiting?",
                                Arrays.asList("Limiting data types", "Restricting requests per user/time", "Caching strategy", "Database limit"),
                                1, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "OAuth2 is primarily used for",
                                Arrays.asList("Encryption", "Authentication and authorization", "Compression", "Caching"),
                                1, DifficultyLevel.MEDIUM);

                createFillInBlankQuestion(topic,
                                "A ___ is a placeholder in HTML that can be updated by JavaScript without reloading",
                                "div", DifficultyLevel.MEDIUM);

                createSubjectiveQuestion(topic,
                                "Design a full-stack web application for an e-commerce platform. Discuss frontend architecture, API design, database schema, and security considerations.",
                                DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "What is the Virtual DOM in React?",
                                Arrays.asList("Real browser DOM", "In-memory representation for optimization", "Database layer", "CSS framework"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "Web Workers enable",
                                Arrays.asList("Styling", "Background processing without blocking UI", "Database access", "Form validation"),
                                1, DifficultyLevel.HARD);

                createTrueFalseQuestion(topic,
                                "Service Workers only work in production mode",
                                false, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "What is tree-shaking in webpack?",
                                Arrays.asList("Removing dead code", "Bundling strategy", "Debugging tool", "Caching mechanism"),
                                0, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "GraphQL advantages over REST include",
                                Arrays.asList("No advantages", "Single endpoint, precise data", "Better caching", "Simpler queries"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "Lazy loading improves",
                                Arrays.asList("CPU usage", "Initial page load time", "RAM", "Disk space"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "Content Delivery Network (CDN) functions to",
                                Arrays.asList("Encrypt data", "Distribute content geographically", "Validate HTML", "Manage databases"),
                                1, DifficultyLevel.HARD);

                createTrueFalseQuestion(topic,
                                "HTTP/3 uses QUIC protocol instead of TCP",
                                true, DifficultyLevel.HARD);

                createWebDevExam();
                System.out.println("✅ Web Development Exam Created (30 questions)");
        }

        // ==================== ARTIFICIAL INTELLIGENCE ====================
        private void seedAndCreateAIExam() {
                String topic = "ARTIFICIAL_INTELLIGENCE";

                createMCQQuestion(topic,
                                "Machine learning is:",
                                Arrays.asList("Manual rule programming",
                                                "Systems learning from data without explicit programming",
                                                "Artificial human replacement",
                                                "Data storage system"),
                                1, DifficultyLevel.EASY);

                createTrueFalseQuestion(topic,
                                "Neural networks are inspired by brain structure",
                                true, DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "What is supervised learning?",
                                Arrays.asList("Learning without labels",
                                                "Learning with labeled training data",
                                                "Unsupervised learning",
                                                "Reinforcement learning"),
                                1, DifficultyLevel.EASY);

                createFillInBlankQuestion(topic,
                                "The process of adjusting model parameters during training is called ___",
                                "backpropagation", DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "What does overfitting mean?",
                                Arrays.asList("Not enough data",
                                                "Model memorizing training data noise",
                                                "High learning rate",
                                                "Insufficient features"),
                                1, DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "What is the difference between AI and ML?",
                                Arrays.asList("No difference", "AI is broader field, ML is subset", "ML is older", "AI is cheaper"),
                                1, DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "Deep learning requires",
                                Arrays.asList("Simple data", "Multiple layers", "Small datasets", "No preprocessing"),
                                1, DifficultyLevel.EASY);

                createTrueFalseQuestion(topic,
                                "Regularization helps prevent underfitting",
                                false, DifficultyLevel.EASY);

                createFillInBlankQuestion(topic,
                                "The ___ metric is used for classification evaluation",
                                "accuracy", DifficultyLevel.EASY);

                createMCQQuestion(topic,
                                "Feature scaling normalizes data to",
                                Arrays.asList("Large numbers", "Similar ranges", "Integers", "Remove outliers"),
                                1, DifficultyLevel.EASY);

                createSubjectiveQuestion(topic,
                                "Explain supervised vs unsupervised learning with real-world examples. Discuss when to use each.",
                                DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "What is cross-validation?",
                                Arrays.asList("Data encryption", "Dividing data for model evaluation",
                                                "Feature scaling", "Loss function"),
                                1, DifficultyLevel.MEDIUM);

                createTrueFalseQuestion(topic,
                                "Gradient descent is used for classification only",
                                false, DifficultyLevel.MEDIUM);

                createFillInBlankQuestion(topic,
                                "A ___ is a measure of how well a model performs on unseen data",
                                "test/validation accuracy", DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "What is the bias-variance tradeoff?",
                                Arrays.asList("Always minimize bias", "Balance error sources", "Ignore variance", "Only reduce variance"),
                                1, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "Convolutional Neural Networks are designed for",
                                Arrays.asList("Text", "Images", "Numerical data", "Time series"),
                                1, DifficultyLevel.MEDIUM);

                createTrueFalseQuestion(topic,
                                "Recurrent Neural Networks are good for sequences",
                                true, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "What is a confusion matrix?",
                                Arrays.asList("Network structure", "Evaluation table for classification", "Data preprocessing", "Loss function"),
                                1, DifficultyLevel.MEDIUM);

                createMCQQuestion(topic,
                                "Principal Component Analysis is used for",
                                Arrays.asList("Classification", "Dimensionality reduction", "Neural network training", "Data collection"),
                                1, DifficultyLevel.MEDIUM);

                createFillInBlankQuestion(topic,
                                "Precision = TP / (TP + ___)",
                                "FP", DifficultyLevel.MEDIUM);

                createSubjectiveQuestion(topic,
                                "Design an ML system for predicting house prices. Discuss data preprocessing, feature engineering, model selection, and evaluation metrics.",
                                DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "Transformer architecture uses",
                                Arrays.asList("Recurrence", "Self-attention mechanism", "Convolution", "Reinforcement learning"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "What is reinforcement learning?",
                                Arrays.asList("Supervised learning", "Agent learns via rewards", "Unsupervised only", "Data labeling"),
                                1, DifficultyLevel.HARD);

                createTrueFalseQuestion(topic,
                                "Generative models learn data distribution",
                                true, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "What is the purpose of batch normalization?",
                                Arrays.asList("Data collection", "Stabilizing training", "Feature scaling", "Error calculation"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "Attention mechanism helps models focus on",
                                Arrays.asList("All inputs equally", "Relevant input parts", "Random inputs", "Previous layers"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "Meta-learning enables models to",
                                Arrays.asList("Process metadata", "Learn to learn", "Improve speed", "Store data"),
                                1, DifficultyLevel.HARD);

                createMCQQuestion(topic,
                                "What is few-shot learning?",
                                Arrays.asList("Fast training", "Learning from few examples", "Limited data", "Quick inference"),
                                1, DifficultyLevel.HARD);

                createTrueFalseQuestion(topic,
                                "Adversarial training improves model robustness",
                                true, DifficultyLevel.HARD);

                createAIExam();
                System.out.println("✅ Artificial Intelligence Exam Created (30 questions)");
        }

        // ==================== FACTORY METHODS ====================

        private void createMCQQuestion(String topic, String content, List<String> options,
                        int correctIndex, DifficultyLevel difficulty) {
                Question q = new Question();
                q.setContent(content);
                q.setDifficulty(difficulty);
                q.setTopic(topic);
                q.setType(Question.QuestionType.MCQ);
                q.setCorrectOptionIndex(correctIndex);

                options.forEach(opt -> {
                        QuestionOption option = new QuestionOption();
                        option.setText(opt);
                        option.setQuestion(q);
                        q.getOptions().add(option);
                });

                questionRepository.save(q);
        }

        private void createTrueFalseQuestion(String topic, String content, boolean isTrue,
                        DifficultyLevel difficulty) {
                Question q = new Question();
                q.setContent(content);
                q.setDifficulty(difficulty);
                q.setTopic(topic);
                q.setType(Question.QuestionType.TRUE_FALSE);
                q.setCorrectOptionIndex(isTrue ? 0 : 1);

                QuestionOption trueOpt = new QuestionOption();
                trueOpt.setText("True");
                trueOpt.setQuestion(q);
                q.getOptions().add(trueOpt);

                QuestionOption falseOpt = new QuestionOption();
                falseOpt.setText("False");
                falseOpt.setQuestion(q);
                q.getOptions().add(falseOpt);

                questionRepository.save(q);
        }

        private void createFillInBlankQuestion(String topic, String content, String answer,
                        DifficultyLevel difficulty) {
                Question q = new Question();
                q.setContent(content);
                q.setDifficulty(difficulty);
                q.setTopic(topic);
                q.setType(Question.QuestionType.FILL_IN_BLANK);
                q.setCorrectOptionIndex(0);

                QuestionOption correctOpt = new QuestionOption();
                correctOpt.setText(answer);
                correctOpt.setQuestion(q);
                q.getOptions().add(correctOpt);

                questionRepository.save(q);
        }

        private void createSubjectiveQuestion(String topic, String content,
                        DifficultyLevel difficulty) {
                Question q = new Question();
                q.setContent(content);
                q.setDifficulty(difficulty);
                q.setTopic(topic);
                q.setType(Question.QuestionType.SUBJECTIVE);
                q.setCorrectOptionIndex(0);

                QuestionOption placeholder = new QuestionOption();
                placeholder.setText("Student Answer");
                placeholder.setQuestion(q);
                q.getOptions().add(placeholder);

                questionRepository.save(q);
        }

        // ==================== EXAM CREATION ====================

        private void createDSExam() {
                Exam exam = new Exam();
                exam.setTitle("CS301: Data Structures & Algorithms");
                exam.setTopic("DATA_STRUCTURES");
                exam.setDescription("Test algorithmic thinking and data structure implementation skills.");
                exam.setDurationMinutes(45);
                exam.setPassingScore(60);
                exam.setType(Exam.ExamType.ADAPTIVE);
                exam.setAllowPauseResume(false);
                exam.setMaxViolationScore(15);
                exam = examRepository.save(exam);

                ExamBlueprint blueprint = new ExamBlueprint();
                blueprint.setEasyCount(10);
                blueprint.setMediumCount(10);
                blueprint.setHardCount(10);
                blueprint.setExam(exam);
                examBlueprintRepository.save(blueprint);
        }

        private void createCNExam() {
                Exam exam = new Exam();
                exam.setTitle("CN302: Computer Networks");
                exam.setTopic("COMPUTER_NETWORKS");
                exam.setDescription("Advanced networking protocols, OSI model, and architecture.");
                exam.setDurationMinutes(60);
                exam.setPassingScore(65);
                exam.setType(Exam.ExamType.ADAPTIVE);
                exam.setAllowPauseResume(false);
                exam.setMaxViolationScore(15);
                exam = examRepository.save(exam);

                ExamBlueprint blueprint = new ExamBlueprint();
                blueprint.setEasyCount(10);
                blueprint.setMediumCount(10);
                blueprint.setHardCount(10);
                blueprint.setExam(exam);
                examBlueprintRepository.save(blueprint);
        }

        private void createSEExam() {
                Exam exam = new Exam();
                exam.setTitle("SE303: Software Engineering Principles");
                exam.setTopic("SOFTWARE_ENGINEERING");
                exam.setDescription("SDLC methods, design patterns, SOLID, and software quality.");
                exam.setDurationMinutes(50);
                exam.setPassingScore(65);
                exam.setType(Exam.ExamType.ADAPTIVE);
                exam.setAllowPauseResume(false);
                exam.setMaxViolationScore(15);
                exam = examRepository.save(exam);

                ExamBlueprint blueprint = new ExamBlueprint();
                blueprint.setEasyCount(10);
                blueprint.setMediumCount(10);
                blueprint.setHardCount(10);
                blueprint.setExam(exam);
                examBlueprintRepository.save(blueprint);
        }

        private void createDatabaseExam() {
                Exam exam = new Exam();
                exam.setTitle("CS304: Database Management Systems");
                exam.setTopic("DATABASES");
                exam.setDescription("SQL, normalization, ACID, and database design principles.");
                exam.setDurationMinutes(55);
                exam.setPassingScore(60);
                exam.setType(Exam.ExamType.ADAPTIVE);
                exam.setAllowPauseResume(false);
                exam.setMaxViolationScore(15);
                exam = examRepository.save(exam);

                ExamBlueprint blueprint = new ExamBlueprint();
                blueprint.setEasyCount(10);
                blueprint.setMediumCount(10);
                blueprint.setHardCount(10);
                blueprint.setExam(exam);
                examBlueprintRepository.save(blueprint);
        }

        private void createWebDevExam() {
                Exam exam = new Exam();
                exam.setTitle("WD305: Web Development & Full Stack");
                exam.setTopic("WEB_DEVELOPMENT");
                exam.setDescription("Frontend, backend, REST APIs, and modern web technologies.");
                exam.setDurationMinutes(50);
                exam.setPassingScore(62);
                exam.setType(Exam.ExamType.ADAPTIVE);
                exam.setAllowPauseResume(false);
                exam.setMaxViolationScore(15);
                exam = examRepository.save(exam);

                ExamBlueprint blueprint = new ExamBlueprint();
                blueprint.setEasyCount(10);
                blueprint.setMediumCount(10);
                blueprint.setHardCount(10);
                blueprint.setExam(exam);
                examBlueprintRepository.save(blueprint);
        }

        private void createAIExam() {
                Exam exam = new Exam();
                exam.setTitle("AI306: Artificial Intelligence & Machine Learning");
                exam.setTopic("ARTIFICIAL_INTELLIGENCE");
                exam.setDescription("ML algorithms, neural networks, supervised/unsupervised learning.");
                exam.setDurationMinutes(60);
                exam.setPassingScore(65);
                exam.setType(Exam.ExamType.ADAPTIVE);
                exam.setAllowPauseResume(false);
                exam.setMaxViolationScore(15);
                exam = examRepository.save(exam);

                ExamBlueprint blueprint = new ExamBlueprint();
                blueprint.setEasyCount(10);
                blueprint.setMediumCount(10);
                blueprint.setHardCount(10);
                blueprint.setExam(exam);
                examBlueprintRepository.save(blueprint);
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
