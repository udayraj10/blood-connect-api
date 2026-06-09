package com.uday.blood_connect.dto.request;

import com.uday.blood_connect.enums.BloodGroup;
import com.uday.blood_connect.enums.UrgencyLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BloodRequestDTO(

        @Schema(description = "Blood group required for donation", example = "O+")
        @NotNull(message = "Blood group is required")
        BloodGroup bloodGroup,

        @Schema(description = "City where blood is needed", example = "Bengaluru")
        @NotBlank(message = "City cannot be blank")
        @Size(min = 2, max = 100, message = "City must be between 2 and 100 characters")
        String city,

        @Schema(description = "Urgency level of the blood request", example = "URGENT")
        @NotNull(message = "Urgency level is required")
        UrgencyLevel urgencyLevel,

        @Schema(description = "Additional message or notes for the blood request", example = "Urgent blood needed for surgery")
        @Size(max = 500, message = "Message must not exceed 500 characters")
        String message

) {
}
