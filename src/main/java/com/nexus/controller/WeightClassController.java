package com.nexus.controller;

import com.nexus.dto.error.ApiErrorResponse;
import com.nexus.dto.weightClass.WeightClassResponse;
import com.nexus.service.WeightClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/weight-classes")
@Tag(name = "Weight Classes", description = "Public endpoints for retrieving boxing weight classes")
public class WeightClassController {

    private final WeightClassService weightClassService;

    @Autowired
    public WeightClassController(WeightClassService weightClassService) {
        this.weightClassService = weightClassService;
    }

    @Operation(
            summary = "Get all weight classes",
            description = "Returns all boxing weight classes. This endpoint is public."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Weight classes retrieved successfully"),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<WeightClassResponse>> getWeightClasses() {
        return ResponseEntity.ok(weightClassService.getWeightClasses());
    }

    @Operation(
            summary = "Get weight class by ID",
            description = "Returns a single weight class by its ID. This endpoint is public."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Weight class found"),
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
    @GetMapping(path = "/{id}")
    public ResponseEntity<WeightClassResponse> getWeightClassById(@PathVariable Integer id) {
        return ResponseEntity.ok(weightClassService.getWeightClassById(id));
    }

    @Operation(
            summary = "Get weight class by name",
            description = "Returns a single weight class by its class name. This endpoint is public."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Weight class found"),
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
    @GetMapping(path = "/name/{className}")
    public ResponseEntity<WeightClassResponse> getWeightClassByName(@PathVariable String className) {
        return ResponseEntity.ok(weightClassService.getWeightClassByName(className));
    }
}