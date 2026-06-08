package com.uday.blood_connect.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record JwtResponse(

        @Schema(description = "Status of the authentication operation", example = "success")
        String status,

        @Schema(description = "Message describing the authentication result", example = "Authentication successful")
        String message,

        @Schema(description = "JWT token for authenticated requests", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,

        @Schema(description = "Timestamp when the token was generated", example = "2025-06-06T10:30:00")
        LocalDateTime timestamp
) {

    public JwtResponse(String message, String token) {
        this("success", message, token, LocalDateTime.now());
    }
}
