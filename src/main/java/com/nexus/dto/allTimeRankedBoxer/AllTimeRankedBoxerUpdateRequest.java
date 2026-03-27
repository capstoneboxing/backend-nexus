package com.nexus.dto.allTimeRankedBoxer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record AllTimeRankedBoxerUpdateRequest(

        // Physical
        @Schema(description = "Height of the boxer in centimeters", example = "178.0")
        @Min(0) @Max(999)
        Double heightCm,

        @Schema(description = "Reach of the boxer in centimeters", example = "188.0")
        @Min(0) @Max(300)
        Double reachCm,

        @Schema(description = "Score representing how naturally aligned the boxer is with the weight class", example = "9.2")
        @Min(1) @Max(10)
        Double weightClassAlignment,

        @Schema(description = "Score representing hand speed", example = "9.6")
        @Min(1) @Max(10)
        Double handSpeed,

        @Schema(description = "Score representing foot speed", example = "9.3")
        @Min(1) @Max(10)
        Double footSpeed,

        @Schema(description = "Score representing strength", example = "8.8")
        @Min(1) @Max(10)
        Double strength,

        @Schema(description = "Score representing endurance", example = "9.1")
        @Min(1) @Max(10)
        Double endurance,

        @Schema(description = "Score representing reaction time", example = "9.4")
        @Min(1) @Max(10)
        Double reactionTime,

        // Technical
        @Schema(description = "Score representing punch accuracy", example = "9.5")
        @Min(1) @Max(10)
        Double punchAccuracy,

        @Schema(description = "Score representing punch variety", example = "9.0")
        @Min(1) @Max(10)
        Double punchVariety,

        @Schema(description = "Score representing defensive guard efficiency", example = "8.9")
        @Min(1) @Max(10)
        Double defensiveGuardEfficiency,

        @Schema(description = "Score representing head movement", example = "9.2")
        @Min(1) @Max(10)
        Double headMovement,

        @Schema(description = "Score representing footwork technique", example = "9.7")
        @Min(1) @Max(10)
        Double footworkTechnique,

        @Schema(description = "Score representing counterpunching ability", example = "9.4")
        @Min(1) @Max(10)
        Double counterpunchingAbility,

        @Schema(description = "Score representing combination efficiency", example = "9.1")
        @Min(1) @Max(10)
        Double combinationEfficiency,

        // Tactical
        @Schema(description = "Score representing ring IQ", example = "9.8")
        @Min(1) @Max(10)
        Double ringIq,

        @Schema(description = "Score representing adaptability during a fight", example = "9.3")
        @Min(1) @Max(10)
        Double adaptabilityMidFight,

        @Schema(description = "Score representing distance control", example = "9.4")
        @Min(1) @Max(10)
        Double distanceControl,

        @Schema(description = "Score representing tempo control", example = "9.1")
        @Min(1) @Max(10)
        Double tempoControl,

        @Schema(description = "Score representing ability to recognize opponent patterns", example = "9.5")
        @Min(1) @Max(10)
        Double opponentPatternRecognition,

        @Schema(description = "Score representing fight planning discipline", example = "9.0")
        @Min(1) @Max(10)
        Double fightPlanningDiscipline,

        // Psychological
        @Schema(description = "Score representing composure under pressure", example = "9.6")
        @Min(1) @Max(10)
        Double composureUnderPressure,

        @Schema(description = "Score representing aggression control", example = "8.9")
        @Min(1) @Max(10)
        Double aggressionControl,

        @Schema(description = "Score representing mental toughness", example = "9.7")
        @Min(1) @Max(10)
        Double mentalToughness,

        @Schema(description = "Score representing focus consistency", example = "9.2")
        @Min(1) @Max(10)
        Double focusConsistency,

        @Schema(description = "Score representing resilience after knockdown", example = "9.0")
        @Min(1) @Max(10)
        Double resilienceAfterKnockdown,

        // Performance
        @Schema(description = "Career win ratio expressed as a decimal", example = "0.89")
        @DecimalMin("0.0") @DecimalMax("1.0")
        Double winRatio,

        @Schema(description = "Career knockout ratio expressed as a decimal", example = "0.54")
        @DecimalMin("0.0") @DecimalMax("1.0")
        Double knockoutRatio,

        @Schema(description = "Score representing title fight experience", example = "9.4")
        @Min(1) @Max(10)
        Double titleFightExperience,

        @Schema(description = "Score representing strength of opposition faced", example = "9.8")
        @Min(1) @Max(10)
        Double strengthOfOpposition,

        @Schema(description = "Score representing recent fight activity", example = "8.1")
        @Min(1) @Max(10)
        Double recentFightActivity,

        @Schema(description = "Score representing consistency of performance", example = "9.3")
        @Min(1) @Max(10)
        Double performanceConsistency,

        @Schema(description = "Optional note describing source confidence or supporting context", example = "Profile generated from widely reported public boxing sources")
        String sourceNote
) {
}