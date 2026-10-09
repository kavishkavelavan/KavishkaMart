package com.kavishka.kavishkamart.controller;

import com.kavishka.dao.CouponDAO;
import com.kavishka.kavishkamart.dto.ApiResponse;
import com.kavishka.kavishkamart.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CouponController", urlPatterns = {"/api/coupon/validate"})
public class CouponController extends HttpServlet {

    private final CouponDAO couponDAO = new CouponDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        if (code == null || code.trim().isEmpty()) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    ApiResponse.error("INVALID_CODE", "Coupon code cannot be empty"));
            return;
        }

        try {
            int discountPercent = couponDAO.getDiscountPercent(code);
            if (discountPercent > 0) {
                Map<String, Object> data = new HashMap<>();
                data.put("code", code.trim().toUpperCase());
                data.put("discountPercent", discountPercent);
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, ApiResponse.success(data));
            } else {
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                        ApiResponse.error("NOT_FOUND", "Invalid or expired coupon code"));
            }
        } catch (SQLException e) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    ApiResponse.error("DB_ERROR", "Failed to validate coupon: " + e.getMessage()));
        }
    }
}
