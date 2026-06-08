package com.uday.blood_connect.dto.response;

import com.uday.blood_connect.enums.BloodGroup;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

public record StatsResponseDTO(

        @Schema(description = "Total number of registered users", example = "1500")
        Long totalUsers,

        @Schema(description = "Total number of available donors", example = "750")
        Long totalAvailableDonors,

        @Schema(description = "Total number of blood requests", example = "200")
        Long totalBloodRequests,

        @Schema(description = "Number of open blood requests", example = "50")
        Long totalOpenRequests,

        @Schema(description = "Total number of accepted offers", example = "120")
        Long totalAcceptedOffers,

        @Schema(description = "Total number of fulfilled requests", example = "180")
        Long totalFulfilledRequests,

        @Schema(description = "Total number of declined offers", example = "30")
        Long totalDeclinedOffers,

        @Schema(description = "Total number of cancelled requests", example = "10")
        Long totalCancelledRequests,

        @Schema(description = "Total number of normal urgency requests", example = "100")
        Long totalNormalRequests,

        @Schema(description = "Total number of urgent requests", example = "70")
        Long totalUrgentRequests,

        @Schema(description = "Total number of critical requests", example = "30")
        Long totalCriticalRequests,

        @Schema(description = "Distribution of users grouped by age", example = "{\"20\": 100, \"30\": 200, \"40\": 150}")
        Map<Integer, Long> groupByAge,

        @Schema(description = "Distribution of users grouped by blood group", example = "{\"O+\": 300, " + "\"A+\": 250, \"B+\": 200}")
        Map<BloodGroup, Long> groupByBlood
) {
}
