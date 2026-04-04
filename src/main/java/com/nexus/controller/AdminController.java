//package com.nexus.controller;
//
//import com.nexus.dto.admin.AdminCreateRequest;
//import com.nexus.dto.admin.AdminResponse;
//import com.nexus.dto.admin.AdminUpdateRequest;
//import com.nexus.dto.error.ApiErrorResponse;
//import com.nexus.service.AdminService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//
//@RestController
//@RequestMapping("api/v1/admins")
//@Tag(name = "Admins", description = "Endpoints for managing admin accounts")
//@SecurityRequirement(name = "bearerAuth")
//public class AdminController {
//
//    private final AdminService adminService;
//
//    public AdminController(AdminService adminService) {
//        this.adminService = adminService;
//    }
//
//    @Operation(
//            summary = "Create a new admin",
//            description = "Creates a new admin account. Requires a valid Bearer JWT with ADMIN access."
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "201", description = "Admin created successfully"),
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Invalid request or validation failed",
//                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
//            ),
//            @ApiResponse(
//                    responseCode = "409",
//                    description = "Username already exists",
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
//    @PostMapping
//    public ResponseEntity<AdminResponse> createAdmin(@Valid @RequestBody AdminCreateRequest request) {
//        AdminResponse response = adminService.createAdmin(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//
//    @Operation(
//            summary = "Update admin",
//            description = "Updates username and/or password for an existing admin. Requires a valid Bearer JWT with ADMIN access."
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Admin updated successfully"),
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Invalid request or validation failed",
//                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Admin not found",
//                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
//            ),
//            @ApiResponse(
//                    responseCode = "409",
//                    description = "Username already exists",
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
//    @PutMapping("/{id}")
//    public ResponseEntity<AdminResponse> updateAdmin(@PathVariable Integer id,
//                                                     @Valid @RequestBody AdminUpdateRequest request) {
//        return ResponseEntity.ok(adminService.updateAdmin(id, request));
//    }
//}