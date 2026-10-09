package com.kavishka.kavishkamart.controller;

import com.kavishka.kavishkamart.util.DbUtil;
import com.kavishka.kavishkamart.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Observability endpoint verifying database connectivity and system health.
 * Satisfies Spec Section 18 rule 1: GET /api/v1/health returns { "status": "UP", "db": "UP" }.
 */
@WebServlet(name = "HealthServlet", urlPatterns = {"/api/v1/health", "/health", "/api/health", "/healthcheck"})
public class HealthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> healthStatus = new LinkedHashMap<>();
        healthStatus.put("status", "UP");

        boolean dbUp = checkDatabaseHealth();
        healthStatus.put("db", dbUp ? "UP" : "DOWN");

        int statusCode = dbUp ? HttpServletResponse.SC_OK : HttpServletResponse.SC_SERVICE_UNAVAILABLE;
        JsonUtil.sendJsonResponse(response, statusCode, healthStatus);
    }

    private boolean checkDatabaseHealth() {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT 1");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() && rs.getInt(1) == 1;
        } catch (Exception e) {
            return false;
        }
    }
}
