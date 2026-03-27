package com.nexus.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Standard API error response")
public record ApiErrorResponse(

        @Schema(description = "Date and time when the error occurred", example = "2026-03-27T18:30:00Z")
        OffsetDateTime timestamp,

        @Schema(description = "HTTP status code", example = "404")
        int status,

        @Schema(description = "Short HTTP error label", example = "Not Found")
        String error,

        @Schema(description = "Detailed error message", example = "Weight class not found: 11")
        String message
) {
}