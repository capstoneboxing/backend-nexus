package com.nexus.controller;

import com.nexus.dto.error.ApiErrorResponse;
import com.nexus.dto.perfectboxer.PerfectBoxerBatchStatusResponse;
import com.nexus.dto.perfectboxer.PerfectBoxerGenerationRequest;
import com.nexus.dto.perfectboxer.PerfectBoxerGenerationStartedResponse;
import com.nexus.dto.perfectboxer.PerfectBoxerResponse;
import com.nexus.service.PerfectBoxerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/perfect-boxers")
@Tag(name = "Perfect Boxers", description = "Endpoints for generating and managing perfect boxer records")
@SecurityRequirement(name = "bearerAuth")
public class PerfectBoxerController {
    private final PerfectBoxerService perfectBoxerService;

    public PerfectBoxerController(PerfectBoxerService generationService) {
        this.perfectBoxerService = generationService;
    }

    @Operation(
            summary = "Get perfect boxer by ID",
            description = "Returns a fully detailed perfect boxer record including all attributes"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfect boxer retrieved successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Perfect boxer not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<PerfectBoxerResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(perfectBoxerService.getById(id));
    }

    @Operation(
            summary = "Get active perfect boxer by weight class",
            description = "Returns the currently active perfect boxer for a specific weight class"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Active perfect boxer retrieved successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Active perfect boxer or batch not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/active/weight-class/{weightClassId}")
    public ResponseEntity<PerfectBoxerResponse> getActiveByWeightClassId(@PathVariable Integer weightClassId) {
        return ResponseEntity.ok(perfectBoxerService.getActiveByWeightClassId(weightClassId));
    }

    @Operation(
            summary = "Get all active perfect boxers",
            description = "Returns all active perfect boxer records across weight classes"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Active perfect boxers retrieved successfully")
    })
    @GetMapping("/active")
    public ResponseEntity<List<PerfectBoxerResponse>> getAllActivePerfectBoxers() {
        return ResponseEntity.ok(perfectBoxerService.getAllActivePerfectBoxers());
    }

    @Operation(
            summary = "Start perfect boxer generation",
            description = "Starts asynchronous perfect boxer generation for a weight class. Requires a valid Bearer JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Generation started successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or validation failed",
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
    @PostMapping("/generate")
    public ResponseEntity<PerfectBoxerGenerationStartedResponse> generate(
            @Valid @RequestBody PerfectBoxerGenerationRequest request
    ) {
        PerfectBoxerGenerationStartedResponse response =
                perfectBoxerService.generateForWeightClassAsync(request.weightClassId(), request.amount());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Operation(
            summary = "Get batch status",
            description = "Returns the current status of a perfect boxer generation batch. Requires a valid Bearer JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Batch status retrieved successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Batch not found",
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
    @GetMapping("/batches/{batchId}/status")
    public ResponseEntity<PerfectBoxerBatchStatusResponse> getBatchStatus(@PathVariable Integer batchId) {
        return ResponseEntity.ok(perfectBoxerService.getBatchStatus(batchId));
    }

    @Operation(
            summary = "Regenerate active perfect boxer by weight class",
            description = "Recalculates the perfect boxer using the active batch for a weight class. Requires a valid Bearer JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfect boxer regenerated successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Active batch not found",
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
    @PostMapping("/recalculate/weight-class/{weightClassId}")
    public ResponseEntity<PerfectBoxerResponse> recalculateByWeightClass(@PathVariable Integer weightClassId) {
        return ResponseEntity.ok(perfectBoxerService.recalculateForWeightClass(weightClassId));
    }

//    @Operation(
//            summary = "Regenerate perfect boxer by batch",
//            description = "Recalculates the perfect boxer for a specific batch. Requires a valid Bearer JWT."
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Perfect boxer regenerated successfully"),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Batch not found",
//                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
//            ),
//            @ApiResponse(
//                    responseCode = "401",
//                    description = "Authentication required",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = ApiErrorResponse.class)
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "403",
//                    description = "Access denied",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = ApiErrorResponse.class)
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "500",
//                    description = "Unexpected server error",
//                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
//            )
//    })
//    @PostMapping("/recalculate/batch/{batchId}")
//    public ResponseEntity<PerfectBoxerResponse> recalculateByBatch(@PathVariable Integer batchId) {
//        return ResponseEntity.ok(perfectBoxerService.recalculateForBatch(batchId));
//    }
}