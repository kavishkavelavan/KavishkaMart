package com.kavishka.service;

import com.kavishka.dao.ProductDAO;
import com.kavishka.model.Product;
import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private final ProductDAO productDAO = new ProductDAO();

    public Product create(Product product) throws SQLException {
        return productDAO.create(product);
    }

    public Product findById(long id) throws SQLException {
        return productDAO.findById(id);
    }

    public List<Product> findAll() throws SQLException {
        return productDAO.findAll();
    }

    public List<Product> findBySellerId(long sellerId) throws SQLException {
        return productDAO.findBySellerId(sellerId);
    }

    public boolean update(Product product) throws SQLException {
        return productDAO.update(product);
    }

    public boolean delete(long productId, long sellerId) throws SQLException {
        return productDAO.delete(productId, sellerId);
    }

    public List<Product> searchProducts(String query, String category) throws SQLException {
        return productDAO.searchProducts(query, category);
    }

    public List<String> getAllCategories() throws SQLException {
        return productDAO.findAllCategories();
    }
}
