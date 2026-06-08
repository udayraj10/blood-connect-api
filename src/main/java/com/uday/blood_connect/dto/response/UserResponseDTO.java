package com.uday.blood_connect.dto.response;

import com.uday.blood_connect.enums.AccountType;
import com.uday.blood_connect.enums.BloodGroup;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Container class for User response payloads to reduce file bloat.
 * Contains specific representations for different access levels (User vs Admin).
 */
public final class UserResponseDTO {

    // Private constructor to prevent instantiation of this container class
    private UserResponseDTO() {
    }

    /**
     * Basic user information accessible by regular users or public listings.
     */
    public record Summary(
            @Schema(description = "Unique identifier of the user", example = "1")
            Long id,

            @Schema(description = "Full name of the user", example = "John Doe")
            String fullName,

            @Schema(description = "Blood group of the user", example = "O+")
            BloodGroup bloodGroup,

            @Schema(description = "City where the user is located", example = "New York")
            String city,

            @Schema(description = "Whether the user is available for donation", example = "true")
            Boolean isAvailable,

            @Schema(description = "Account type (INDIVIDUAL or ORGANIZATION)", example = "INDIVIDUAL")
            AccountType accountType
    ) {
    }

    /**
     * Detailed user information containing sensitive operational and tracking data,
     * restricted for Admin and internal management use.
     */
    public record Details(
            @Schema(description = "Unique identifier of the user", example = "1")
            Long id,

            @Schema(description = "Full name of the user", example = "John Doe")
            String fullName,

            @Schema(description = "Email address of the user", example = "john@example.com")
            String email,

            @Schema(description = "Phone number of the user", example = "9876543210")
            String phone,

            @Schema(description = "Age of the user", example = "30")
            Integer age,

            @Schema(description = "Blood group of the user", example = "O+")
            BloodGroup bloodGroup,

            @Schema(description = "City where the user is located", example = "New York")
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
}