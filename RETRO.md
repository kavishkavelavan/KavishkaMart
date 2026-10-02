# Sprint Retrospective Log — KavishkaMart

**Specification Rule (Section 16)**: Retro entry in `RETRO.md` per sprint (one line: what worked, what didn't, one change for the next sprint).

---

### Sprint 1 (Weeks 1 - 2: Architecture & Auth Setup)
- **What Worked**: HikariCP connection pool setup via `AppContextListener` and H2 in-memory DB schema initialization worked seamlessly.
- **What Didn't**: Initial password comparison checks were plain-text before BCrypt integration.
- **Change Implemented**: Enforced `PasswordUtil` hashing with `jBCrypt` across all user registrations and logins.

---

### Sprint 2 (Weeks 3 - 4: Marketplace Catalog & Transactional Cart)
- **What Worked**: Layered MVC structure allowed clean separation between `ProductDAO` JDBC logic and Servlet controllers.
- **What Didn't**: Servlet mapping overriding `@WebServlet` annotations in Jetty caused initial `/cart` 404 routing errors.
- **Change Implemented**: Explicitly declared all servlet mappings inside `web.xml`.

---

### Sprint 3 (Weeks 5 - 6: Wishlist, Reviews & Seller Fulfillment)
- **What Worked**: Wishlist and product review modals integrated cleanly into the Glassmorphic JSP view system.
- **What Didn't**: JSP EL engine attempted to evaluate JS template literal strings (`${p.price.toFixed(2)}`) on the server side causing EL exceptions.
- **Change Implemented**: Converted JavaScript template strings inside JSP files to clean string concatenation (`+`).

---

### Sprint 4 (Weeks 7 - 8: Security Audit & Automated Test Suite)
- **What Worked**: JUnit 5 tests running against embedded H2 instances provided instant regression feedback.
- **What Didn't**: FK integrity violations occurred during test setup when seed products referenced missing user IDs.
- **Change Implemented**: Updated test `@BeforeAll` setup blocks to insert seed user rows before creating test products.

---

### Sprint 5 (Weeks 9 - 11: AI Chatbot, Final Polish & Documentation)
- **What Worked**: AI Chatbot fallback (`MockChatProvider`) allowed offline FAQ responses without requiring external API keys.
- **What Didn't**: Initial JSON request bodies needed handling for both `application/x-www-form-urlencoded` and raw JSON payloads.
- **Change Implemented**: Enhanced `ChatServlet.parseMessage()` to support both form parameters and raw JSON payloads gracefully.
