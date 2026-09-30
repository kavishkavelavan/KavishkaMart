# KavishkaMart — Final Project Presentation Slide Deck Outline

**Anna University R2025 Semester 3 Project Defense Review**

---

### Slide 1: Title & Project Overview
- **Title**: KavishkaMart — Multi-Seller E-Commerce Marketplace Web Application
- **Student Name / Builder**: Solo Execution
- **Curriculum**: Anna University R2025 Semester 3 Project Window
- **Tech Stack**: Java 17 Servlets, JDBC, HikariCP, H2 RDBMS, JSP/JSTL, Glassmorphism UI

---

### Slide 2: Problem Statement & Objectives
- **Problem**: Single-vendor web apps lack role isolation, multi-tenant inventory control, and AI-assisted shopping support.
- **Objectives**: Build a production-grade multi-seller e-commerce marketplace using Layered MVC over Java Servlets, complete with transactional checkout, review ratings, seller analytics, and AI shopping assistance.

---

### Slide 3: Architecture & Design Patterns
- **Layered MVC Pattern**: Browser -> Filter Layer -> Servlets -> Service Layer -> DAO Layer -> HikariCP Pool -> H2 RDBMS.
- **Design Patterns Used**:
  - **Front Controller**: Servlet dispatcher mapping in `web.xml`.
  - **DAO Pattern**: Complete abstraction of JDBC SQL queries.
  - **Singleton**: HikariCP Connection Pool managed via `DbUtil` & `AppContextListener`.
  - **Factory & Strategy**: Pluggable AI engines (`GeminiChatProvider` & `MockChatProvider`).

---

### Slide 4: Database Schema (3NF) & Entity Relationships
- **Entities**: `USERS`, `PRODUCTS`, `ORDERS`, `ORDER_ITEMS`, `CART_ITEMS`, `REVIEWS`, `COUPONS`.
- **Integrity Constraints**: Foreign key indexes on all relationships, unique email constraints, `DECIMAL(10,2)` for currency values.

---

### Slide 5: Core Marketplace Features (Weeks 1 - 4)
- **Role System**: Buyer, Seller, and Admin access levels with BCrypt password hashing.
- **Product Catalog**: Live keyword search and category filtering across verified seller listings.
- **Shopping Cart & Checkout**: Session cart, promo coupon engine (`WELCOME10`, `KAVISHKA20`, `SUPER50`), atomic DB checkout transactions.

---

### Slide 6: Extended Marketplace Features (Weeks 5 - 6)
- **Buyer Wishlist**: Save-for-later functionality (`/wishlist`).
- **Product Reviews**: 5-star customer rating system with comment feedback modals.
- **Seller Order Fulfillment**: Status tracking (`PENDING` -> `CONFIRMED` -> `SHIPPED` -> `DELIVERED`).
- **Admin Dashboard**: System metrics, user management, listing moderation.

---

### Slide 7: Security Audit & Quality Assurance
- **100% Parameterized Queries**: Every SQL query uses `PreparedStatement`. Zero SQL injection vulnerabilities.
- **XSS Protection**: JSTL output escaping (`<c:out>`) and text escaping on JSP views.
- **Custom Error Handling**: 404 and 500 custom error pages hiding raw stack traces.

---

### Slide 8: Observability & CI/CD Pipeline
- **Health Check Endpoint**: `/api/v1/health` returning JSON `{"status":"UP","db":"UP"}` verifying database connectivity.
- **MDC Request Logging**: Unique UUID request tracking filter (`MdcLoggingFilter.java`).
- **GitHub Actions CI**: Automated `mvn -B clean verify` build pipeline on push/PR.

---

### Slide 9: AI Shopping Assistant Integration (Phase 3)
- **Architecture**: `ChatServlet` mapped to `/api/v1/chat`.
- **Guardrails**: Per-session rate limiting (10 msgs/min), 500-character input validation, and in-memory question caching.
- **UI Widget**: Floating AI assistant button and glassmorphism chat modal on all pages.

---

### Slide 10: Automated Test Results & Demonstration
- **Automated Test Suite**: **16 / 16 Tests Passed (100% Pass Rate)** in Maven.
- **Live Demo Flow**: Buyer Registration -> Product Search -> Add to Cart -> Promo Coupon -> Transactional Checkout -> Seller Fulfillment -> AI Chatbot Interaction.
