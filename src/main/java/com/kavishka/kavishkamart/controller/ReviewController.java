package com.kavishka.kavishkamart.controller;

import com.google.gson.Gson;
import com.kavishka.dao.ReviewDAO;
import com.kavishka.kavishkamart.dto.ApiResponse;
import com.kavishka.kavishkamart.dto.UserDTO;
import com.kavishka.kavishkamart.util.JsonUtil;
import com.kavishka.model.Review;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ReviewController", urlPatterns = {"/api/reviews/*"})
public class ReviewController extends HttpServlet {

    private final ReviewDAO reviewDAO = new ReviewDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productIdStr = req.getParameter("productId");
        if (productIdStr == null) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    ApiResponse.error("BAD_REQUEST", "Missing productId parameter"));
            return;
        }

        try {
            long productId = Long.parseLong(productIdStr);
            List<Review> reviews = reviewDAO.findByProductId(productId);
            double avgRating = reviewDAO.getAverageRating(productId);

            Map<String, Object> data = new HashMap<>();
            data.put("reviews", reviews);
            data.put("averageRating", Math.round(avgRating * 10.0) / 10.0);
            data.put("totalReviews", reviews.size());

            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, ApiResponse.success(data));
        } catch (NumberFormatException e) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    ApiResponse.error("INVALID_ID", "Invalid product ID format"));
        } catch (SQLException e) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    ApiResponse.error("DB_ERROR", "Failed to retrieve reviews: " + e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO currentUser = (session != null) ? (UserDTO) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED,
                    ApiResponse.error("UNAUTHORIZED", "Please log in to submit a review"));
            return;
        }

        try {
            long productId = Long.parseLong(req.getParameter("productId"));
            int rating = Integer.parseInt(req.getParameter("rating"));
            String comment = req.getParameter("comment");

            if (rating < 1 || rating > 5) {
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        ApiResponse.error("INVALID_RATING", "Rating must be between 1 and 5 stars"));
                return;
            }

            Review review = new Review(productId, currentUser.getId(), rating, comment);
            reviewDAO.addReview(review);

            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, ApiResponse.success(review));
        } catch (Exception e) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    ApiResponse.error("SUBMIT_FAILED", "Failed to submit review: " + e.getMessage()));
        }
    }
}
