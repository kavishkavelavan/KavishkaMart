package com.kavishka.kavishkamart.exception;

import java.util.Collections;
import java.util.Map;

/**
 * Exception thrown when input validation rules fail.
 */
public class ValidationException extends Exception {
    private final String code;
    private final Map<String, String> fieldErrors;

    public ValidationException(String message) {
        super(message);
        this.code = "VALIDATION_ERROR";
        this.fieldErrors = Collections.emptyMap();
    }

    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.code = "VALIDATION_ERROR";
        this.fieldErrors = fieldErrors != null ? fieldErrors : Collections.emptyMap();
    }

    public String getCode() {
        return code;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
