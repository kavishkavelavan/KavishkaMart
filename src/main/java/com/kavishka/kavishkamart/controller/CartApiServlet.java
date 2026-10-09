package com.kavishka.kavishkamart.controller;

import com.google.gson.Gson;
import com.kavishka.service.CartService;
import com.kavishka.service.CartService.CartItemView;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CartApiServlet", urlPatterns = {"/api/cart/*"})
public class CartApiServlet extends HttpServlet {
    private final CartService cartService = new CartService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || "/".equals(path) || "".equals(path)) {
            List<CartItemView> items = cartService.listItems(req.getSession());
            writeJson(resp, items);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action");
            return;
        }
        switch (action) {
            case "add":
                handleAdd(req, resp);
                break;
            case "remove":
                handleRemove(req, resp);
                break;
            case "clear":
                cartService.clearCart(req.getSession());
                resp.setStatus(HttpServletResponse.SC_OK);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long productId = parseLong(req.getParameter("productId"));
        int qty = parseInt(req.getParameter("quantity"), 1);
        if (productId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid productId");
            return;
        }
        cartService.addItem(req.getSession(), productId, qty);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void handleRemove(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long productId = parseLong(req.getParameter("productId"));
        if (productId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid productId");
            return;
        }
        cartService.removeItem(req.getSession(), productId);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private Long parseLong(String s) {
        try { return Long.valueOf(s); } catch (Exception e) { return null; }
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }

    private void writeJson(HttpServletResponse resp, Object obj) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(obj));
    }
}
