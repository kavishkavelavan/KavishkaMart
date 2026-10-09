package com.kavishka.kavishkamart.dto;

import java.io.Serializable;

/**
 * Standard API Response Envelope matching Spec Section 13.
 *
 * @param <T> Response data payload type
 */
public class ApiResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private T data;
    private ErrorDetail error;

    public ApiResponse() {
    }

    public ApiResponse(boolean success, T data, ErrorDetail error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> error(String errorCode, String message) {
        return new ApiResponse<>(false, null, new ErrorDetail(errorCode, message));
    }

    public static <T> ApiResponse<T> error(String errorCode, String message, java.util.Map<String, String> fieldErrors) {
        return new ApiResponse<>(false, null, new ErrorDetail(errorCode, message, fieldErrors));
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ErrorDetail getError() {
        return error;
    }

    public void setError(ErrorDetail error) {
        this.error = error;
    }
}
