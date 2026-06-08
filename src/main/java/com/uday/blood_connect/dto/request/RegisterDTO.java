package com.uday.blood_connect.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uday.blood_connect.enums.AccountType;
import com.uday.blood_connect.enums.BloodGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {

    @Schema(description = "Full name of the user", example = "John Doe")
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @Schema(description = "Email address of the user", example = "john@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Schema(description = "Password for user account", example = "John@545")
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    private String password;

    @Schema(description = "Age of the user in years", example = "30")
    @NotNull(message = "Age is required")
    @Positive(message = "Age must be a positive number")
    private Integer age;

    @Schema(description = "Phone number of the user (10 digits)", example = "9876543210")
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone;

    @Schema(description = "Blood group of the user", example = "O+")
    @NotNull(message = "Blood group is required")
    private BloodGroup bloodGroup;

    @Schema(description = "City where the user is located", example = "New York")
    @NotBlank(message = "City is required")
    @Size(min = 2, max = 100, message = "City must be between 2 and 100 characters")
    private String city;

    @Schema(description = "Complete address of the user", example = "123 Main Street, Apt 4B")
    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
    private String address;

    @Schema(description = "Account type: INDIVIDUAL or ORGANIZATION", example = "INDIVIDUAL")
    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @Schema(description = "Whether the user is available for donation", example = "true")
    @NotNull(message = "Availability status is required")
    private Boolean isAvailable;

    @Schema(description = "Last blood donation date in dd-MM-yyyy format", example = "01-06-2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate lastDonationDate;
}
