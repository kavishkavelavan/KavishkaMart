package com.kavishka.kavishkamart.dao.impl;

import com.kavishka.kavishkamart.dao.UserDAO;
import com.kavishka.kavishkamart.model.Role;
import com.kavishka.kavishkamart.model.User;
import com.kavishka.kavishkamart.util.DbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of UserDAO.
 * Strictly adheres to Mandatory Engineering Rules:
 * 1. PreparedStatement for ALL queries.
 * 2. Try-with-resources for Connection, PreparedStatement, and ResultSet.
 */
public class UserDAOImpl implements UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    private static final String SELECT_BY_EMAIL =
            "SELECT id, name, email, password_hash, role, created_at FROM users WHERE email = ?";

    private static final String SELECT_BY_ID =
            "SELECT id, name, email, password_hash, role, created_at FROM users WHERE id = ?";

    private static final String INSERT_USER =
            "INSERT INTO users (name, email, password_hash, role, created_at) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";

    private static final String UPDATE_USER =
            "UPDATE users SET name = ?, email = ?, password_hash = ?, role = ? WHERE id = ?";

    private static final String SELECT_ALL =
            "SELECT id, name, email, password_hash, role, created_at FROM users ORDER BY created_at DESC";

    private static final String DELETE_BY_ID =
            "DELETE FROM users WHERE id = ?";

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null) return Optional.empty();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_EMAIL)) {

            ps.setString(1, email.trim().toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error executing findByEmail for email: {}", email, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Long id) {
        if (id == null) return Optional.empty();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error executing findById for id: {}", id, e);
        }
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail().trim().toLowerCase());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole().name());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                }
            }
            return user;
        } catch (SQLException e) {
            logger.error("Error executing save for user email: {}", user.getEmail(), e);
            throw new RuntimeException("Database error saving user", e);
        }
    }

    @Override
    public User update(User user) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_USER)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail().trim().toLowerCase());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole().name());
            ps.setLong(5, user.getId());

            ps.executeUpdate();
            return user;
        } catch (SQLException e) {
            logger.error("Error updating user ID: {}", user.getId(), e);
            throw new RuntimeException("Database error updating user", e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all users", e);
        }
        return users;
    }

    @Override
    public boolean deleteById(Long id) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_BY_ID)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting user by ID: {}", id, e);
            return false;
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setRole(Role.fromString(rs.getString("role")));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }
}
