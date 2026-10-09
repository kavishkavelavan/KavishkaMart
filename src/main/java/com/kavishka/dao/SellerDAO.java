package com.kavishka.dao;

import com.kavishka.model.Seller;
import com.kavishka.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SellerDAO {

    private static final String INSERT_SQL = "INSERT INTO users (name, email, password_hash, role) VALUES (?, ?, ?, 'SELLER')";
    private static final String SELECT_BY_EMAIL_SQL = "SELECT * FROM users WHERE email = ? AND role = 'SELLER'";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM users WHERE id = ? AND role = 'SELLER'";

    public Seller create(Seller seller) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, seller.getName());
            stmt.setString(2, seller.getEmail());
            stmt.setString(3, seller.getPasswordHash());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    seller.setId(rs.getLong(1));
                }
            }
            return seller;
        }
    }

    public Seller findByEmail(String email) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_EMAIL_SQL)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    public Seller findById(long id) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    private Seller mapRow(ResultSet rs) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getLong("id"));
        seller.setName(rs.getString("name"));
        seller.setEmail(rs.getString("email"));
        seller.setPasswordHash(rs.getString("password_hash"));
        seller.setRole(rs.getString("role"));
        return seller;
    }
}
