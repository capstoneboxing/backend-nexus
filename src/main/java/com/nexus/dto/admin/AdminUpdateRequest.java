package com.nexus.dto.admin;

import jakarta.validation.constraints.Size;

public record AdminUpdateRequest(
        @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
        String username,

        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        String password
) {
}