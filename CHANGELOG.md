# Changelog — KavishkaMart

All notable changes to this project will be documented in this file following Semantic Versioning (`vX.Y.Z`).

---

## [v1.1.0] — 2026-09-20 (AI Chatbot & Final Release)
### Added
- **AI Chatbot Shopping Assistant**: `ChatServlet` mapped to `/api/v1/chat` with `ChatService` rate-limiting (10 msgs/min), caching, and input validation.
- **Pluggable AI Architecture**: `ChatProvider` interface with `GeminiChatProvider` REST API client and `MockChatProvider` fallback FAQ engine.
- **Floating UI Widget**: Interactive Glassmorphism AI launcher button and chat modal on all pages via `footer.jsp`.
- **Complete Academic Deliverables**: `FINAL_PROJECT_REPORT.md`, `SLIDE_DECK_SUMMARY.md`, `DEMO_SCRIPT.md`, and `RETRO.md`.

---

## [v1.0.0] — 2026-09-20 (Full Build & Security Release)
### Added
- **Security Audit Compliance**: 100% `PreparedStatement` query parameterization, XSS escaping, BCrypt password hashing.
- **Automated Test Suite**: 16 JUnit 5 integration and unit tests passing in Maven.
- **Observability Endpoint**: `/api/v1/health` returning JSON `{"status":"UP","db":"UP"}` verifying database health.
- **CI/CD Integration**: GitHub Actions workflow (`.github/workflows/build.yml`) running `mvn -B clean verify` on JDK 17.

---

## [v0.2.0] — 2026-09-18 (Seller Dashboard & Reviews Release)
### Added
- **Seller Order Fulfillment**: Seller incoming orders management page (`/seller/orders`) with status workflow (`PENDING` -> `CONFIRMED` -> `SHIPPED` -> `DELIVERED`).
- **Buyer Wishlist**: Wishlist save-for-later functionality (`/wishlist`).
- **Customer Reviews**: Star rating system (1-5 stars) and feedback modal on product cards.
- **Admin Dashboard**: System metrics, user counts, and catalog moderation.

---

## [v0.1.0] — 2026-09-10 (MVP Initial Release)
### Added
- **Core E-Commerce Journeys**: User Registration/Login, Product Catalog browsing with Search & Category filters.
- **Cart & Checkout**: Session-based cart, promo coupon engine (`WELCOME10`, `KAVISHKA20`, `SUPER50`), and transactional checkout receipts.
- **Database Initialization**: H2 in-memory DB schema migration and 20-item seed catalog dataset.
