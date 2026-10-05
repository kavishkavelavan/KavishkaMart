# KavishkaMart — Multi-Seller E-Commerce Marketplace Application

> **Anna University R2025, Semester 3 Project Window**  
> **Tech Stack**: Java 17 Servlets, JDBC, HikariCP, H2 Database, JSP/JSTL, Gson, jBCrypt, SLF4J/Logback, JUnit 5, Jetty/Tomcat.  
> **Live Deployment**: 🌐 [https://kavishkamart.onrender.com](https://kavishkamart.onrender.com) | 🟢 [Health Status](https://kavishkamart.onrender.com/api/v1/health)

---

## 📌 Executive Problem Statement

Traditional single-vendor e-commerce web applications restrict multi-tenant catalog management, role-based order fulfillment, and automated real-time buyer-seller interactions. **KavishkaMart** addresses this gap by implementing a production-grade, multi-seller e-commerce marketplace platform following the **Layered Model-View-Controller (MVC) over Servlets Architecture** (Front Controller pattern).

Sellers can register, list products, manage inventory, and track incoming order fulfillment. Buyers can browse verified listings, search by keyword, filter by category, manage a session shopping cart, apply promo coupons, save items to a wishlist, place orders via mock transactional checkout, and submit 5-star customer reviews. Admins oversee marketplace users, catalog listings, and order analytics. An embedded **AI Shopping Assistant** (`/api/v1/chat`) provides real-time customer assistance.

---

## ⚙️ Technology Stack & Dependencies

| Layer | Component | Technology / Library |
| :--- | :--- | :--- |
| **JDK** | Java Runtime | JDK 17 (LTS) |
| **Server** | Servlet Container | Apache Tomcat 9.0.x / Eclipse Jetty 10.0.x |
| **Database** | RDBMS | H2 Database (Server mode & Embedded In-Memory) |
| **Connection Pooling** | Connection Pool | HikariCP (`HikariDataSource` initialized via `ServletContextListener`) |
| **View Layer** | Server Rendering | JSP + JSTL (`c:out`, `fmt:formatNumber`) + Vanilla CSS (Glassmorphism) |
| **Security** | Authentication | Password Hashing via `jBCrypt`, HTTPSession, `AuthFilter` |
| **JSON API** | Serialization | Google Gson |
| **Observability** | Logging & Health | SLF4J + Logback, MDC Request ID Filter, `/api/v1/health` |
| **Testing** | Unit & DAO Tests | JUnit 5 + HikariCP test pools |
| **CI / CD** | Integration Pipeline | GitHub Actions (`.github/workflows/build.yml`) |

---

## 🏛️ System Architecture Diagram

```
Browser (HTML5 / Vanilla JS / Glassmorphism UI + Floating AI Widget)
       │
       │ HTTP Request / AJAX Fetch
       ▼
   [Filter Layer] ──> MdcLoggingFilter ──> EncodingFilter ──> AuthFilter
       │
       ▼
 [Front Controller Layer] ──> Resource Servlets (ProductServlet, OrderServlet, CartApiServlet, ChatServlet...)
       │
       ▼
  [Service Layer] ──> Business Rules, Validation, DTO Mapping, AI Rate Limiting (No JDBC SQL)
       │
       ▼
    [DAO Layer] ──> ProductDAO, OrderDAO, UserDAO, ReviewDAO, WishlistDAO (ALL SQL / PreparedStatements)
       │
       ▼
 [Connection Pool] ──> HikariCP Connection Pool (Owned by AppContextListener)
       │
       ▼
 [H2 RDBMS Engine] ──> Relational Database Schema (users, products, orders, order_items, cart_items, reviews)
```

---

## 📊 Database Schema (ER Diagram)

```
┌───────────────────────────┐         ┌───────────────────────────┐
│           USERS           │         │         PRODUCTS          │
├───────────────────────────┤         ├───────────────────────────┤
│ id (PK)                   │1       *│ id (PK)                   │
│ name                      ├─────────┤ seller_id (FK -> users.id)│
│ email (UNIQUE)            │         │ name                      │
│ password_hash             │         │ description               │
│ role (BUYER|SELLER|ADMIN) │         │ price (DECIMAL 10,2)      │
│ created_at                │         │ stock_qty                 │
└─────────────┬─────────────┘         │ category                  │
              │                       │ image_url                 │
              │                       └─────────────┬─────────────┘
              │ 1                                   │ 1
              │                                     │
              │ *                                   │ *
┌─────────────┴─────────────┐         ┌─────────────┴─────────────┐
│          ORDERS           │         │        ORDER_ITEMS        │
├───────────────────────────┤         ├───────────────────────────┤
│ id (PK)                   │1       *│ id (PK)                   │
│ buyer_id (FK -> users.id) ├─────────┤ order_id (FK -> orders.id)│
│ total_amount              │         │ product_id (FK)           │
│ status (PENDING|SHIPPED)  │         │ quantity                  │
│ created_at                │         │ unit_price                │
└───────────────────────────┘         └───────────────────────────┘
```

---

## 🚀 Quick Start Guide (Local Setup)

### Prerequisites
- JDK 17 installed (`java -version`)
- Apache Maven installed (`mvn -version`)

### Execution Commands

1. **Clone Repository & Build**:
   ```bash
   git clone https://github.com/kavishkavelavan/KavishkaMart.git
   cd KavishkaMart
   mvn clean verify
   ```

2. **Run Local Server**:
   ```bash
   mvn jetty:run
   ```

3. **Access Application**:
   Open browser at: **`http://localhost:8088/`**

---

## 🔑 Test Login Credentials

| Role | Username / Email | Password | Access Rights |
| :--- | :--- | :--- | :--- |
| **Buyer** | `buyer1@kavishkamart.com` *(or `buyer1`)* | `password123` | Browse, Wishlist, Cart, Checkout, Submit Reviews |
| **Seller** | `seller1@kavishkamart.com` *(or `seller1`)* | `password123` | Create/Edit Products, Manage Stock, Seller Orders |
| **Admin** | `admin1@kavishkamart.com` *(or `admin1`)* | `admin123` | Moderate Listings, System Metrics Dashboard |

---

## 🌐 API Contract Reference

| Method | Path | Description | Sample Response Envelope |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/health` | Observability Health Check | `{"status":"UP","db":"UP"}` |
| `POST` | `/api/v1/chat` | AI Shopping Assistant | `{"success":true,"data":{"reply":"..."}}` |
| `GET` | `/api/cart` | List Session Cart Items | `[{"product":{...},"quantity":2}]` |
| `POST` | `/api/cart?action=add` | Add Item to Cart | HTTP 200 OK |
| `GET` | `/api/coupon/validate` | Validate Promo Code | `{"success":true,"data":{"discountPercent":20}}` |
| `GET` | `/api/reviews` | Product Rating & Reviews | `{"success":true,"data":{"averageRating":4.5}}` |
