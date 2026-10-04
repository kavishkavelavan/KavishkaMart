# Contributing Guide — KavishkaMart

Welcome to the **KavishkaMart** developer onboarding guide. This document outlines setup instructions, coding guidelines, version control rules, and execution workflows.

---

## 🛠️ Environment Prerequisites
- **Java Development Kit (JDK)**: JDK 17 LTS (`java -version`)
- **Build Tool**: Apache Maven 3.8+ (`mvn -version`)
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code with Java Extension Pack

---

## 🚀 Step-by-Step Developer Setup

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/kavishkavelavan/KavishkaMart.git
   cd KavishkaMart
   ```

2. **Build and Run Unit Tests**:
   ```bash
   mvn clean verify
   ```

3. **Start Local Development Server**:
   ```bash
   mvn jetty:run
   ```

4. **Verify Application in Browser**:
   Open **`http://localhost:8088/`** in your browser.

---

## 📜 Coding Standards & Mandatory Rules

### 1. Database Access Rules (Spec Section 2)
- **ALL SQL queries MUST use `PreparedStatement`**. String-concatenated SQL queries are strictly prohibited under any circumstance.
- Always use `try-with-resources` for `Connection`, `PreparedStatement`, and `ResultSet`.
- Connection pooling must be retrieved via `DbUtil.getConnection()`. Do NOT invoke `DriverManager.getConnection()`.

### 2. Architecture & SOLID Principles
- **Controllers (Servlets)**: Restrict to HTTP orchestration, session validation, and view forwarding/JSON output. No SQL logic in servlets.
- **Service Layer**: Business rule validation, DTO mapping, transaction orchestration. No JDBC code in services.
- **DAO Layer**: Pure SQL queries and JDBC result mapping only.

### 3. Git Workflow & Commit Guidelines (Spec Section 8)
- **Branching Model**: `main` is always deployable. Feature branches follow `feature/<feature-name>`.
- **Commit Message Format**: Use Conventional Commits:
  - `feat: add AI chatbot proxy servlet`
  - `fix: resolve cart JSP EL concatenation issue`
  - `test: add ProductDAO integration test`
  - `docs: update API endpoints in README`

---

## 🧪 Testing Guidelines
Execute all automated JUnit 5 unit and DAO tests before submitting pull requests:
```bash
mvn test
```
Confirm all 16 tests pass with 0 failures and 0 errors.
