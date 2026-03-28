package com.nexus.dto.perfectboxer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to start perfect boxer generation for a specific weight class")
public record PerfectBoxerGenerationRequest(

        @Schema(
                description = "ID of the weight class for which the perfect boxer will be generated",
                example = "11",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Weight class id is required")
        @Min(1) @Max(17)
        Integer weightClassId,

        @Schema(
                description = "Number of top ranked boxers to use when generating the perfect boxer (used to compute mean attributes)",
                example = "10",
                minimum = "3",
                maximum = "10",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Amount is required")
        @Min(value = 3, message = "Amount must be at least 3")
        @Max(value = 10, message = "Amount cannot be more than 10")
        Integer amount
) {
}