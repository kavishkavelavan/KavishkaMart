package com.kavishka.kavishkamart.controller;

import com.kavishka.dao.WishlistDAO;
import com.kavishka.kavishkamart.dto.ApiResponse;
import com.kavishka.kavishkamart.dto.UserDTO;
import com.kavishka.kavishkamart.util.JsonUtil;
import com.kavishka.model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "WishlistController", urlPatterns = {"/wishlist", "/api/wishlist/*"})
public class WishlistController extends HttpServlet {

    private final WishlistDAO wishlistDAO = new WishlistDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO currentUser = (session != null) ? (UserDTO) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            List<Product> wishlist = wishlistDAO.getWishlist(currentUser.getId());
            req.setAttribute("wishlist", wishlist);
            req.getRequestDispatcher("/WEB-INF/views/buyer/wishlist.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("errorMessage", "Failed to load wishlist: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error/500.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO currentUser = (session != null) ? (UserDTO) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED,
                    ApiResponse.error("UNAUTHORIZED", "Please log in to manage your wishlist"));
            return;
        }

        String action = req.getParameter("action");
        String productIdStr = req.getParameter("productId");

        if (productIdStr == null) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    ApiResponse.error("BAD_REQUEST", "Missing productId"));
            return;
        }

        try {
            long productId = Long.parseLong(productIdStr);
            if ("add".equalsIgnoreCase(action)) {
                wishlistDAO.addToWishlist(currentUser.getId(), productId);
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, ApiResponse.success("Added to wishlist"));
            } else if ("remove".equalsIgnoreCase(action)) {
                wishlistDAO.removeFromWishlist(currentUser.getId(), productId);
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, ApiResponse.success("Removed from wishlist"));
            } else {
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ApiResponse.error("INVALID_ACTION", "Unknown action"));
            }
        } catch (Exception e) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ApiResponse.error("ERROR", e.getMessage()));
        }
    }
}
