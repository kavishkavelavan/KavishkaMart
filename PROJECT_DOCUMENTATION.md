# KavishkaMart — Multi-Seller E-Commerce Marketplace
## Comprehensive Project Documentation & Technical Specification
**Anna University R2025 Specification — Complete Weeks 1 to 6 Master Edition**

---

## 1. Executive Summary

**KavishkaMart** is an enterprise-grade multi-seller e-commerce web platform built strictly in accordance with the **Anna University R2025 Semester 3** guidelines. The application enables verified sellers to list products, manage inventory, and fulfill orders, while allowing buyers to discover items, search across categories, submit customer reviews, manage stateful shopping carts, save items to a personal wishlist, apply promotional discount coupons, and execute checkout transactions with real-time atomic inventory deduction.

---

## 2. Technology Stack & Architecture

| Layer | Technology Choice | Compliance Rationale |
| :--- | :--- | :--- |
| **Java Runtime** | Java 17 LTS | Modern LTS Java standard |
| **Build Tool** | Apache Maven 3.x | Standard dependency & lifecycle management |
| **Web Container** | Embedded Eclipse Jetty 10.0.x / Servlet 4.0 API | Fast local dev server without standalone Tomcat requirement |
| **Database** | H2 In-Memory Database (`jdbc:h2:mem:kavishkamart`) | Zero-setup embedded DB with MySQL compatibility mode |
| **Connection Pool**| HikariCP 5.0.x (`AppContextListener`) | High-performance pooled DataSource initialization |
| **Data Access** | Pure JDBC (`PreparedStatement` only) | Zero ORM overhead, strict SQL injection prevention |
| **Security** | jBCrypt (`PasswordUtil`) | Industry-standard BCrypt hashing with salt |
| **Presentation** | JSP 2.3 + JSTL 1.2 + Vanilla Glassmorphism CSS | Responsive UI with modern dark mode aesthetic |

---

## 3. Comprehensive Database Schema (Weeks 1 – 6)

The database schema includes 8 normalized tables with foreign keys and indexes:

- **`users`**: User accounts (email UNIQUE, password_hash, role `BUYER`/`SELLER`/`ADMIN`).
- **`products`**: Catalog items (seller_id FK, price, stock_qty, category, image_url).
- **`orders`**: Buyer orders (buyer_id FK, status `PENDING`/`CONFIRMED`/`SHIPPED`/`DELIVERED`/`CANCELLED`, total_amount).
- **`order_items`**: Purchased order line items (order_id FK, product_id FK, quantity, unit_price).
- **`cart_items`**: Stateful session cart persistence (user_id FK, product_id FK, quantity).
- **`reviews`**: Product ratings & comments (product_id FK, user_id FK, rating 1-5, comment).
- **`coupons`**: Promotional discount codes (code UNIQUE, discount_percent).
- **`wishlist`**: Buyer saved items (user_id FK, product_id FK, UNIQUE constraint).

---

## 4. Full Roadmap & Module Matrix (Weeks 1 – 6)

### Week 1: Foundations, Security & Health
- **Database Connection Pool**: `AppContextListener` initializes HikariCP `DataSource`.
- **BCrypt Authentication**: `PasswordUtil` handles salted password hashing & verification.
- **MDC Logging**: `MdcLoggingFilter` attaches UUID request IDs (`X-Request-ID`).
- **Health Monitoring API**: `/health` and `/api/v1/health` returning JSON `{"status": "UP", "db": "UP"}`.

### Week 2: Seller Management & Product CRUD
- **Product Management**: Sellers create, view, edit, and delete catalog listings.
- **Role Authorization**: Role enum check (`Role.SELLER` or `Role.ADMIN`).

### Week 3: Buyer Shopping Cart & Catalog
- **Session Shopping Cart**: Stateful cart storing items across HTTP requests (`CartService`).
- **REST Cart API**: `/api/cart` endpoint for adding, updating, and removing items.

### Week 4: Order Management & Checkout
- **Transactional Checkout**: `OrderDAO.createOrder()` runs within a single JDBC transaction (`conn.setAutoCommit(false)`), creating orders and deducting inventory atomically.
- **Order Confirmation & History**: Buyer receipt screen (`/buyer/checkout`) and order history list (`/orders`).
- **Admin Panel**: System metrics dashboard (`/admin/dashboard`) showing total users, products, orders, and database status.

### Week 5: Advanced Analytics & Wishlist System
- **Buyer Wishlist**: Saved items page (`/wishlist`) and wishlist management API (`/api/wishlist`).
- **Seller Order Fulfillment**: Sellers manage incoming orders and update order status (`/seller/orders`).

### Week 6: Promotional Coupons, Search & Optimization
- **Coupon & Discount Engine**: Promo code validation API (`/api/coupon/validate`) and live cart savings calculation.
- **Dynamic Search & Filters**: Keyword search bar and category selector on catalog page.
- **Expanded Seed Catalog**: Pre-populated database with 20 diverse items across 6 categories.

---

## 5. Security & Rule Compliance Audit

1. **SQL Injection Defense**: 100% parameterized `PreparedStatement` queries.
2. **Password Security**: BCrypt password hashing with unique salt generation.
3. **Session Fixation Defense**: `request.changeSessionId()` invoked upon user login.
4. **Role-Based Access Control**: `AuthFilter` guards `/seller/*`, `/admin/*`, `/checkout/*`, `/orders/*`, `/wishlist`.

---

## 6. API Reference Table

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/health` | System & Database Health Check | No |
| `POST` | `/api/v1/auth/login` | JSON User Login | No |
| `POST` | `/api/v1/auth/register` | JSON User Registration | No |
| `GET` | `/api/cart` | Get current session shopping cart | No |
| `POST` | `/api/cart?action=add` | Add product to cart | No |
| `POST` | `/api/cart?action=remove` | Remove product from cart | No |
| `GET` | `/api/reviews?productId=X` | Get customer reviews for product | No |
| `POST` | `/api/reviews` | Submit product review & rating | Yes (Buyer) |
| `GET` | `/api/coupon/validate?code=X` | Validate promo code discount | No |
| `POST` | `/api/wishlist` | Add or remove product from wishlist | Yes (Buyer) |

---

## 7. Demo Accounts & Test Credentials

| Role | Email | Password | Access Capabilities |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin@kavishkamart.com` | `Admin123!` | System Dashboard (`/admin/dashboard`), User List, Health Monitoring |
| **Seller 1** | `seller1@kavishkamart.com` | `Seller123!` | Seller Dashboard (`/seller/dashboard`), Product Management, Order Fulfillment (`/seller/orders`) |
| **Seller 2** | `seller2@kavishkamart.com` | `Seller234!` | Seller Dashboard, Apparel catalog listings |
| **Buyer 1** | `buyer1@kavishkamart.com` | `Buyer123!` | Browse Catalog, Shopping Cart, Wishlist (`/wishlist`), Checkout (`/buyer/checkout`), Orders (`/orders`), Apply Coupons (`WELCOME10`, `KAVISHKA20`, `SUPER50`) |

---

## 8. Build & Execution Instructions

```bash
# 1. Compile project
mvn clean compile

# 2. Start embedded Jetty server
mvn jetty:run
```

Access application: **`http://localhost:8088/`**
