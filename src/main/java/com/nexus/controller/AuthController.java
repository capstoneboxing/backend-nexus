package com.nexus.controller;

import com.nexus.dto.admin.AdminResponse;
import com.nexus.dto.auth.AuthRequest;
import com.nexus.dto.auth.AuthResponse;
import com.nexus.dto.error.ApiErrorResponse;
import com.nexus.security.JwtService;
import com.nexus.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AdminService adminService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,  AdminService adminService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.adminService = adminService;
    }

    @Operation(
            summary = "Login",
            description = "Authenticates an admin and returns a JWT access token. This endpoint is public."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or validation failed",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid username or password",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );

            String token = jwtService.generateToken(
                    (org.springframework.security.core.userdetails.UserDetails) Objects.requireNonNull(authentication.getPrincipal())
            );

            return ResponseEntity.ok(
                    new AuthResponse(token, "Bearer", request.username())
            );
        } catch (BadCredentialsException ex) {
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

    @Operation(
            summary = "Get current authenticated admin",
            description = "Returns details of the currently authenticated admin using the JWT token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Admin retrieved successfully"),
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
                    description = "Admin not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<AdminResponse> getCurrentAdmin(Authentication authentication) {

        String username = authentication.getName();

        return ResponseEntity.ok(
                adminService.getAdminByUsername(username)
        );
    }
}
