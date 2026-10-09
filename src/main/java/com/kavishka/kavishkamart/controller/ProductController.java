package com.kavishka.kavishkamart.controller;

import com.kavishka.kavishkamart.dto.ApiResponse;
import com.kavishka.kavishkamart.dto.UserDTO;
import com.kavishka.kavishkamart.model.Role;
import com.kavishka.kavishkamart.util.JsonUtil;
import com.kavishka.model.Product;
import com.kavishka.service.ProductService;
import com.kavishka.service.SellerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Handles public product browsing and seller product management.
 */
@WebServlet(name = "ProductController", urlPatterns = {"/products", "/product", "/seller/product/new", "/seller/product/create", "/seller/product/edit", "/seller/product/update", "/seller/product/delete"})
public class ProductController extends HttpServlet {
    private final ProductService productService = new ProductService();
    private final SellerService sellerService = new SellerService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        try {
            if (uri.endsWith("/products")) {
                // Public catalog with search & category filtering
                String q = req.getParameter("q");
                String cat = req.getParameter("category");

                List<Product> products = productService.searchProducts(q, cat);
                List<String> categories = productService.getAllCategories();

                req.setAttribute("products", products);
                req.setAttribute("categories", categories);
                req.setAttribute("searchQuery", q != null ? q : "");
                req.setAttribute("selectedCategory", cat != null ? cat : "ALL");

                req.getRequestDispatcher("/WEB-INF/views/product/list.jsp").forward(req, resp);
                return;
            }
            if (uri.endsWith("/product")) {
                // View product detail, expects id param
                String idStr = req.getParameter("id");
                if (idStr == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing product id");
                    return;
                }
                long id = Long.parseLong(idStr);
                Product p = productService.findById(id);
                if (p == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                req.setAttribute("product", p);
                req.getRequestDispatcher("/WEB-INF/views/product/detail.jsp").forward(req, resp);
                return;
            }
            if (uri.endsWith("/seller/product/new")) {
                // Show create form – seller must be authenticated
                UserDTO user = (UserDTO) req.getSession().getAttribute("currentUser");
                if (user == null || (user.getRole() != Role.SELLER && user.getRole() != Role.ADMIN)) {
                    resp.sendRedirect(req.getContextPath() + "/login");
                    return;
                }
                req.getRequestDispatcher("/WEB-INF/views/product/new.jsp").forward(req, resp);
                return;
            }
            if (uri.endsWith("/seller/product/edit")) {
                UserDTO user = (UserDTO) req.getSession().getAttribute("currentUser");
                if (user == null || (user.getRole() != Role.SELLER && user.getRole() != Role.ADMIN)) {
                    resp.sendRedirect(req.getContextPath() + "/login");
                    return;
                }
                String idStr = req.getParameter("id");
                if (idStr == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing product id");
                    return;
                }
                long id = Long.parseLong(idStr);
                Product p = productService.findById(id);
                if (p == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                // Ensure ownership unless admin
                if (user.getRole() != Role.ADMIN && p.getSellerId() != user.getId()) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                req.setAttribute("product", p);
                req.getRequestDispatcher("/WEB-INF/views/product/edit.jsp").forward(req, resp);
                return;
            }
        } catch (SQLException e) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ApiResponse.error("DB_ERROR", e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        UserDTO user = (UserDTO) req.getSession().getAttribute("currentUser");
        if (user == null || (user.getRole() != Role.SELLER && user.getRole() != Role.ADMIN)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            if (uri.endsWith("/seller/product/create")) {
                // Process new product form
                Product p = new Product();
                p.setSellerId(user.getId());
                p.setName(req.getParameter("name"));
                p.setDescription(req.getParameter("description"));
                p.setPrice(Double.parseDouble(req.getParameter("price")));
                p.setStockQty(Integer.parseInt(req.getParameter("stockQty")));
                p.setCategory(req.getParameter("category"));
                p.setImageUrl(req.getParameter("imageUrl"));
                productService.create(p);
                resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
                return;
            }
            if (uri.endsWith("/seller/product/update")) {
                long id = Long.parseLong(req.getParameter("id"));
                Product p = productService.findById(id);
                if (p == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                if (user.getRole() != Role.ADMIN && p.getSellerId() != user.getId()) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                // Update fields
                p.setName(req.getParameter("name"));
                p.setDescription(req.getParameter("description"));
                p.setPrice(Double.parseDouble(req.getParameter("price")));
                p.setStockQty(Integer.parseInt(req.getParameter("stockQty")));
                p.setCategory(req.getParameter("category"));
                p.setImageUrl(req.getParameter("imageUrl"));
                productService.update(p);
                resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
                return;
            }
            if (uri.endsWith("/seller/product/delete")) {
                long id = Long.parseLong(req.getParameter("id"));
                Product p = productService.findById(id);
                if (p == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                if (user.getRole() != Role.ADMIN && p.getSellerId() != user.getId()) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                productService.delete(id, p.getSellerId());
                resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
                return;
            }
        } catch (SQLException e) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ApiResponse.error("DB_ERROR", e.getMessage()));
        }
    }
}
