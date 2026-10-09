package com.kavishka.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private long id;
    private long buyerId;
    private String status; // 'PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED'
    private BigDecimal totalAmount;
    private Timestamp createdAt;
    
    private List<OrderItem> items = new ArrayList<>();

    public Order() {}

    public Order(long buyerId, String status, BigDecimal totalAmount) {
        this.buyerId = buyerId;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getBuyerId() { return buyerId; }
    public void setBuyerId(long buyerId) { this.buyerId = buyerId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    
    public void addItem(OrderItem item) {
        items.add(item);
    }
}
