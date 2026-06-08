package com.uday.blood_connect.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordDTO(

        @Schema(description = "New password for the user account", example = "newPassword123")
        @NotBlank(message = "New password is required")
        @Size(min = 6, message = "New password must be at least 6 characters")
        String newPassword,

        @Schema(description = "Confirmation of the new password for verification", example = "confirmPassword123")
        @NotBlank(message = "Confirm password is required")
        @Size(min = 6, message = "Confirm password must be at least 6 characters")
        String confirmPassword
) {
}
