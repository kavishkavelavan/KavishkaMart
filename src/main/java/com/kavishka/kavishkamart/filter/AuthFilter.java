package com.kavishka.kavishkamart.filter;

import com.kavishka.kavishkamart.dto.ApiResponse;
import com.kavishka.kavishkamart.dto.UserDTO;
import com.kavishka.kavishkamart.model.Role;
import com.kavishka.kavishkamart.util.JsonUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filter performing authentication and authorization checks on protected routes.
 * Enforces Spec Section 2 and Section 9 rules.
 */
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        UserDTO currentUser = (session != null) ? (UserDTO) session.getAttribute("currentUser") : null;

        String uri = req.getRequestURI();
        boolean isApiRequest = uri.contains("/api/");

        if (currentUser == null) {
            if (isApiRequest) {
                JsonUtil.sendJsonResponse(res, HttpServletResponse.SC_UNAUTHORIZED,
                        ApiResponse.error("UNAUTHORIZED", "Authentication required to access this resource"));
            } else {
                res.sendRedirect(req.getContextPath() + "/login?redirect=" + req.getRequestURI());
            }
            return;
        }

        // Role-based access control checks
        if (uri.contains("/admin") && currentUser.getRole() != Role.ADMIN) {
            if (isApiRequest) {
                JsonUtil.sendJsonResponse(res, HttpServletResponse.SC_FORBIDDEN,
                        ApiResponse.error("FORBIDDEN", "Admin privilege required"));
            } else {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Admin role required");
            }
            return;
        }

        if (uri.contains("/seller") && (currentUser.getRole() != Role.SELLER && currentUser.getRole() != Role.ADMIN)) {
            if (isApiRequest) {
                JsonUtil.sendJsonResponse(res, HttpServletResponse.SC_FORBIDDEN,
                        ApiResponse.error("FORBIDDEN", "Seller privilege required"));
            } else {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Seller role required");
            }
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
