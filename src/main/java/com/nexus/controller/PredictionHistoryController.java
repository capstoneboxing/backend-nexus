package com.nexus.controller;

import com.nexus.dto.error.ApiErrorResponse;
import com.nexus.dto.predictionHistory.PredictionHistoryResponse;
import com.nexus.dto.predictionHistory.PredictionHistoryUpdateRequest;
import com.nexus.service.PredictionHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/prediction-history")
@Tag(name = "Prediction History", description = "Endpoints for viewing and managing saved fight prediction history")
@SecurityRequirement(name = "bearerAuth")
public class PredictionHistoryController {

    private final PredictionHistoryService predictionHistoryService;

    public PredictionHistoryController(PredictionHistoryService predictionHistoryService) {
        this.predictionHistoryService = predictionHistoryService;
    }

    @Operation(
            summary = "Get all prediction history",
            description = "Returns all saved prediction history records. Requires a valid Bearer JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prediction history retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<PredictionHistoryResponse>> getPredictionHistories() {
        return ResponseEntity.ok(predictionHistoryService.getPredictionHistories());
    }

    @Operation(
            summary = "Get prediction history by ID",
            description = "Returns one saved prediction history record by prediction ID. Requires a valid Bearer JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prediction history found"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prediction history not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<PredictionHistoryResponse> getPredictionHistoryById(
            @Parameter(description = "Prediction history ID", example = "1")
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(predictionHistoryService.getPredictionHistoryById(id));
    }

    @Operation(
            summary = "Update prediction history",
            description = "Updates an existing saved prediction history record. Requires a valid Bearer JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prediction history updated successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or validation failed",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prediction history not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<PredictionHistoryResponse> updatePredictionHistory(
            @Parameter(description = "Prediction history ID", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody PredictionHistoryUpdateRequest request
    ) {
        return ResponseEntity.ok(predictionHistoryService.updatePredictionHistory(id, request));
    }

    @Operation(
            summary = "Delete prediction history",
            description = "Deletes a saved prediction history record by ID. Requires a valid Bearer JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Prediction history deleted successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prediction history not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePredictionHistory(
            @Parameter(description = "Prediction history ID", example = "1")
            @PathVariable Integer id
    ) {
        predictionHistoryService.deletePredictionHistory(id);
        return ResponseEntity.noContent().build();
    }
}