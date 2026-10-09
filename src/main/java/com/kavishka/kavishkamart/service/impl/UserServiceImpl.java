package com.kavishka.kavishkamart.service.impl;

import com.kavishka.kavishkamart.dao.UserDAO;
import com.kavishka.kavishkamart.dto.LoginRequestDTO;
import com.kavishka.kavishkamart.dto.RegisterRequestDTO;
import com.kavishka.kavishkamart.dto.UserDTO;
import com.kavishka.kavishkamart.exception.AuthenticationException;
import com.kavishka.kavishkamart.exception.ResourceNotFoundException;
import com.kavishka.kavishkamart.exception.ValidationException;
import com.kavishka.kavishkamart.model.Role;
import com.kavishka.kavishkamart.model.User;
import com.kavishka.kavishkamart.service.UserService;
import com.kavishka.kavishkamart.util.PasswordUtil;
import com.kavishka.kavishkamart.util.ValidationUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of UserService interface.
 * Implements business rules and validation logic (No JDBC code here).
 */
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDTO register(RegisterRequestDTO dto) throws ValidationException {
        Map<String, String> errors = new HashMap<>();

        if (dto == null) {
            throw new ValidationException("Registration request payload cannot be empty");
        }

        String name = ValidationUtil.sanitize(dto.getName());
        String email = ValidationUtil.sanitize(dto.getEmail());
        String password = dto.getPassword();
        String roleStr = ValidationUtil.sanitize(dto.getRole());

        if (ValidationUtil.isNullOrEmpty(name)) {
            errors.put("name", "Full name is required");
        }

        if (!ValidationUtil.isValidEmail(email)) {
            errors.put("email", "A valid email address is required");
        } else if (userDAO.findByEmail(email).isPresent()) {
            errors.put("email", "Email address is already registered");
        }

        if (ValidationUtil.isNullOrEmpty(password) || password.length() < 6) {
            errors.put("password", "Password must be at least 6 characters long");
        }

        Role role = Role.fromString(roleStr);
        if (role == Role.ADMIN) {
            // Rule F1: Admin role assigned via seed account; no separate admin signup flow.
            role = Role.BUYER;
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed for user registration", errors);
        }

        // Encrypt password using BCrypt (Spec Section 2 & Rule #2)
        String hashedPassword = PasswordUtil.hashPassword(password);

        User user = new User();
        user.setName(name);
        user.setEmail(email.toLowerCase());
        user.setPasswordHash(hashedPassword);
        user.setRole(role);

        User savedUser = userDAO.save(user);
        return UserDTO.fromEntity(savedUser);
    }

    @Override
    public UserDTO login(LoginRequestDTO dto) throws AuthenticationException, ValidationException {
        Map<String, String> errors = new HashMap<>();

        if (dto == null) {
            throw new ValidationException("Login payload cannot be empty");
        }

        String email = ValidationUtil.sanitize(dto.getEmail());
        String password = dto.getPassword();

        if (!ValidationUtil.isValidEmail(email)) {
            errors.put("email", "A valid email address is required");
        }
        if (ValidationUtil.isNullOrEmpty(password)) {
            errors.put("password", "Password is required");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed for login request", errors);
        }

        User user = userDAO.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new AuthenticationException("INVALID_CREDENTIALS", "Invalid email or password"));

        if (!PasswordUtil.checkPassword(password, user.getPasswordHash())) {
            throw new AuthenticationException("INVALID_CREDENTIALS", "Invalid email or password");
        }

        return UserDTO.fromEntity(user);
    }

    @Override
    public UserDTO getUserById(Long id) throws ResourceNotFoundException {
        User user = userDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User with ID " + id + " does not exist"));
        return UserDTO.fromEntity(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userDAO.findAll().stream()
                .map(UserDTO::fromEntity)
                .toList();
    }
}
