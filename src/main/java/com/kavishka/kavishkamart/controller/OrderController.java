package com.kavishka.kavishkamart.controller;

import com.kavishka.kavishkamart.dto.UserDTO;
import com.kavishka.model.CartItem;
import com.kavishka.model.Order;
import com.kavishka.service.CartService;
import com.kavishka.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "OrderController", urlPatterns = {"/buyer/checkout", "/buyer/orders", "/checkout", "/orders"})
public class OrderController extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession(false);
        UserDTO currentUser = (session != null) ? (UserDTO) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if ("/buyer/orders".equals(path) || "/orders".equals(path)) {
            try {
                List<Order> orders = orderService.getOrdersByBuyer(currentUser.getId());
                req.setAttribute("orders", orders);
                req.getRequestDispatcher("/WEB-INF/views/buyer/orders.jsp").forward(req, resp);
            } catch (SQLException e) {
                req.setAttribute("errorMessage", "Failed to load orders: " + e.getMessage());
                req.getRequestDispatcher("/WEB-INF/views/error/500.jsp").forward(req, resp);
            }
        } else if ("/buyer/checkout".equals(path) || "/checkout".equals(path)) {
            // Forward to checkout summary or success page if accessed directly
            req.getRequestDispatcher("/WEB-INF/views/buyer/checkout_success.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession(false);
        UserDTO currentUser = (session != null) ? (UserDTO) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if ("/buyer/checkout".equals(path) || "/checkout".equals(path)) {
            Map<Long, CartItem> cart = cartService.getCart(req.getSession());
            if (cart.isEmpty()) {
                req.setAttribute("errorMessage", "Your cart is empty.");
                resp.sendRedirect(req.getContextPath() + "/seller/cart");
                return;
            }

            try {
                Order order = orderService.checkout(currentUser.getId(), cart);
                // Clear cart on successful checkout
                cartService.clearCart(req.getSession());

                req.setAttribute("order", order);
                req.getRequestDispatcher("/WEB-INF/views/buyer/checkout_success.jsp").forward(req, resp);
            } catch (IllegalArgumentException | IllegalStateException e) {
                req.setAttribute("errorMessage", e.getMessage());
                req.getRequestDispatcher("/WEB-INF/views/buyer/cart.jsp").forward(req, resp);
            } catch (SQLException e) {
                req.setAttribute("errorMessage", "Database transaction failed during checkout: " + e.getMessage());
                req.getRequestDispatcher("/WEB-INF/views/error/500.jsp").forward(req, resp);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
