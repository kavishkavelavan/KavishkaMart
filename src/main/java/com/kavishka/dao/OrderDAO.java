package com.kavishka.dao;

import com.kavishka.kavishkamart.util.DbUtil;
import com.kavishka.model.Order;
import com.kavishka.model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    /**
     * Creates an order and its items in a single transaction.
     */
    public void createOrder(Order order) throws SQLException {
        String insertOrderSQL = "INSERT INTO orders (buyer_id, total_amount, status) VALUES (?, ?, ?)";
        String insertItemSQL = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        String updateStockSQL = "UPDATE products SET stock_qty = stock_qty - ? WHERE id = ? AND stock_qty >= ?";

        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Insert Order
            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setLong(1, order.getBuyerId());
                orderStmt.setBigDecimal(2, order.getTotalAmount());
                orderStmt.setString(3, order.getStatus() != null ? order.getStatus() : "PENDING");
                
                int affectedRows = orderStmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating order failed, no rows affected.");
                }

                try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            }

            // 2. Insert Order Items & Deduct Stock
            try (PreparedStatement itemStmt = conn.prepareStatement(insertItemSQL);
                 PreparedStatement stockStmt = conn.prepareStatement(updateStockSQL)) {
                
                for (OrderItem item : order.getItems()) {
                    // Update stock
                    stockStmt.setInt(1, item.getQuantity());
                    stockStmt.setLong(2, item.getProductId());
                    stockStmt.setInt(3, item.getQuantity());
                    
                    int stockUpdated = stockStmt.executeUpdate();
                    if (stockUpdated == 0) {
                        throw new SQLException("Insufficient stock for product ID: " + item.getProductId());
                    }

                    // Insert item
                    itemStmt.setLong(1, order.getId());
                    itemStmt.setLong(2, item.getProductId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setBigDecimal(4, item.getUnitPrice());
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch();
            }

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException ex) {
                    e.addSuppressed(ex);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    // Ignore close error
                }
            }
        }
    }

    /**
     * Retrieves all orders for a specific buyer.
     */
    public List<Order> findByBuyerId(long buyerId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE buyer_id = ? ORDER BY created_at DESC";
        List<Order> orders = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, buyerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getLong("id"));
                    order.setBuyerId(rs.getLong("buyer_id"));
                    order.setStatus(rs.getString("status"));
                    order.setTotalAmount(rs.getBigDecimal("total_amount"));
                    order.setCreatedAt(rs.getTimestamp("created_at"));
                    orders.add(order);
                }
            }
        }
        for (Order order : orders) {
            order.setItems(getOrderItems(order.getId()));
        }
        return orders;
    }

    public List<OrderItem> getOrderItems(long orderId) throws SQLException {
        String sql = "SELECT oi.*, p.name AS product_name, p.image_url AS product_image " +
                     "FROM order_items oi " +
                     "LEFT JOIN products p ON oi.product_id = p.id " +
                     "WHERE oi.order_id = ?";
        List<OrderItem> items = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setId(rs.getLong("id"));
                    item.setOrderId(rs.getLong("order_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getBigDecimal("unit_price"));
                    item.setProductName(rs.getString("product_name"));
                    item.setProductImage(rs.getString("product_image"));
                    items.add(item);
                }
            }
        }
        return items;
    }

    public List<Order> findOrdersBySellerId(long sellerId) throws SQLException {
        String sql = "SELECT DISTINCT o.* FROM orders o " +
                     "JOIN order_items oi ON o.id = oi.order_id " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "WHERE p.seller_id = ? ORDER BY o.created_at DESC";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, sellerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getLong("id"));
                    order.setBuyerId(rs.getLong("buyer_id"));
                    order.setStatus(rs.getString("status"));
                    order.setTotalAmount(rs.getBigDecimal("total_amount"));
                    order.setCreatedAt(rs.getTimestamp("created_at"));
                    orders.add(order);
                }
            }
        }
        for (Order order : orders) {
            order.setItems(getOrderItems(order.getId()));
        }
        return orders;
    }

    public void updateOrderStatus(long orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setLong(2, orderId);
            stmt.executeUpdate();
        }
    }
}
