package com.nexus.dto.predictionHistory;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PredictionHistoryUpdateRequest(
        @NotBlank(message = "Boxer A name is required")
        String boxerAName,

        @NotBlank(message = "Boxer B name is required")
        String boxerBName,

        @NotBlank(message = "Match decision is required")
        String matchDecision,

        @NotNull(message = "Weight class ID is required")
        @Min(1) @Max(17)
        Integer weightClassId,

        @NotNull(message = "Probability A is required")
        Double probabilityA,

        @NotNull(message = "Probability B is required")
        Double probabilityB,

        String breakdownSnapshot
) {
}