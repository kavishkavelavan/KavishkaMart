package com.kavishka.kavishkamart.dao;

import com.kavishka.dao.OrderDAO;
import com.kavishka.kavishkamart.util.DbUtil;
import com.kavishka.model.Order;
import com.kavishka.model.OrderItem;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderDAOTest {

    private HikariDataSource dataSource;
    private OrderDAO orderDAO;

    @BeforeAll
    void setupDataSource() throws Exception {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:mem:testorderdao;DB_CLOSE_DELAY=-1;MODE=MySQL");
        config.setUsername("sa");
        config.setPassword("");
        config.setMaximumPoolSize(5);

        dataSource = new HikariDataSource(config);
        DbUtil.setDataSource(dataSource);
        orderDAO = new OrderDAO();

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

            stmt.execute("INSERT INTO users (id, name, email, password_hash, role) VALUES (1, 'Test Buyer', 'buyer@test.com', 'hash', 'BUYER')");
            stmt.execute("INSERT INTO users (id, name, email, password_hash, role) VALUES (2, 'Test Seller', 'seller@test.com', 'hash', 'SELLER')");
            stmt.execute("INSERT INTO products (id, seller_id, name, description, price, stock_qty, category, image_url) VALUES (1, 2, 'Test Keyboard', 'RGB Keyboard', 49.99, 10, 'Electronics', 'http://example.com/kb.jpg')");
        }
    }

    @AfterAll
    void tearDown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    @DisplayName("Should create order with items and retrieve by buyer ID")
    void testCreateOrderWithItems() throws Exception {
        Order order = new Order();
        order.setBuyerId(1L);
        order.setTotalAmount(new BigDecimal("49.99"));
        order.setStatus("PENDING");

        OrderItem item = new OrderItem();
        item.setProductId(1L);
        item.setQuantity(1);
        item.setUnitPrice(new BigDecimal("49.99"));

        List<OrderItem> items = new ArrayList<>();
        items.add(item);
        order.setItems(items);

        orderDAO.createOrder(order);
        assertTrue(order.getId() > 0);

        List<Order> buyerOrders = orderDAO.findByBuyerId(1L);
        assertFalse(buyerOrders.isEmpty());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    @DisplayName("Should update order status")
    void testUpdateOrderStatus() throws Exception {
        List<Order> buyerOrders = orderDAO.findByBuyerId(1L);
        Order o = buyerOrders.get(0);

        orderDAO.updateOrderStatus(o.getId(), "SHIPPED");

        List<Order> reReadOrders = orderDAO.findByBuyerId(1L);
        assertEquals("SHIPPED", reReadOrders.get(0).getStatus());
    }
}
