package com.kavishka.kavishkamart.service;

import com.kavishka.kavishkamart.dao.UserDAO;
import com.kavishka.kavishkamart.dto.LoginRequestDTO;
import com.kavishka.kavishkamart.dto.RegisterRequestDTO;
import com.kavishka.kavishkamart.dto.UserDTO;
import com.kavishka.kavishkamart.exception.AuthenticationException;
import com.kavishka.kavishkamart.exception.ValidationException;
import com.kavishka.kavishkamart.model.Role;
import com.kavishka.kavishkamart.model.User;
import com.kavishka.kavishkamart.service.impl.UserServiceImpl;
import com.kavishka.kavishkamart.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Business rule validation tests for UserService using Mockito.
 * Satisfies Spec Section 9 Testing requirements (Service layer business rule validation with DAO mocked).
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userDAO);
    }

    @Test
    @DisplayName("Should successfully register a new buyer with hashed password")
    void testRegisterSuccess() throws Exception {
        RegisterRequestDTO dto = new RegisterRequestDTO("Alice Johnson", "alice@example.com", "Password123!", "BUYER");

        when(userDAO.findByEmail("alice@example.com")).thenReturn(Optional.empty());
        when(userDAO.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(10L);
            return u;
        });

        UserDTO result = userService.register(dto);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Alice Johnson", result.getName());
        assertEquals("alice@example.com", result.getEmail());
        assertEquals(Role.BUYER, result.getRole());
        verify(userDAO, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when registering with duplicate email")
    void testRegisterDuplicateEmail() {
        RegisterRequestDTO dto = new RegisterRequestDTO("Alice Johnson", "existing@example.com", "Password123!", "BUYER");

        when(userDAO.findByEmail("existing@example.com")).thenReturn(Optional.of(new User()));

        assertThrows(ValidationException.class, () -> userService.register(dto));
        verify(userDAO, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should demote ADMIN self-registration to BUYER per Rule F1")
    void testRegisterAdminDemotion() throws Exception {
        RegisterRequestDTO dto = new RegisterRequestDTO("Sneaky Admin", "sneaky@example.com", "Password123!", "ADMIN");

        when(userDAO.findByEmail("sneaky@example.com")).thenReturn(Optional.empty());
        when(userDAO.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(11L);
            return u;
        });

        UserDTO result = userService.register(dto);

        assertEquals(Role.BUYER, result.getRole(), "Self-registration as ADMIN must be forced to BUYER role per Spec Rule F1");
    }

    @Test
    @DisplayName("Should successfully authenticate valid credentials")
    void testLoginSuccess() throws Exception {
        String plainPassword = "SecretPassword123!";
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);

        User mockUser = new User();
        mockUser.setId(5L);
        mockUser.setName("Bob Smith");
        mockUser.setEmail("bob@example.com");
        mockUser.setPasswordHash(hashedPassword);
        mockUser.setRole(Role.SELLER);

        when(userDAO.findByEmail("bob@example.com")).thenReturn(Optional.of(mockUser));

        LoginRequestDTO loginDTO = new LoginRequestDTO("bob@example.com", plainPassword);
        UserDTO loggedIn = userService.login(loginDTO);

        assertNotNull(loggedIn);
        assertEquals(5L, loggedIn.getId());
        assertEquals(Role.SELLER, loggedIn.getRole());
    }

    @Test
    @DisplayName("Should throw AuthenticationException on incorrect password")
    void testLoginFailureWrongPassword() {
        String hashedPassword = PasswordUtil.hashPassword("CorrectPassword123!");

        User mockUser = new User();
        mockUser.setEmail("bob@example.com");
        mockUser.setPasswordHash(hashedPassword);

        when(userDAO.findByEmail("bob@example.com")).thenReturn(Optional.of(mockUser));

        LoginRequestDTO loginDTO = new LoginRequestDTO("bob@example.com", "WrongPassword!");

        assertThrows(AuthenticationException.class, () -> userService.login(loginDTO));
    }
}
