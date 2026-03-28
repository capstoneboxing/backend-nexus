package com.nexus.dto.allTimeRankedBoxer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to generate a boxer profile using AI for a given boxer name and weight class")
public record GenerateBoxerProfileRequest(

        @Schema(
                description = "Name of the boxer to generate a profile for",
                example = "Terence Crawford",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Boxer name is required")
        String boxerName,

        @Schema(
                description = "Weight class ID to contextualize the boxer profile",
                example = "11",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Weight class id is required")
        @Min(1) @Max(17)
        Integer weightClassId
) {
}