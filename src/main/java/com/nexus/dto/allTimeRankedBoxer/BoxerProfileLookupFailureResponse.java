package com.nexus.dto.allTimeRankedBoxer;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response returned when the AI cannot confidently identify a boxer profile")
public record BoxerProfileLookupFailureResponse(

        @Schema(
                description = "Indicates that the boxer profile lookup failed",
                example = "false"
        )
        boolean boxerFound,

        @Schema(
                description = "Confidence score of the AI in identifying the boxer (0.0 to 1.0)",
                example = "0.42"
        )
        double confidence,

        @Schema(
                description = "Explanation of why the boxer could not be confidently identified",
                example = "Could not confidently match the boxer name to a known professional boxer in the selected weight class"
        )
        String message
) {
}