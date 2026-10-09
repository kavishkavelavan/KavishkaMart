package com.kavishka.kavishkamart.dto;

import java.io.Serializable;
import java.util.Map;

/**
 * Structured error details for API responses.
 */
public class ErrorDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    private String message;
    private Map<String, String> fieldErrors;

    public ErrorDetail() {
    }

    public ErrorDetail(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorDetail(String code, String message, Map<String, String> fieldErrors) {
        this.code = code;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
