package com.kavishka.kavishkamart.model;

/**
 * User roles in the KavishkaMart e-commerce marketplace.
 */
public enum Role {
    BUYER,
    SELLER,
    ADMIN;

    public static Role fromString(String value) {
        if (value == null) return BUYER;
        try {
            return Role.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return BUYER;
        }
    }
}
