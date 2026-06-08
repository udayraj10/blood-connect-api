package com.uday.blood_connect.exception;

import java.time.LocalDateTime;

public record ErrorResponse(

        String status,
        String code,
        Object error,
        LocalDateTime timestamp
) {
    public ErrorResponse(String code, Object error) {
        this("error", code, error, LocalDateTime.now());
    }
}
