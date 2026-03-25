package com.nexus.dto.allTimeRankedBoxer;

public record GeneratedBoxerProfileResponse(
        boolean boxerFound,
        double confidence,
        String matchReason,
        Integer weightClassId,
        String boxerName,

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

        String sourceNote
) {
}