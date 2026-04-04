package com.nexus.dto.categoryweight;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Category weight response")
public record CategoryWeightResponse(

        @Schema(description = "Weight class ID", example = "11")
        Integer weightClassId,

        @Schema(description = "Physical category weight", example = "0.20")
        Double physicalWeight,

        @Schema(description = "Technical category weight", example = "0.25")
        Double technicalWeight,

        @Schema(description = "Tactical category weight", example = "0.25")
        Double tacticalWeight,

        @Schema(description = "Psychological category weight", example = "0.15")
        Double psychologicalWeight,

        @Schema(description = "Experience category weight", example = "0.15")
        Double experienceWeight
) {
}