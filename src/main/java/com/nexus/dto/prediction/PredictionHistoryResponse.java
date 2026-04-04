package com.nexus.dto.prediction;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.Map;

@Schema(description = "Prediction history response")
public record PredictionHistoryResponse(

        @Schema(description = "Unique prediction ID", example = "1")
        Integer predictionId,

        @Schema(description = "Name of boxer A", example = "Floyd Mayweather Jr.")
        String boxerAName,

        @Schema(description = "Name of boxer B", example = "Manny Pacquiao")
        String boxerBName,

        @Schema(description = "Model-predicted winner", example = "BOXER_A")
        String predictedWinner,

        @Schema(description = "Actual winner after the fight, if known", example = "BOXER_A")
        String matchWinner,

        @Schema(description = "Actual method of victory, if known", example = "DECISION")
        String matchWinMethod,

        @Schema(description = "Weight class ID", example = "11")
        Integer weightClassId,

        @Schema(description = "Closeness score of boxer A to the perfect boxer", example = "0.80")
        Double boxerAClosenessScore,

        @Schema(description = "Closeness score of boxer B to the perfect boxer", example = "0.86")
        Double boxerBClosenessScore,

        @Schema(description = "Predicted win probability for boxer A", example = "0.48")
        Double probabilityA,

        @Schema(description = "Predicted win probability for boxer B", example = "0.52")
        Double probabilityB,

        @Schema(description = "Detailed structured breakdown or explanation of the prediction")
        Map<String, Object> breakdownSnapshot,

        @Schema(description = "Date and time of prediction", example = "2026-03-27T18:30:00Z")
        OffsetDateTime predictionDate
) {}