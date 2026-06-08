package com.uday.blood_connect.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AvailabilityDTO(

        @Schema(description = "Availability status for blood donation", example = "true")
        @NotNull(message = "Availability status is required")
        Boolean isAvailable
) {
}
