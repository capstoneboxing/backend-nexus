package com.nexus.dto.prediction;

import jakarta.validation.constraints.Pattern;

public record PredictionResultUpdateRequest(

        @Pattern(
                regexp = "(?i)BOXER_A|BOXER_B|DRAW",
                message = "matchWinner must be BOXER_A, BOXER_B or DRAW"
        )
        String matchWinner,

        @Pattern(
                regexp = "(?i)KO|TKO|DECISION|DISQUALIFICATION",
                message = "matchWinMethod must be KO, TKO, DECISION or DISQUALIFICATION"
        )
        String matchWinMethod
) {
}