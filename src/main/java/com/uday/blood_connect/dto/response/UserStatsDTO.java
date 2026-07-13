package com.uday.blood_connect.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record UserStatsDTO(

        @Schema(description = "Total number of blood donations made by the user", example = "5")
        Long totalDonations,

        @Schema(description = "Number of pending donation offers from this user", example = "1")
        Long pendingOffers,

        @Schema(description = "Number of accepted donation offers from this user", example = "3")
        Long acceptedOffers,

        @Schema(description = "Number of completed donation offers from this user", example = "1")
        Long completedOffers,

        @Schema(description = "Number of declined donation offers from this user", example = "1")
        Long declinedOffers,

        @Schema(description = "Last date when the user made a blood donation", example = "2025-06-01")
        LocalDate lastDonationDate,

        @Schema(description = "Total number of blood requests made by the user", example = "2")
        Long totalRequestsMade,

        @Schema(description = "Number of open blood requests from this user", example = "1")
        Long openRequests,

        @Schema(description = "Number of fulfilled blood requests from this user", example = "1")
        Long fulfilledRequests,

        @Schema(description = "Number of cancelled blood requests from this user", example = "0")
        Long cancelledRequests
) {
}
