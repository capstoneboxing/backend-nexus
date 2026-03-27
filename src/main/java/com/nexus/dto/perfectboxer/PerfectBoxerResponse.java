package com.nexus.dto.perfectboxer;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Response containing a generated perfect boxer record")
public record PerfectBoxerResponse(

        @Schema(description = "Unique ID of the perfect boxer record", example = "3")
        Integer perfectBoxerId,

        @Schema(description = "Batch ID used to generate this perfect boxer", example = "12")
        Integer batchId,

        @Schema(description = "Weight class ID for which the perfect boxer was generated", example = "11")
        Integer weightClassId,

        @Schema(description = "Date and time when the perfect boxer record was created", example = "2026-03-27T18:30:00Z")
        OffsetDateTime createdAt
) {
}