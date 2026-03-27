package com.nexus.dto.predictionHistory;

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
        Integer weightClassId,

        @NotNull(message = "Probability A is required")
        Double probabilityA,

        @NotNull(message = "Probability B is required")
        Double probabilityB,

        String breakdownSnapshot
) {
}