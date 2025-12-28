# 📄 Document Management Service (REST API)

A robust, production-ready **Document Management System** built with **Spring Boot**. This project demonstrates a clean, layered architecture for managing documents with automatic tagging and an **advanced dynamic search** engine using JPA Specifications.

The project is designed with a strong focus on **Clean Code principles**, **Test-Driven Development (TDD)** concepts, and comprehensive test coverage.

---

## 🛠 Tech Stack

* **Java 17** - Core language.
* **Spring Boot 3.4.1** - Main framework.
* **Spring Data JPA** - ORM and data persistence.
* **PostgreSQL** - Production database.
* **H2 Database** - In-memory database for high-performance integration testing.
* **JUnit 5 & Mockito** - Unit and Integration testing framework.
* **Docker & Docker Compose** - Containerization.
* **Lombok** - Boilerplate code reduction.

---

## ✨ Key Features

### 1. Document Lifecycle Management
* Create documents with Title, Content, and Tags.
* **Intelligent Tagging:** Implements a Many-to-Many relationship. The system automatically checks if a tag exists; if not, it creates a new one on the fly.

### 2. 🔍 Advanced Dynamic Search (JPA Criteria API)
Unlike simple repository methods, this project implements the **Specification Pattern** to allow flexible filtering.
* **Dynamic Filtering:** Users can search by `Title`, `Content`, `Tags`, or `All` fields simultaneously.
* **Type-Safe Queries:** Utilizes `CriteriaBuilder` to construct SQL queries programmatically, avoiding raw SQL strings.
* **Smart Joins:** Optimizes performance using `INNER JOIN` for specific tag searches and `LEFT JOIN` for comprehensive searches to handle empty associations correctly.

### 3. 🧪 Comprehensive Testing Strategy
The project features a robust test suite covering all architectural layers:
* **Repository Layer (`@DataJpaTest`):** Verifies custom JPQL and Criteria API queries using an H2 in-memory database.
* **Service Layer (Unit Tests):** Tests business logic in total isolation using **Mockito** (Mocking Repositories).
* **Controller Layer (`@WebMvcTest`):** Validates REST endpoints, HTTP status codes, and JSON serialization using `MockMvc`.

---

## 🚀 Getting Started

### Prerequisites
* JDK 17 or higher
* Maven (Wrapper included)
* Docker (Optional, for running PostgreSQL locally)

### 1. Clone the Repository
```bash
git clone [https://github.com/mobinaheidari/document-management-service.git](https://github.com/mobinaheidari/document-management-service.git)
cd document-management-service