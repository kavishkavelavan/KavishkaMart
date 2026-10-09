package com.kavishka.kavishkamart.controller;

import com.kavishka.kavishkamart.dto.UserDTO;
import com.kavishka.kavishkamart.model.Role;
import com.kavishka.kavishkamart.util.DbUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AdminController", urlPatterns = {"/admin/dashboard"})
public class AdminController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO currentUser = (session != null) ? (UserDTO) session.getAttribute("currentUser") : null;

        if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try (Connection conn = DbUtil.getConnection()) {
            // Count users
            int userCount = 0;
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM users");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) userCount = rs.getInt(1);
            }

            // Count products
            int productCount = 0;
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM products");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) productCount = rs.getInt(1);
            }

            // Count orders
            int orderCount = 0;
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM orders");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) orderCount = rs.getInt(1);
            }

            // Fetch user list
            List<UserDTO> userList = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, name, email, role, created_at FROM users ORDER BY created_at DESC");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UserDTO user = new UserDTO();
                    user.setId(rs.getLong("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(Role.valueOf(rs.getString("role")));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    userList.add(user);
                }
            }

            req.setAttribute("userCount", userCount);
            req.setAttribute("productCount", productCount);
            req.setAttribute("orderCount", orderCount);
            req.setAttribute("userList", userList);

            req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Failed to load admin stats: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error/500.jsp").forward(req, resp);
        }
    }
}
