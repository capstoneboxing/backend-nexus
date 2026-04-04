package com.nexus.dto.prediction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BoxerInput(
        @NotBlank String boxerName,

        @NotNull Double heightCm,
        @NotNull Double reachCm,
        @NotNull Double weightClassAlignment,
        @NotNull Double handSpeed,
        @NotNull Double footSpeed,
        @NotNull Double strength,
        @NotNull Double endurance,
        @NotNull Double reactionTime,

        @NotNull Double punchAccuracy,
        @NotNull Double punchVariety,
        @NotNull Double defensiveGuardEfficiency,
        @NotNull Double headMovement,
        @NotNull Double footworkTechnique,
        @NotNull Double counterpunchingAbility,
        @NotNull Double combinationEfficiency,

        @NotNull Double ringIq,
        @NotNull Double adaptabilityMidFight,
        @NotNull Double distanceControl,
        @NotNull Double tempoControl,
        @NotNull Double opponentPatternRecognition,
        @NotNull Double fightPlanningDiscipline,

        @NotNull Double composureUnderPressure,
        @NotNull Double aggressionControl,
        @NotNull Double mentalToughness,
        @NotNull Double focusConsistency,
        @NotNull Double resilienceAfterKnockdown,

        @NotNull Double winRatio,
        @NotNull Double knockoutRatio,
        @NotNull Double titleFightExperience,
        @NotNull Double strengthOfOpposition,
        @NotNull Double recentFightActivity,
        @NotNull Double performanceConsistency
) {}