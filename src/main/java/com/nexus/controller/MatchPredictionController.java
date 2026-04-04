package com.nexus.controller;

import com.nexus.dto.error.ApiErrorResponse;
import com.nexus.dto.prediction.PredictMatchRequest;
import com.nexus.dto.prediction.PredictionResponse;
import com.nexus.service.MatchPredictionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/predictions")
@Tag(name = "Match Prediction", description = "Endpoints for predicting boxing match outcomes using the Perfect Boxer model")
public class MatchPredictionController {

    private final MatchPredictionService matchPredictionService;

    public MatchPredictionController(MatchPredictionService matchPredictionService) {
        this.matchPredictionService = matchPredictionService;
    }

    @Operation(
            summary = "Predict match outcome",
            description = """
            Predicts the outcome of a boxing match between two fighters.

            The system:
            - Normalizes all attributes using Min-Max normalization
            - Applies category-based weighting
            - Calculates each fighter's closeness to the Perfect Boxer
            - Converts closeness into win probabilities
            - Determines a predicted winner (or draw if equal)

            The result includes probabilities, closeness scores, category breakdowns, and an AI-generated explanation.
            """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Prediction generated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PredictionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or validation failed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Required data not found (e.g., weight class or perfect boxer)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<PredictionResponse> predict(
            @Parameter(
                    description = "Match prediction request containing two fighters and the weight class",
                    required = true
            )
            @Valid @RequestBody PredictMatchRequest request
    ) {
        return ResponseEntity.ok(matchPredictionService.predict(request));
    }
}