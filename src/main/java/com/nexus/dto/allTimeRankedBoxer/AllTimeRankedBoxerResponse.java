package com.nexus.dto.allTimeRankedBoxer;

import java.time.OffsetDateTime;

public record AllTimeRankedBoxerResponse(
        Integer rankedBoxerId,
        Integer batchId,
        Integer weightClassId,
        String boxerName,
        Integer rankingPosition,

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
        String sourceNote,
        OffsetDateTime generatedAt
) {
}
