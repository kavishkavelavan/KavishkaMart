package com.kavishka.kavishkamart.dao;

import com.kavishka.dao.ProductDAO;
import com.kavishka.kavishkamart.util.DbUtil;
import com.kavishka.model.Product;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductDAOTest {

    private HikariDataSource dataSource;
    private ProductDAO productDAO;

    @BeforeAll
    void setupDataSource() throws Exception {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:mem:testproductdao;DB_CLOSE_DELAY=-1;MODE=MySQL");
        config.setUsername("sa");
        config.setPassword("");
        config.setMaximumPoolSize(5);

        dataSource = new HikariDataSource(config);
        DbUtil.setDataSource(dataSource);
        productDAO = new ProductDAO();

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

            stmt.execute("INSERT INTO users (id, name, email, password_hash, role) VALUES (1, 'Test Seller', 'seller@test.com', 'hash', 'SELLER')");
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
    @DisplayName("Should create product and retrieve by ID")
    void testCreateAndFindById() throws Exception {
        Product product = new Product(1L, "Wireless Mouse", "Ergonomic mouse", 29.99, 50, "Electronics", "http://example.com/mouse.jpg");
        Product created = productDAO.create(product);
        assertTrue(created.getId() > 0);

        Product found = productDAO.findById(created.getId());
        assertNotNull(found);
        assertEquals("Wireless Mouse", found.getName());
        assertEquals(29.99, found.getPrice());
    }

    @Test
    @Order(2)
    @DisplayName("Should search products by keyword and category")
    void testSearchProducts() throws Exception {
        List<Product> results = productDAO.searchProducts("Mouse", "Electronics");
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals("Wireless Mouse", results.get(0).getName());
    }

    @Test
    @Order(3)
    @DisplayName("Should update product details")
    void testUpdateProduct() throws Exception {
        Product found = productDAO.searchProducts("Mouse", "ALL").get(0);
        found.setPrice(24.99);
        found.setStockQty(45);

        boolean updated = productDAO.update(found);
        assertTrue(updated);

        Product reRead = productDAO.findById(found.getId());
        assertEquals(24.99, reRead.getPrice());
        assertEquals(45, reRead.getStockQty());
    }
}
