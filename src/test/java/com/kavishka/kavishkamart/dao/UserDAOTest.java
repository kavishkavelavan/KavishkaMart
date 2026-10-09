package com.kavishka.kavishkamart.dao;

import com.kavishka.kavishkamart.dao.impl.UserDAOImpl;
import com.kavishka.kavishkamart.model.Role;
import com.kavishka.kavishkamart.model.User;
import com.kavishka.kavishkamart.util.DbUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserDAO executing against embedded H2 in-memory instance.
 * Satisfies Spec Section 9 Testing Requirements.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDAOTest {

    private HikariDataSource dataSource;
    private UserDAO userDAO;

    @BeforeAll
    void setupDataSource() throws Exception {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:mem:testuserdao;DB_CLOSE_DELAY=-1;MODE=MySQL");
        config.setUsername("sa");
        config.setPassword("");
        config.setMaximumPoolSize(5);

        dataSource = new HikariDataSource(config);
        DbUtil.setDataSource(dataSource);
        userDAO = new UserDAOImpl();

        // Initialize Schema
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            InputStream is = getClass().getClassLoader().getResourceAsStream("db/schema.sql");
            assertNotNull(is, "schema.sql must be present on classpath");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder sql = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("--")) continue;
                    sql.append(line).append("\n");
                    if (line.endsWith(";")) {
                        stmt.execute(sql.toString().replace(";", "").trim());
                        sql.setLength(0);
                    }
                }
            }
        }
    }

    @AfterAll
    void tearDown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    @Order(1)
    @DisplayName("Should save a new user and retrieve by email")
    void testSaveAndFindByEmail() {
        User user = new User();
        user.setName("Test Buyer");
        user.setEmail("testbuyer@kavishkamart.com");
        user.setPasswordHash("$2a$10$abcdefghijklmnopqrstuu");
        user.setRole(Role.BUYER);

        User saved = userDAO.save(user);
        assertNotNull(saved.getId());

        Optional<User> found = userDAO.findByEmail("testbuyer@kavishkamart.com");
        assertTrue(found.isPresent());
        assertEquals("Test Buyer", found.get().getName());
        assertEquals(Role.BUYER, found.get().getRole());
    }

    @Test
    @Order(2)
    @DisplayName("Should retrieve user by ID")
    void testFindById() {
        User user = new User();
        user.setName("Test Seller");
        user.setEmail("testseller@kavishkamart.com");
        user.setPasswordHash("$2a$10$abcdefghijklmnopqrstuu");
        user.setRole(Role.SELLER);

        User saved = userDAO.save(user);

        Optional<User> found = userDAO.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Test Seller", found.get().getName());
        assertEquals(Role.SELLER, found.get().getRole());
    }

    @Test
    @Order(3)
    @DisplayName("Should list all saved users")
    void testFindAll() {
        List<User> users = userDAO.findAll();
        assertNotNull(users);
        assertTrue(users.size() >= 2);
    }
}
