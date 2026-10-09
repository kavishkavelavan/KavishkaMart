package com.kavishka.kavishkamart.exception;

/**
 * Exception thrown when a requested entity or resource is not found.
 */
public class ResourceNotFoundException extends Exception {
    private final String code;

    public ResourceNotFoundException(String message) {
        super(message);
        this.code = "NOT_FOUND";
    }

    public ResourceNotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
