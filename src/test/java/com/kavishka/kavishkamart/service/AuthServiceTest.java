package com.kavishka.kavishkamart.service;

import com.kavishka.kavishkamart.dto.LoginRequestDTO;
import com.kavishka.kavishkamart.dto.UserDTO;
import com.kavishka.kavishkamart.model.Role;
import com.kavishka.kavishkamart.model.User;
import com.kavishka.kavishkamart.util.PasswordUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    @Test
    @DisplayName("Should hash password and verify successfully")
    void testPasswordHashing() {
        String plain = "password123";
        String hashed = PasswordUtil.hashPassword(plain);

        assertNotNull(hashed);
        assertTrue(PasswordUtil.checkPassword(plain, hashed));
        assertFalse(PasswordUtil.checkPassword("wrongpassword", hashed));
    }

    @Test
    @DisplayName("Should validate DTO mapping and role assignment")
    void testUserDTOMapping() {
        User user = new User();
        user.setId(10L);
        user.setName("Alice Buyer");
        user.setEmail("alice@kavishkamart.com");
        user.setRole(Role.BUYER);

        UserDTO dto = UserDTO.fromEntity(user);
        assertEquals(10L, dto.getId());
        assertEquals("Alice Buyer", dto.getName());
        assertEquals("alice@kavishkamart.com", dto.getEmail());
        assertEquals(Role.BUYER, dto.getRole());
    }

    @Test
    @DisplayName("Should validate LoginRequestDTO encapsulation")
    void testLoginRequestDTO() {
        LoginRequestDTO request = new LoginRequestDTO("test@example.com", "secretPass");
        assertEquals("test@example.com", request.getEmail());
        assertEquals("secretPass", request.getPassword());
    }
}
