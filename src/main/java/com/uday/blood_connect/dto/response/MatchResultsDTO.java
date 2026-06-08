package com.uday.blood_connect.dto.response;

import com.uday.blood_connect.enums.BloodGroup;
import com.uday.blood_connect.enums.OfferStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record MatchResultsDTO(

        @Schema(description = "Unique identifier of the match result", example = "1")
        Long id,

        @Schema(description = "Full name of the matched donor", example = "Jane Smith")
        String fullName,

        @Schema(description = "City of the matched donor", example = "New York")
        String city,

        @Schema(description = "Blood group of the matched donor", example = "O+")
        BloodGroup bloodGroup,

        @Schema(description = "Status of the donation offer", example = "ACCEPTED")
        OfferStatus offerStatus,

        @Schema(description = "Timestamp when the offer was made", example = "2025-06-06T10:30:00")
        LocalDateTime offeredAt
) {
}
