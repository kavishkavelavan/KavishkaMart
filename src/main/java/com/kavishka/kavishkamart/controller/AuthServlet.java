package com.kavishka.kavishkamart.controller;

import com.kavishka.kavishkamart.dao.impl.UserDAOImpl;
import com.kavishka.kavishkamart.dto.ApiResponse;
import com.kavishka.kavishkamart.dto.LoginRequestDTO;
import com.kavishka.kavishkamart.dto.RegisterRequestDTO;
import com.kavishka.kavishkamart.dto.UserDTO;
import com.kavishka.kavishkamart.exception.AuthenticationException;
import com.kavishka.kavishkamart.exception.ValidationException;
import com.kavishka.kavishkamart.service.UserService;
import com.kavishka.kavishkamart.service.impl.UserServiceImpl;
import com.kavishka.kavishkamart.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Servlet handling user authentication (Login, Register, Logout, Session check).
 * Versioned under /api/v1/auth/* for REST and supporting HTML form views for /login & /register.
 */
@WebServlet(name = "AuthServlet", urlPatterns = {"/login", "/register", "/logout", "/api/v1/auth/*"})
public class AuthServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserServiceImpl(new UserDAOImpl());
    }

    // Constructor injection for testing
    public AuthServlet(UserService userService) {
        this.userService = userService;
    }

    public AuthServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        String pathInfo = request.getPathInfo();

        if ("/login".equals(path)) {
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            return;
        } else if ("/register".equals(path)) {
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        } else if ("/logout".equals(path)) {
            handleLogout(request, response, false);
            return;
        }

        if (pathInfo != null) {
            switch (pathInfo) {
                case "/me" -> handleGetCurrentUser(request, response);
                case "/logout" -> handleLogout(request, response, true);
                default -> JsonUtil.sendJsonResponse(response, HttpServletResponse.SC_NOT_FOUND,
                        ApiResponse.error("NOT_FOUND", "Endpoint not found"));
            }
        } else {
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        String pathInfo = request.getPathInfo();

        if ("/api/v1/auth".equals(path) || pathInfo != null) {
            String target = pathInfo != null ? pathInfo : "";
            switch (target) {
                case "/login" -> handleJsonLogin(request, response);
                case "/register" -> handleJsonRegister(request, response);
                case "/logout" -> handleLogout(request, response, true);
                default -> JsonUtil.sendJsonResponse(response, HttpServletResponse.SC_NOT_FOUND,
                        ApiResponse.error("NOT_FOUND", "API endpoint not found"));
            }
            return;
        }

        // HTML form submission fallback
        if ("/login".equals(path)) {
            handleFormLogin(request, response);
        } else if ("/register".equals(path)) {
            handleFormRegister(request, response);
        }
    }

    private void handleJsonLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            LoginRequestDTO dto = parseRequestBody(request, LoginRequestDTO.class);
            UserDTO userDTO = userService.login(dto);

            // Spec Rule #3: Regenerate session ID on login to prevent Session Fixation
            HttpSession session = request.getSession(true);
            request.changeSessionId();
            session.setAttribute("currentUser", userDTO);

            JsonUtil.sendJsonResponse(response, HttpServletResponse.SC_OK, ApiResponse.success(userDTO));
        } catch (ValidationException e) {
            JsonUtil.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                    ApiResponse.error(e.getCode(), e.getMessage(), e.getFieldErrors()));
        } catch (AuthenticationException e) {
            JsonUtil.sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    ApiResponse.error(e.getCode(), e.getMessage()));
        } catch (Exception e) {
            JsonUtil.sendJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    ApiResponse.error("SERVER_ERROR", "An unexpected error occurred during login"));
        }
    }

    private void handleFormLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        LoginRequestDTO dto = new LoginRequestDTO(email, password);

        try {
            UserDTO userDTO = userService.login(dto);
            HttpSession session = request.getSession(true);
            request.changeSessionId();
            session.setAttribute("currentUser", userDTO);

            String redirect = request.getParameter("redirect");
            if (redirect != null && !redirect.trim().isEmpty() && !redirect.contains("\n")) {
                response.sendRedirect(redirect);
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
        } catch (ValidationException | AuthenticationException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }

    private void handleJsonRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            RegisterRequestDTO dto = parseRequestBody(request, RegisterRequestDTO.class);
            UserDTO userDTO = userService.register(dto);

            // Automatically log in user after registration
            HttpSession session = request.getSession(true);
            request.changeSessionId();
            session.setAttribute("currentUser", userDTO);

            JsonUtil.sendJsonResponse(response, HttpServletResponse.SC_CREATED, ApiResponse.success(userDTO));
        } catch (ValidationException e) {
            JsonUtil.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                    ApiResponse.error(e.getCode(), e.getMessage(), e.getFieldErrors()));
        } catch (Exception e) {
            JsonUtil.sendJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    ApiResponse.error("SERVER_ERROR", "An unexpected error occurred during registration"));
        }
    }

    private void handleFormRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                request.getParameter("name"),
                request.getParameter("email"),
                request.getParameter("password"),
                request.getParameter("role")
        );

        try {
            UserDTO userDTO = userService.register(dto);
            HttpSession session = request.getSession(true);
            request.changeSessionId();
            session.setAttribute("currentUser", userDTO);
            response.sendRedirect(request.getContextPath() + "/");
        } catch (ValidationException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("fieldErrors", e.getFieldErrors());
            request.setAttribute("name", dto.getName());
            request.setAttribute("email", dto.getEmail());
            request.setAttribute("role", dto.getRole());
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
        }
    }

    private void handleGetCurrentUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        UserDTO currentUser = (session != null) ? (UserDTO) session.getAttribute("currentUser") : null;

        if (currentUser != null) {
            JsonUtil.sendJsonResponse(response, HttpServletResponse.SC_OK, ApiResponse.success(currentUser));
        } else {
            JsonUtil.sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    ApiResponse.error("UNAUTHORIZED", "No active session found"));
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response, boolean isJson)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        if (isJson) {
            JsonUtil.sendJsonResponse(response, HttpServletResponse.SC_OK,
                    ApiResponse.success("Logged out successfully"));
        } else {
            response.sendRedirect(request.getContextPath() + "/login?logged_out=true");
        }
    }

    private <T> T parseRequestBody(HttpServletRequest request, Class<T> clazz) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return JsonUtil.fromJson(sb.toString(), clazz);
    }
}
