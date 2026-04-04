package com.nexus.dto.perfectboxer;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Full perfect boxer data including all attributes")
public record PerfectBoxerResponse(

        Integer perfectBoxerId,
        Integer batchId,
        Integer weightClassId,

        // Physical
        Double heightCm,
        Double reachCm,
        Double weightClassAlignment,
        Double handSpeed,
        Double footSpeed,
        Double strength,
        Double endurance,
        Double reactionTime,

        // Technical
        Double punchAccuracy,
        Double punchVariety,
        Double defensiveGuardEfficiency,
        Double headMovement,
        Double footworkTechnique,
        Double counterpunchingAbility,
        Double combinationEfficiency,

        // Tactical
        Double ringIq,
        Double adaptabilityMidFight,
        Double distanceControl,
        Double tempoControl,
        Double opponentPatternRecognition,
        Double fightPlanningDiscipline,

        // Psychological
        Double composureUnderPressure,
        Double aggressionControl,
        Double mentalToughness,
        Double focusConsistency,
        Double resilienceAfterKnockdown,

        // Performance
        Double winRatio,
        Double knockoutRatio,
        Double titleFightExperience,
        Double strengthOfOpposition,
        Double recentFightActivity,
        Double performanceConsistency,

        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

}