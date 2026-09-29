# KavishkaMart — Final Academic Project Submission Report

**Course**: Java Servlets, JDBC & Apache Tomcat Web Development  
**Curriculum**: Anna University R2025, Semester 3  
**Project Title**: KavishkaMart — Multi-Seller E-Commerce Marketplace Web Application  
**Version**: `v1.1.0`  

---

## 1. Abstract & Executive Problem Statement

Traditional single-tenant e-commerce web applications lack role-isolated multi-vendor listing management, atomic transaction processing, and AI-assisted shopping support. **KavishkaMart** is a production-grade multi-seller marketplace built using Java 17 Servlets, JDBC, HikariCP connection pooling, H2 relational database, JSP/JSTL views, and Vanilla CSS Glassmorphism design system.

The application allows **Sellers** to manage product listings and fulfill orders; **Buyers** to search products, manage wishlists, maintain a session cart, apply promo coupons, place orders via atomic transactions, and submit verified 5-star customer reviews; and **Admins** to oversee users, listings, and marketplace sales analytics. An embedded **AI Shopping Assistant** (`/api/v1/chat`) provides automated customer assistance with rate-limiting and in-memory session caching.

---

## 2. System Architecture & Layered MVC Pattern

KavishkaMart implements the **Layered Model-View-Controller (MVC)** pattern over Java Servlets (Front Controller pattern):

1. **View Layer**: Server-side JSPs (`index.jsp`, `list.jsp`, `cart.jsp`, `wishlist.jsp`, `orders.jsp`, `dashboard.jsp`) using JSTL taglibs (`<c:out>`, `<c:if>`, `<c:forEach>`) and client-side Vanilla JavaScript (`fetch()` AJAX endpoints).
2. **Filter Layer**:
   - `MdcLoggingFilter`: Attaches unique UUID request IDs to SLF4J MDC for logging traceability.
   - `EncodingFilter`: Enforces UTF-8 character encoding across requests and responses.
   - `AuthFilter`: Enforces HTTPSession role authorization on protected routes (`/seller/*`, `/admin/*`, `/buyer/*`).
3. **Controller Layer (Servlets)**: Lightweight HTTP handlers (`ProductController`, `OrderController`, `CartPageServlet`, `CartApiServlet`, `WishlistController`, `ReviewController`, `CouponController`, `HealthServlet`, `ChatServlet`) delegating business logic to services.
4. **Service Layer**: Pure Java orchestration classes (`AuthService`, `UserService`, `CartService`, `OrderService`, `ChatService`) containing business rules, rate limiting, and DTO mappings without raw SQL.
5. **DAO Layer**: Pure JDBC data access abstraction (`UserDAO`, `ProductDAO`, `OrderDAO`, `WishlistDAO`, `ReviewDAO`, `CouponDAO`) executing parameterized queries via `PreparedStatement`.
6. **Connection Pool & RDBMS**: HikariCP connection pool owned by `AppContextListener` managing connections to the H2 database engine (`jdbc:h2:mem:kavishkamart`).

---

## 3. Database Schema & ER Specifications

The database schema strictly adheres to third normal form (3NF), foreign key indexing, and currency accuracy rules (`DECIMAL(10,2)`):

- **USERS**: `id` (PK), `name`, `email` (UNIQUE), `password_hash`, `role` (`BUYER`, `SELLER`, `ADMIN`), `created_at`.
- **PRODUCTS**: `id` (PK), `seller_id` (FK -> `users.id`), `name`, `description`, `price` (DECIMAL 10,2), `stock_qty`, `category`, `image_url`, `created_at`.
- **ORDERS**: `id` (PK), `buyer_id` (FK -> `users.id`), `status` (`PENDING`, `CONFIRMED`, `SHIPPED`, `DELIVERED`, `CANCELLED`), `total_amount` (DECIMAL 10,2), `created_at`.
- **ORDER_ITEMS**: `id` (PK), `order_id` (FK -> `orders.id`), `product_id` (FK -> `products.id`), `quantity`, `unit_price` (DECIMAL 10,2).
- **CART_ITEMS**: `id` (PK), `user_id` (FK -> `users.id`), `product_id` (FK -> `products.id`), `quantity`.
- **REVIEWS**: `id` (PK), `product_id` (FK -> `products.id`), `user_id` (FK -> `users.id`), `rating` (1-5), `comment`, `created_at`.
- **COUPONS**: `id` (PK), `code` (UNIQUE), `discount_percent`, `active`.

---

## 4. Software Design Patterns Implemented

1. **Front Controller Pattern**: Centralized resource Servlets mapped in `web.xml` dispatching view requests.
2. **Data Access Object (DAO) Pattern**: Decoupled SQL data access layer abstracts database interactions from services.
3. **Singleton Pattern**: Connection pool instance lifecycle managed centrally via `DbUtil` and `AppContextListener`.
4. **Factory Pattern**: Dynamic instantiation of `ChatProvider` implementations (`GeminiChatProvider` vs `MockChatProvider`).
5. **Strategy Pattern**: Configurable AI chatbot engines selected via configuration (`ai.chatbot.provider=gemini|mock`).
6. **Builder / DTO Pattern**: Structuring JSON API response envelopes (`UserDTO`, `CartItemView`, `ReviewResponse`).

---

## 5. Security & Observability Audit

- **SQL Injection Prevention**: 100% of SQL queries across all DAOs use parameterized `PreparedStatement`. Zero string-concatenated SQL queries exist in the codebase.
- **Password Security**: Passwords hashed using `jBCrypt` salt hashing algorithm. No plaintext passwords stored.
- **Session Security**: Session ID regenerated on login; 30-minute timeout configured in `web.xml`.
- **XSS & Output Escaping**: HTML output rendered with `<c:out>` and explicit text escaping.
- **Custom Error Pages**: 404 and 500 error pages hide raw Java stack traces from end users.
- **Observability**: `/api/v1/health` endpoint returning `{"status":"UP","db":"UP"}`; MDC request IDs attached to SLF4J logs.

---

## 6. Automated Testing Verification

Unit and DAO tests execute against an embedded H2 test database (`jdbc:h2:mem:test;DB_CLOSE_DELAY=-1`):

- `UserDAOTest`: Save, find by email, find by ID, list all.
- `ProductDAOTest`: Create, search by category/keyword, update stock.
- `OrderDAOTest`: Transactional order creation, find by buyer, update order status.
- `AuthServiceTest`: BCrypt hashing verification, DTO encapsulation.
- `UserServiceTest`: Registration validation, role assignment.

**Result**: **16 / 16 Tests Passed (100% Pass Rate)** on `mvn clean verify`.

---

## 7. Technical Decisions & Known Limitations

### Technical Decisions
1. **HikariCP over DriverManager**: Provides high-concurrency connection pooling and connection leak protection.
2. **Pluggable AI Architecture**: Enables offline FAQ chatbot operation when external LLM API keys are unconfigured.
3. **Vanilla CSS Glassmorphism**: High-contrast, dark-mode design system avoiding bloated UI frameworks.

### Known Limitations
1. **Mock Payment Step**: Payment confirmation step uses instant simulated verification rather than live banking APIs (per specification constraints).
2. **In-Memory H2 DB**: Data resets on full server restart unless configured with file-backed storage (`jdbc:h2:file:./data/kavishkamart`).
