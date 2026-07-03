package com.uday.blood_connect.dto.response;

import com.uday.blood_connect.enums.AccountType;
import com.uday.blood_connect.enums.BloodGroup;
import com.uday.blood_connect.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponseDTO(
        @Schema(description = "Unique identifier of the user", example = "1")
        Long id,

        @Schema(description = "Full name of the user", example = "Aarav Sharma")
        String fullName,

        @Schema(description = "Email address of the user", example = "aarav.sharma@gmail.com")
        String email,

        @Schema(description = "Role of the user")
        Role role,

        @Schema(description = "Phone number of the user", example = "9876543210")
        String phone,

        @Schema(description = "Age of the user", example = "30")
        Integer age,

        @Schema(description = "Blood group of the user", example = "O+")
        BloodGroup bloodGroup,

        @Schema(description = "City where the user is located", example = "Hyderabad")
        String city,

        @Schema(description = "Complete address of the user", example = "123 Main Street, Apt 4B")
        String address,

        @Schema(description = "Account type (INDIVIDUAL or ORGANIZATION)", example = "INDIVIDUAL")
        AccountType accountType,

        @Schema(description = "Whether the user is available for donation", example = "true")
        Boolean isAvailable,

        @Schema(description = "Last blood donation date", example = "2025-06-01")
        LocalDate lastDonationDate,

        @Schema(description = "Timestamp when the user account was created", example = "2025-06-06T10:30:00")
        LocalDateTime createdAt
) {
}