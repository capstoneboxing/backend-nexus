package com.nexus.dto.prediction;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record PredictMatchRequest(
        @NotNull Integer weightClassId,
        @Valid BoxerInput boxerA,
        @Valid BoxerInput boxerB
) {}