package com.kavishka.kavishkamart.controller;

import com.kavishka.kavishkamart.dto.ApiResponse;
import com.kavishka.kavishkamart.dto.UserDTO;
import com.kavishka.kavishkamart.model.Role;
import com.kavishka.kavishkamart.util.JsonUtil;
import com.kavishka.model.Order;
import com.kavishka.model.Seller;
import com.kavishka.service.OrderService;
import com.kavishka.service.SellerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Handles seller registration, login, dashboard, and order fulfillment.
 */
@WebServlet(name = "SellerController", urlPatterns = {"/seller/register", "/seller/login", "/seller/dashboard", "/seller/orders", "/seller/orders/update"})
public class SellerController extends HttpServlet {
    private final SellerService sellerService = new SellerService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        UserDTO user = (UserDTO) req.getSession().getAttribute("currentUser");

        if (uri.endsWith("/register")) {
            req.getRequestDispatcher("/WEB-INF/views/seller/register.jsp").forward(req, resp);
        } else if (uri.endsWith("/login")) {
            req.getRequestDispatcher("/WEB-INF/views/seller/login.jsp").forward(req, resp);
        } else if (uri.endsWith("/dashboard")) {
            if (user == null || (user.getRole() != Role.SELLER && user.getRole() != Role.ADMIN)) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            try {
                long sellerId = user.getId();
                req.setAttribute("products", sellerService.findProductsBySellerId(sellerId));
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to load dashboard: " + e.getMessage());
                return;
            }
            req.getRequestDispatcher("/WEB-INF/views/seller/dashboard.jsp").forward(req, resp);
        } else if (uri.endsWith("/orders")) {
            if (user == null || (user.getRole() != Role.SELLER && user.getRole() != Role.ADMIN)) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            try {
                List<Order> orders = orderService.getOrdersBySeller(user.getId());
                req.setAttribute("orders", orders);
                req.getRequestDispatcher("/WEB-INF/views/seller/orders.jsp").forward(req, resp);
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load seller orders: " + e.getMessage());
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        UserDTO user = (UserDTO) req.getSession().getAttribute("currentUser");

        if (uri.endsWith("/register")) {
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            try {
                Seller seller = sellerService.register(name, email, password);
                req.getSession(true).setAttribute("currentUser", new UserDTO(seller.getId(), seller.getName(), seller.getEmail(), Role.SELLER, null));
                resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
            } catch (Exception e) {
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ApiResponse.error("REGISTRATION_FAILED", e.getMessage()));
            }
        } else if (uri.endsWith("/login")) {
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            try {
                Seller seller = sellerService.login(email, password);
                if (seller != null) {
                    req.getSession(true).setAttribute("currentUser", new UserDTO(seller.getId(), seller.getName(), seller.getEmail(), Role.SELLER, null));
                    resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
                } else {
                    JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, ApiResponse.error("INVALID_CREDENTIALS", "Login failed"));
                }
            } catch (Exception e) {
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ApiResponse.error("LOGIN_ERROR", e.getMessage()));
            }
        } else if (uri.endsWith("/orders/update")) {
            if (user == null || (user.getRole() != Role.SELLER && user.getRole() != Role.ADMIN)) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            try {
                long orderId = Long.parseLong(req.getParameter("orderId"));
                String status = req.getParameter("status");
                orderService.updateOrderStatus(orderId, status);
                resp.sendRedirect(req.getContextPath() + "/seller/orders");
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to update status: " + e.getMessage());
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
