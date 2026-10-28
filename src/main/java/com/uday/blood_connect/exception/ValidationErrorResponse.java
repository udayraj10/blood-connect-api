package com.uday.blood_connect.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponse(

        LocalDateTime timestamp,
        int statusCode,
        String error,
        String message,
        Map<String, String> fieldErrors
) {

    public ValidationErrorResponse(int statusCode, String error, String message, Map<String, String> fieldErrors) {
        this(LocalDateTime.now(), statusCode, error, message, fieldErrors);
    }
}
