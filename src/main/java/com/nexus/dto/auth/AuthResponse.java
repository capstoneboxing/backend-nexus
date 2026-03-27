package com.nexus.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response returned after successful authentication")
public record AuthResponse(

        @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0eXdhaW5lIn0.signature")
        String token,

        @Schema(description = "Token type to be used in the Authorization header", example = "Bearer")
        String tokenType,

        @Schema(description = "Authenticated admin username", example = "tywaine")
        String username
) {
}