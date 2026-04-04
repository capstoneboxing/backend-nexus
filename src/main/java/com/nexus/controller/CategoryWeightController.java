package com.nexus.controller;

import com.nexus.dto.categoryweight.CategoryWeightResponse;
import com.nexus.dto.categoryweight.CategoryWeightUpdateRequest;
import com.nexus.dto.error.ApiErrorResponse;
import com.nexus.service.CategoryWeightService;
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
@RequestMapping(path = "/api/v1/category-weights")
@Tag(name = "Category Weights", description = "Endpoints for viewing and updating category weights by weight class")
@SecurityRequirement(name = "bearerAuth")
public class CategoryWeightController {

    private final CategoryWeightService categoryWeightService;

    public CategoryWeightController(CategoryWeightService categoryWeightService) {
        this.categoryWeightService = categoryWeightService;
    }

    @Operation(
            summary = "Get all category weights",
            description = "Returns all category weight records across all weight classes. Requires a valid Bearer JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category weights retrieved successfully"),
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
            )
    })
    @GetMapping
    public ResponseEntity<List<CategoryWeightResponse>> getAll() {
        return ResponseEntity.ok(categoryWeightService.getAll());
    }

    @Operation(
            summary = "Get category weights by weight class ID",
            description = "Returns the category weights for a specific weight class. Requires a valid Bearer JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category weights retrieved successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category weights not found",
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
    })
    @GetMapping("/weight-class/{weightClassId}")
    public ResponseEntity<CategoryWeightResponse> getByWeightClassId(
            @Parameter(description = "Weight class ID", example = "11")
            @PathVariable Integer weightClassId
    ) {
        return ResponseEntity.ok(categoryWeightService.getByWeightClassId(weightClassId));
    }

    @Operation(
            summary = "Update category weights by weight class ID",
            description = "Updates the category weights for a specific weight class. The weights must add up to 1.0. Requires a valid Bearer JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category weights updated successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or weights do not add up to 1.0",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category weights not found",
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
    })
    @PutMapping("/weight-class/{weightClassId}")
    public ResponseEntity<CategoryWeightResponse> updateByWeightClassId(
            @Parameter(description = "Weight class ID", example = "11")
            @PathVariable Integer weightClassId,
            @Valid @RequestBody CategoryWeightUpdateRequest request
    ) {
        return ResponseEntity.ok(categoryWeightService.updateByWeightClassId(weightClassId, request));
    }
}