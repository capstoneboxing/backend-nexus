package com.nexus.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Response containing admin account details")
public record AdminResponse(

        @Schema(description = "Unique admin ID", example = "1")
        Integer adminId,

        @Schema(description = "Admin username", example = "tywaine")
        String username,

        @Schema(description = "Date and time when the admin account was created", example = "2026-03-27T18:30:00Z")
        OffsetDateTime createdAt
) {
}