package com.kavishka.util;

/**
 * Utility delegating password hashing to the BCrypt implementation in com.kavishka.kavishkamart.util.PasswordUtil.
 */
public class PasswordUtil {

    public static String hashPassword(String raw) {
        return com.kavishka.kavishkamart.util.PasswordUtil.hashPassword(raw);
    }

    public static boolean checkPassword(String raw, String hashed) {
        return com.kavishka.kavishkamart.util.PasswordUtil.checkPassword(raw, hashed);
    }
}

