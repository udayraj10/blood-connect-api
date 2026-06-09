package com.uday.blood_connect.dto.response;

import com.uday.blood_connect.enums.BloodGroup;
import com.uday.blood_connect.enums.OfferStatus;
import com.uday.blood_connect.enums.UrgencyLevel;
import io.swagger.v3.oas.annotations.media.Schema;

public record DonationOfferResponseDTO(

        @Schema(description = "Unique identifier of the donation offer", example = "1")
        Long id,

        @Schema(description = "Name of the person who requested the blood", example = "Aarav Sharma")
        String requestedBy,

        @Schema(description = "Blood group for the donation", example = "A+")
        BloodGroup bloodGroup,

        @Schema(description = "City where the donation is offered", example = "Delhi")
        String city,

        @Schema(description = "Urgency level of the request", example = "URGENT")
        UrgencyLevel urgencyLevel,

        @Schema(description = "Additional message for the donation offer", example = "Emergency heart surgery requirement.")
        String message,

        @Schema(description = "Current status of the donation offer", example = "PENDING")
        OfferStatus status,

        @Schema(description = "Timestamp when the offer was made", example = "2025-06-06T10:30:00")
        String offeredAt,

        @Schema(description = "Timestamp when the offer was responded to", example = "2025-06-06T11:00:00")
        String respondedAt
) {
}
