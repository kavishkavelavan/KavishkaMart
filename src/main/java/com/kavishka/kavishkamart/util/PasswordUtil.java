package com.kavishka.kavishkamart.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility for hashing and verifying passwords using BCrypt.
 * Enforces Mandatory Rule #2 (No plaintext, MD5, or SHA1 storage).
 */
public class PasswordUtil {

    private PasswordUtil() {
    }

    /**
     * Hash a plain text password using BCrypt.
     *
     * @param plainPassword Password in cleartext
     * @return Hashed password string
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
    }

    /**
     * Verify a plain text password against a stored BCrypt hash.
     *
     * @param plainPassword Plaintext password candidate
     * @param hashedPassword Stored BCrypt password hash
     * @return True if passwords match, false otherwise
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null || hashedPassword.isEmpty()) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
