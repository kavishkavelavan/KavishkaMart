package com.kavishka.service;

import com.kavishka.dao.OrderDAO;
import com.kavishka.dao.ProductDAO;
import com.kavishka.model.CartItem;
import com.kavishka.model.Order;
import com.kavishka.model.OrderItem;
import com.kavishka.model.Product;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OrderService {

    private final OrderDAO orderDAO = new OrderDAO();
    private final ProductDAO productDAO = new ProductDAO();

    public Order checkout(long buyerId, Map<Long, CartItem> cart) throws SQLException {
        if (cart == null || cart.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        Order order = new Order();
        order.setBuyerId(buyerId);
        order.setStatus("PENDING");

        for (CartItem ci : cart.values()) {
            Product p = productDAO.findById(ci.getProductId());
            if (p == null) {
                throw new IllegalArgumentException("Product not found: " + ci.getProductId());
            }

            if (p.getStockQty() < ci.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + p.getName());
            }

            BigDecimal unitPrice = BigDecimal.valueOf(p.getPrice());
            BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(ci.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(p.getId());
            orderItem.setQuantity(ci.getQuantity());
            orderItem.setUnitPrice(unitPrice);
            orderItem.setProductName(p.getName());
            orderItem.setProductImage(p.getImageUrl());
            
            order.addItem(orderItem);
        }

        order.setTotalAmount(totalAmount);
        
        // Save order and deduct inventory atomically via OrderDAO
        orderDAO.createOrder(order);

        return order;
    }

    public List<Order> getOrdersByBuyer(long buyerId) throws SQLException {
        return orderDAO.findByBuyerId(buyerId);
    }

    public List<Order> getOrdersBySeller(long sellerId) throws SQLException {
        return orderDAO.findOrdersBySellerId(sellerId);
    }

    public void updateOrderStatus(long orderId, String status) throws SQLException {
        orderDAO.updateOrderStatus(orderId, status);
    }
}
