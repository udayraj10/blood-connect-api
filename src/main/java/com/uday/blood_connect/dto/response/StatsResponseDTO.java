package com.uday.blood_connect.dto.response;

import com.uday.blood_connect.enums.BloodGroup;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

public record StatsResponseDTO(

        @Schema(description = "Total number of registered users", example = "1500")
        Long totalUsers,

        @Schema(description = "Total number of active users", example = "1000")
        Long activeUsers,

        @Schema(description = "Total number of in active users", example = "500")
        Long inActiveUsers,

        @Schema(description = "Total number of available users", example = "700")
        Long availableUsers,

        @Schema(description = "Total number of blood requests", example = "300")
        Long totalBloodRequests,

        @Schema(description = "Total number of donation offers", example = "550")
        Long totalDonationOffers,

        @Schema(description = "Total number of open blood requests", example = "100")
        Long openRequests,

        @Schema(description = "Total number of fulfilled requests", example = "50")
        Long fulfilledRequests,

        @Schema(description = "Total number of cancelled requests", example = "10")
        Long cancelledRequests,

        @Schema(description = "Total number of normal urgency requests", example = "100")
        Long normalRequests,

        @Schema(description = "Total number of urgent requests", example = "70")
        Long urgentRequests,

        @Schema(description = "Total number of critical requests", example = "30")
        Long criticalRequests,

        @Schema(description = "Distribution of users grouped by age", example = "{\"20\": 100, \"30\": 200, \"40\": 150}")
        Map<Integer, Long> groupByAge,

        @Schema(description = "Distribution of users grouped by blood group", example = "{\"O+\": 300, " + "\"A+\": 250, \"B+\": 200}")
        Map<BloodGroup, Long> groupByBlood
) {
}
