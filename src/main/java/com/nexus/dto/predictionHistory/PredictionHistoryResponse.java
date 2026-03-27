package com.nexus.dto.predictionHistory;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Prediction history response")
public record PredictionHistoryResponse(

        @Schema(description = "Unique prediction ID", example = "1")
        Integer predictionId,

        @Schema(description = "Name of boxer A", example = "Floyd Mayweather Jr.")
        String boxerAName,

        @Schema(description = "Name of boxer B", example = "Manny Pacquiao")
        String boxerBName,

        @Schema(description = "Actual match result or predicted decision", example = "Boxer A wins by decision")
        String matchDecision,

        @Schema(description = "Weight class ID", example = "11")
        Integer weightClassId,

        @Schema(description = "Win probability for boxer A", example = "0.65")
        Double probabilityA,

        @Schema(description = "Win probability for boxer B", example = "0.35")
        Double probabilityB,

        @Schema(description = "Detailed breakdown explanation", example = "Boxer A has superior footwork and defense...")
        String breakdownSnapshot,

        @Schema(description = "Date and time of prediction", example = "2026-03-27T18:30:00Z")
        OffsetDateTime predictionDate
) {}
