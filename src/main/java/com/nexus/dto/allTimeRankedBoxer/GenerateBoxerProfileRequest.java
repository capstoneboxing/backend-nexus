package com.nexus.dto.allTimeRankedBoxer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GenerateBoxerProfileRequest(
        @NotBlank(message = "Boxer name is required")
        String boxerName,

        @NotNull(message = "Weight class id is required")
        Integer weightClassId
) {
}