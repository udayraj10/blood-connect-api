package com.uday.blood_connect.dto.response;

import com.uday.blood_connect.enums.BloodGroup;
import com.uday.blood_connect.enums.RequestStatus;
import com.uday.blood_connect.enums.UrgencyLevel;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Container for Blood Request response payloads.
 */
public class BloodRequestResponseDTO {

    private BloodRequestResponseDTO() {
    }

    /**
     * Basic summary of a blood request.
     */
    @Schema(
            name = "BloodRequestSummary",
            description = "Summary view of a blood request containing essential information"
    )
    public record Summary(
            @Schema(description = "Unique identifier of the blood request", example = "1")
            Long id,

            @Schema(description = "Blood group required for the request", example = "O+")
            BloodGroup bloodGroup,

            @Schema(description = "City where blood is needed", example = "New York")
            String city,

            @Schema(description = "Urgency level of the request", example = "URGENT")
            UrgencyLevel urgencyLevel,

            @Schema(description = "Current status of the request", example = "OPEN")
            RequestStatus status,

            @Schema(description = "Additional message for the request", example = "Urgent blood needed for surgery")
            String message
    ) {
    }

    /**
     * Detailed view of a blood request, including timestamps.
     */
    @Schema(
            name = "BloodRequestDetails",
            description = "Detailed view of a blood request including timestamp information"
    )
    public record Details(
            @Schema(description = "Unique identifier of the blood request", example = "1")
            Long id,

            @Schema(description = "Blood group required for the request", example = "O+")
            BloodGroup bloodGroup,

            @Schema(description = "City where blood is needed", example = "New York")
            String city,

            @Schema(description = "Urgency level of the request", example = "URGENT")
            UrgencyLevel urgencyLevel,

            @Schema(description = "Current status of the request", example = "OPEN")
            RequestStatus status,

            @Schema(description = "Additional message for the request", example = "Urgent blood needed for surgery")
            String message,

            @Schema(description = "Timestamp when the request was created", example = "2025-06-06T10:30:00")
            LocalDateTime createdAt
    ) {
    }
}