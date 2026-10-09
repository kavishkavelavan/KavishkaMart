package com.kavishka.service;

import com.kavishka.model.CartItem;
import com.kavishka.model.Product;
import com.kavishka.dao.ProductDAO;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.sql.SQLException;

public class CartService {
    private final ProductDAO productDAO = new ProductDAO();

    @SuppressWarnings("unchecked")
    public Map<Long, CartItem> getCart(HttpSession session) {
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new LinkedHashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    public void addItem(HttpSession session, Long productId, int quantity) {
        Map<Long, CartItem> cart = getCart(session);
        CartItem ci = cart.get(productId);
        if (ci == null) {
            cart.put(productId, new CartItem(productId, quantity));
        } else {
            ci.setQuantity(ci.getQuantity() + quantity);
        }
    }

    public void removeItem(HttpSession session, Long productId) {
        Map<Long, CartItem> cart = getCart(session);
        cart.remove(productId);
    }

    public void clearCart(HttpSession session) {
        session.setAttribute("cart", new LinkedHashMap<Long, CartItem>());
    }

    /** DTO for UI */
    public static class CartItemView {
        private Product product;
        private int quantity;
        public CartItemView(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
        public Product getProduct() { return product; }
        public int getQuantity() { return quantity; }
    }



    public List<CartItemView> listItems(HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        List<CartItemView> list = new ArrayList<>();
        for (CartItem ci : cart.values()) {
            try {
                Product p = productDAO.findById(ci.getProductId());
                if (p != null) {
                    list.add(new CartItemView(p, ci.getQuantity()));
                }
            } catch (SQLException e) {
                // Log and continue; in a real app use a logger
                e.printStackTrace();
            }
        }
        return list;
    }
}
