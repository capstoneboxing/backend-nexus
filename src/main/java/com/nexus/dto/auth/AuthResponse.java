package com.nexus.dto.auth;

public record AuthResponse(
        String token,
        String tokenType,
        String username
) {
}