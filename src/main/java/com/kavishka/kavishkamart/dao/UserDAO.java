package com.kavishka.kavishkamart.dao;

import com.kavishka.kavishkamart.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for User database operations.
 */
public interface UserDAO {

    /**
     * Find a user entity by unique email address.
     *
     * @param email User email address
     * @return Optional containing User if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Find a user entity by primary key ID.
     *
     * @param id User ID
     * @return Optional containing User if found
     */
    Optional<User> findById(Long id);

    /**
     * Save a new user entity to the database.
     *
     * @param user User entity to persist
     * @return Saved User entity with generated primary key
     */
    User save(User user);

    /**
     * Update existing user details.
     *
     * @param user User entity with updated fields
     * @return Updated User entity
     */
    User update(User user);

    /**
     * Retrieve list of all registered users.
     *
     * @return List of Users
     */
    List<User> findAll();

    /**
     * Delete user by ID.
     *
     * @param id User ID to remove
     * @return true if deleted successfully
     */
    boolean deleteById(Long id);
}
