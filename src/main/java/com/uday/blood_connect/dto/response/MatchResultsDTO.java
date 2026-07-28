package com.uday.blood_connect.dto.response;

import com.uday.blood_connect.enums.BloodGroup;
import com.uday.blood_connect.enums.OfferStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record MatchResultsDTO(

        @Schema(description = "Unique identifier of the match result", example = "1")
        Long id,

        @Schema(description = "Full name of the matched donor", example = "Aarav Sharma")
        String fullName,

        @Schema(description = "City of the matched donor", example = "Mumbai")
        String city,

        @Schema(description = "Blood group of the matched donor", example = "O+")
        BloodGroup bloodGroup,

        @Schema(description = "Status of the donation offer", example = "PENDING")
        OfferStatus offerStatus,

        @Schema(description = "Timestamp when the offer was made", example = "2025-06-06T10:30:00")
        LocalDateTime offeredAt,

        @Schema(description = "Timestamp when the response was made", example = "2025-07-06T12:35:00")
        LocalDateTime respondedAt
) {
}
