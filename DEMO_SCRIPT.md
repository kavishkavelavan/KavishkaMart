# KavishkaMart — 3-Minute Rehearsed Demonstration Script

**Target Audience**: Anna University R2025 Project Evaluator / Viva Examiner  
**Estimated Time**: 3 Minutes  
**Live Application URL**: `http://localhost:8088/`  

---

## ⏱️ Timeline & Step-by-Step Script

### 0:00 – 0:30 | Introduction & Architecture Overview
- **Action**: Open browser at `http://localhost:8088/`. Point out the modern Glassmorphism dark-mode UI.
- **Script**: *"Good morning/afternoon. This is KavishkaMart, a multi-seller e-commerce marketplace built using Java 17 Servlets, JDBC, HikariCP connection pooling, H2 relational database, and JSP/JSTL views following the Layered MVC pattern over Servlets."*

### 0:30 – 1:15 | Buyer Catalog Search, Wishlist & Cart Checkout
- **Action**: 
  1. Click search box, type `Wireless`, select `Electronics` category, click **Filter**.
  2. Click **Add to Wishlist** on a product card, then navigate to `/wishlist`.
  3. Click **Log In**, login as Buyer (`buyer1@kavishkamart.com` / `password123`).
  4. Click **Add to Cart** on a product. Navigate to `/cart`.
  5. Enter promo code `WELCOME10` in the coupon box and click **Apply** (observe 10% discount calculation).
  6. Click **Place Order (Checkout)**. Show the generated order receipt page.
- **Script**: *"As a buyer, I can search products by keyword or filter by category. Clicking 'Add to Cart' updates the session shopping cart. Applying coupon code WELCOME10 gives an instant 10% discount. Placing an order executes an atomic DB transaction that deducts inventory stock and creates an order receipt."*

### 1:15 – 1:50 | Product Reviews & Seller Order Fulfillment
- **Action**:
  1. On the product catalog, click **Reviews** on a product. Open the star rating modal, select 5 stars, type *"Excellent build quality!"*, and click **Submit Review**. Show the instantly appended feedback.
  2. Log out, then log in as Seller (`seller1@kavishkamart.com` / `password123`).
  3. Navigate to **Seller Orders** (`/seller/orders`). Locate the incoming order, click **Update Status** to change status from `PENDING` to `SHIPPED`.
- **Script**: *"Buyers can leave verified 5-star reviews on products. Switching to the Seller account, the seller can view incoming orders for their products and update the fulfillment status through the order workflow."*

### 1:50 – 2:25 | Admin Panel, Health Check & Observability
- **Action**:
  1. Log in as Admin (`admin1@kavishkamart.com` / `admin123`). Open **Admin Dashboard** (`/admin/dashboard`). Show user counts, order volume, and catalog moderation buttons.
  2. Open a new tab to `http://localhost:8088/api/v1/health`. Show JSON response: `{"status":"UP","db":"UP"}`.
- **Script**: *"The Admin panel provides central oversight over all marketplace users, listings, and total sales volume. For observability, the /api/v1/health endpoint verifies real-time database connectivity and HikariCP pool health."*

### 2:25 – 3:00 | AI Shopping Assistant & Automated Test Verification
- **Action**:
  1. Click the floating robot icon at the bottom-right of the screen to open the **AI Chatbot Widget**.
  2. Click the quick button **🚚 Shipping** (or type *"What promo coupons are available?"*). Observe the instant AI reply.
  3. Show terminal execution of `mvn clean verify` displaying **16 / 16 Tests Passed**.
- **Script**: *"Finally, our AI Shopping Assistant widget provides instant customer assistance with rate-limiting and in-memory session caching. All 16 automated JUnit 5 tests pass cleanly in Maven, completing our full project lifecycle."*
