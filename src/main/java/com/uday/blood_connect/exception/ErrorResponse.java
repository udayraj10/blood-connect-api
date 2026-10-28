package com.uday.blood_connect.exception;

import java.time.LocalDateTime;

public record ErrorResponse(

        LocalDateTime timestamp,
        int statusCode,
        String error,
        String message
) {

    // overloaded constructor
    public ErrorResponse(int statusCode, String error, String message) {
        this(LocalDateTime.now(), statusCode, error, message);
    }
}
