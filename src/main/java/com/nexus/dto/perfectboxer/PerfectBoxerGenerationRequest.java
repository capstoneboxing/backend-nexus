package com.nexus.dto.perfectboxer;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PerfectBoxerGenerationRequest(
        @NotNull(message = "Weight class id is required")
        Integer weightClassId,

        @NotNull(message = "Amount is required")
        @Min(value = 1, message = "Amount must be at least 1")
        @Max(value = 10, message = "Amount cannot be more than 10")
        Integer amount
) {
}