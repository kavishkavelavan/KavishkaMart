package com.kavishka.service.ai;

import java.util.Locale;

/**
 * Fallback & FAQ Chat Engine (Spec Section 11 & 17).
 * Provides domain-specific e-commerce answers for KavishkaMart.
 */
public class MockChatProvider implements ChatProvider {

    @Override
    public String getReply(String userMessage, String context) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Hello! How can I assist you with your KavishkaMart shopping today?";
        }

        String msg = userMessage.toLowerCase(Locale.ROOT);

        if (msg.contains("shipping") || msg.contains("delivery") || msg.contains("deliver")) {
            return "🚚 **Shipping & Delivery**: KavishkaMart offers standard shipping within 2-4 business days on all verified marketplace orders!";
        } else if (msg.contains("return") || msg.contains("refund") || msg.contains("cancel")) {
            return "↺ **Returns & Refunds**: You can request a hassle-free return or refund within 14 days of delivery through your Buyer Orders panel.";
        } else if (msg.contains("coupon") || msg.contains("discount") || msg.contains("promo")) {
            return "🎟️ **Promo Coupons**: You can use code `WELCOME10` for 10% off, `KAVISHKA20` for 20% off, or `SUPER50` for 50% off during checkout!";
        } else if (msg.contains("seller") || msg.contains("sell") || msg.contains("listing")) {
            return "🏪 **Selling on KavishkaMart**: Registered sellers can list, update, and manage products directly from their Seller Dashboard (/seller/dashboard).";
        } else if (msg.contains("order") || msg.contains("track") || msg.contains("status")) {
            return "📦 **Order Tracking**: You can view all your placed orders and live fulfillment status under Buyer Orders (/buyer/orders).";
        } else if (msg.contains("payment") || msg.contains("card") || msg.contains("pay")) {
            return "💳 **Payment Methods**: KavishkaMart supports secure instant mock checkout with automated receipt generation for all items in your cart.";
        } else if (msg.contains("hello") || msg.contains("hi") || msg.contains("hey")) {
            return "👋 Hello! I am KavishkaMart's AI Shopping Assistant. Ask me about products, order tracking, promo coupons, or seller features!";
        } else {
            return "🤖 I am KavishkaMart AI Assistant. I can help you search products, track orders, apply coupons (`WELCOME10`), or learn about seller listings!";
        }
    }
}
