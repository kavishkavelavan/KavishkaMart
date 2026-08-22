package com.kavishka.kavishkamart.service;

import com.kavishka.kavishkamart.dto.LoginRequestDTO;
import com.kavishka.kavishkamart.dto.RegisterRequestDTO;
import com.kavishka.kavishkamart.dto.UserDTO;
import com.kavishka.kavishkamart.exception.AuthenticationException;
import com.kavishka.kavishkamart.exception.ResourceNotFoundException;
import com.kavishka.kavishkamart.exception.ValidationException;

import java.util.List;

/**
 * Service interface for User registration, login authentication, and profile management.
 */
public interface UserService {

    /**
     * Register a new user (Buyer or Seller).
     *
     * @param dto Registration details
     * @return Created UserDTO
     * @throws ValidationException if input validation fails or email already exists
     */
    UserDTO register(RegisterRequestDTO dto) throws ValidationException;

    /**
     * Authenticate user credentials.
     *
     * @param dto Login credentials
     * @return Authenticated UserDTO
     * @throws AuthenticationException if authentication fails
     * @throws ValidationException if input validation fails
     */
    UserDTO login(LoginRequestDTO dto) throws AuthenticationException, ValidationException;

    /**
     * Find user profile by ID.
     *
     * @param id User ID
     * @return UserDTO
     * @throws ResourceNotFoundException if user does not exist
     */
    UserDTO getUserById(Long id) throws ResourceNotFoundException;

    /**
     * Get list of all registered users (for Admin moderation).
     *
     * @return List of UserDTOs
     */
    List<UserDTO> getAllUsers();
}

