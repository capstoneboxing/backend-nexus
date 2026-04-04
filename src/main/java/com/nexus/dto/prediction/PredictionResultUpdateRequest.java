package com.nexus.dto.prediction;

import jakarta.validation.constraints.Pattern;

public record PredictionResultUpdateRequest(

        @Pattern(
                regexp = "(?i)BOXER_A|BOXER_B|DRAW|NO_CONTEST",
                message = "matchWinner must be BOXER_A, BOXER_B, DRAW, or NO_CONTEST"
        )
        String matchWinner,

        @Pattern(
                regexp = "(?i)KO|TKO|DECISION|DISQUALIFICATION|NO_CONTEST",
                message = "matchWinMethod must be KO, TKO, DECISION, DISQUALIFICATION, or NO_CONTEST"
        )
        String matchWinMethod
) {
}