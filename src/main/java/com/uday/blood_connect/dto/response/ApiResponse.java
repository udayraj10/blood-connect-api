package com.uday.blood_connect.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {

    @Schema(description = "Indicates whether the request was successful", example = "true")
    private final boolean success;

    @Schema(description = "Response message describing the result", example = "Operation completed successfully")
    private final String message;

    @Schema(description = "Timestamp when the response was generated", example = "2025-06-06T10:30:00")
    private final LocalDateTime timestamp;

    @Schema(description = "The actual response data payload")
    private final T data;

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

}
