package com.kavishka.kavishkamart.exception;

/**
 * Exception thrown when authentication fails or unauthorized access is attempted.
 */
public class AuthenticationException extends Exception {
    private final String code;

    public AuthenticationException(String message) {
        super(message);
        this.code = "AUTH_FAILED";
    }

    public AuthenticationException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
