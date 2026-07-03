package com.uday.blood_connect.exception;

import java.time.LocalDateTime;

public record ErrorResponse(

        String status,
        String code,
        Object message,
        LocalDateTime timestamp
) {
    public ErrorResponse(String code, Object message) {
        this("error", code, message, LocalDateTime.now());
    }
}
