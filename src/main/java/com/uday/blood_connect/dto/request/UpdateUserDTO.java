package com.uday.blood_connect.dto.request;

import com.uday.blood_connect.enums.BloodGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UpdateUserDTO(

        @Schema(description = "Updated full name of the user", example = "Gaurav Taneja")
        @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
        String fullName,

        @Schema(description = "Updated email address", example = "gaurav.t@gmail.com")
        @Email(message = "Email should be valid")
        String email,

        @Schema(description = "Updated phone number (10-20 digits)", example = "9876543210")
        @Pattern(regexp = "^[0-9]{10,20}$", message = "Phone number must be between 10 and 20 digits")
        String phoneNumber,

        @Schema(description = "Updated age of the user", example = "30")
        @Positive(message = "Age must be a positive number")
        Integer age,

        @Schema(description = "Updated blood group", example = "O+")
        BloodGroup bloodGroup,

        @Schema(description = "Updated city", example = "Mumbai")
        @Size(max = 100, message = "City must be at most 100 characters")
        String city,

        @Schema(description = "Updated address", example = "123 Main Street, Apt 4B")
        @Size(max = 255, message = "Address must be at most 255 characters")
        String address,

        @Schema(description = "Last blood donation date (cannot be in the future)", example = "2025-06-01")
        @PastOrPresent(message = "Last donation date cannot be in the future")
        LocalDate lastDonationDate
) {
}
