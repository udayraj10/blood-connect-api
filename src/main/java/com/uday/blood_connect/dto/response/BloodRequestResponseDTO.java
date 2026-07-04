package com.uday.blood_connect.dto.response;

import com.uday.blood_connect.enums.BloodGroup;
import com.uday.blood_connect.enums.RequestStatus;
import com.uday.blood_connect.enums.UrgencyLevel;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(
        name = "BloodRequestDetails",
        description = "Detailed view of a blood request including timestamp information"
)
public record BloodRequestResponseDTO(
        @Schema(description = "Unique identifier of the blood request", example = "1")
        Long id,

        @Schema(description = "Blood group required for the request", example = "O+")
        BloodGroup bloodGroup,

        @Schema(description = "City where blood is needed", example = "Delhi")
        String city,

        @Schema(description = "Urgency level of the request", example = "NORMAL")
        UrgencyLevel urgencyLevel,

        @Schema(description = "Current status of the request", example = "OPEN")
        RequestStatus status,

        @Schema(description = "Additional message for the request", example = "Scheduled bypass surgery.")
        String message,

        @Schema(description = "Timestamp when the request was created", example = "2025-06-06T10:30:00")
        LocalDateTime createdAt
) {
}