package com.nexus.controller;

import com.nexus.dto.allTimeRankedBoxer.*;
import com.nexus.dto.error.ApiErrorResponse;
import com.nexus.service.AllTimeRankedBoxerService;
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
@RequestMapping("/api/v1/all-time-ranked-boxers")
@Tag(name = "All-Time Ranked Boxers", description = "Endpoints for retrieving and managing all-time ranked boxer records")
public class AllTimeRankedBoxerController {
    private final AllTimeRankedBoxerService rankedBoxerService;

    public AllTimeRankedBoxerController(AllTimeRankedBoxerService rankedBoxerService) {
        this.rankedBoxerService = rankedBoxerService;
    }


    @Operation(
            summary = "Get ranked boxer by ID",
            description = "Returns one all-time ranked boxer record by ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ranked boxer found"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ranked boxer not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<AllTimeRankedBoxerResponse> getById(
            @Parameter(description = "Ranked boxer ID", example = "1")
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(rankedBoxerService.findById(id));
    }

    @Operation(
            summary = "Get ranked boxers by batch ID",
            description = "Returns all ranked boxers from a specific generation batch."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ranked boxers retrieved successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Batch not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<AllTimeRankedBoxerResponse>> getByBatchId(
            @Parameter(description = "Batch ID", example = "5")
            @PathVariable Integer batchId
    ) {
        return ResponseEntity.ok(rankedBoxerService.findByBatchId(batchId));
    }

    @Operation(
            summary = "Get ranked boxers by weight class ID",
            description = "Returns ranked boxer records for a weight class."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ranked boxers retrieved successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Weight class not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/weight-class/{weightClassId}")
    public ResponseEntity<List<AllTimeRankedBoxerResponse>> getByWeightClassId(
            @Parameter(description = "Weight class ID", example = "11")
            @PathVariable Integer weightClassId
    ) {
        return ResponseEntity.ok(rankedBoxerService.findByWeightClassId(weightClassId));
    }

    @Operation(
            summary = "Get all active ranked boxers",
            description = "Returns all ranked boxers from all active batches across all weight classes."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Active ranked boxers retrieved successfully"),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/active")
    public ResponseEntity<List<AllTimeRankedBoxerResponse>> getAllActive() {
        return ResponseEntity.ok(rankedBoxerService.findAllActive());
    }

    @Operation(
            summary = "Get active ranked boxers by weight class ID",
            description = "Returns the ranked boxers in the active batch currently used to calculate the perfect boxer for that weight class."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Active ranked boxers retrieved successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No active batch found for weight class",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/active/weight-class/{weightClassId}")
    public ResponseEntity<List<AllTimeRankedBoxerResponse>> getActiveByWeightClassId(
            @Parameter(description = "Weight class ID", example = "11")
            @PathVariable Integer weightClassId
    ) {
        return ResponseEntity.ok(rankedBoxerService.findActiveByWeightClassId(weightClassId));
    }

    @Operation(
            summary = "Update ranked boxer",
            description = "Updates one all-time ranked boxer record. Requires a valid Bearer JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ranked boxer updated successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or validation failed",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ranked boxer not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
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
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<AllTimeRankedBoxerResponse> update(
            @Parameter(description = "Ranked boxer ID", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody AllTimeRankedBoxerUpdateRequest request
    ) {
        return ResponseEntity.ok(rankedBoxerService.update(id, request));
    }

    @Operation(
            summary = "Generate boxer profile with AI",
            description = "Generates a boxer attribute profile for a boxer name and weight class using AI. Requires a valid Bearer JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Boxer profile generated successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or validation failed",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Boxer could not be confidently identified",
                    content = @Content(schema = @Schema(implementation = BoxerProfileLookupFailureResponse.class))
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
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @PostMapping("/generate-profile")
    public ResponseEntity<GeneratedBoxerProfileResponse> generateProfile(@Valid @RequestBody GenerateBoxerProfileRequest request) {
        return ResponseEntity.ok(
                rankedBoxerService.generateBoxerProfile(
                        request.boxerName(),
                        request.weightClassId()
                )
        );
    }

    @Operation(
            summary = "Get all ranked boxers with isActive status",
            description = "Returns all ranked boxers and indicates whether their batch is active."
    )
    @GetMapping("/with-isActive")
    public ResponseEntity<List<AllTimeRankedBoxerWithBatchStatusResponse>> getAllWithBatchStatus() {
        return ResponseEntity.ok(rankedBoxerService.findAllWithBatchStatus());
    }
}