package com.nexus.dto.categoryweight;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record CategoryWeightUpdateRequest(

        @NotNull(message = "Physical weight is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Physical weight must be at least 0")
        @DecimalMax(value = "1.0", inclusive = true, message = "Physical weight must be at most 1")
        Double physicalWeight,

        @NotNull(message = "Technical weight is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Technical weight must be at least 0")
        @DecimalMax(value = "1.0", inclusive = true, message = "Technical weight must be at most 1")
        Double technicalWeight,

        @NotNull(message = "Tactical weight is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Tactical weight must be at least 0")
        @DecimalMax(value = "1.0", inclusive = true, message = "Tactical weight must be at most 1")
        Double tacticalWeight,

        @NotNull(message = "Psychological weight is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Psychological weight must be at least 0")
        @DecimalMax(value = "1.0", inclusive = true, message = "Psychological weight must be at most 1")
        Double psychologicalWeight,

        @NotNull(message = "Experience weight is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Experience weight must be at least 0")
        @DecimalMax(value = "1.0", inclusive = true, message = "Experience weight must be at most 1")
        Double experienceWeight
) {
}