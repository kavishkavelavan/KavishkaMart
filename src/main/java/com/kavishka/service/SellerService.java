package com.kavishka.service;

import com.kavishka.dao.SellerDAO;
import com.kavishka.model.Seller;
import com.kavishka.util.PasswordUtil;
import com.kavishka.model.Product;
import com.kavishka.service.ProductService;
import java.sql.SQLException;
import java.util.List;

public class SellerService {
    private final SellerDAO sellerDAO = new SellerDAO();
    private final ProductService productService = new ProductService();

    public Seller register(String name, String email, String plainPassword) throws SQLException {
        String hashed = PasswordUtil.hashPassword(plainPassword);
        Seller seller = new Seller(name, email, hashed);
        return sellerDAO.create(seller);
    }

    public Seller login(String email, String plainPassword) throws SQLException {
        Seller seller = sellerDAO.findByEmail(email);
        if (seller != null && PasswordUtil.checkPassword(plainPassword, seller.getPasswordHash())) {
            return seller;
        }
        return null;
    }

    public Seller findById(long id) throws SQLException {
        return sellerDAO.findById(id);
    }

    /**
     * Retrieve all products belonging to a seller.
     */
    public List<Product> findProductsBySellerId(long sellerId) throws SQLException {
        return productService.findBySellerId(sellerId);
    }
}
