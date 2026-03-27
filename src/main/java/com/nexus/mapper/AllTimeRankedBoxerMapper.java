package com.nexus.mapper;

import com.nexus.dto.allTimeRankedBoxer.AllTimeRankedBoxerResponse;
import com.nexus.model.AllTimeRankedBoxer;
import org.springframework.stereotype.Component;

@Component
public class AllTimeRankedBoxerMapper {

    public AllTimeRankedBoxerResponse toResponse(AllTimeRankedBoxer boxer) {
        return new AllTimeRankedBoxerResponse(
                boxer.getRankedBoxerId(),
                boxer.getBatchId(),
                boxer.getWeightClassId(),
                boxer.getBoxerName(),
                boxer.getRankingPosition(),
                boxer.getHeightCm(),
                boxer.getReachCm(),
                boxer.getWeightClassAlignment(),
                boxer.getHandSpeed(),
                boxer.getFootSpeed(),
                boxer.getStrength(),
                boxer.getEndurance(),
                boxer.getReactionTime(),
                boxer.getPunchAccuracy(),
                boxer.getPunchVariety(),
                boxer.getDefensiveGuardEfficiency(),
                boxer.getHeadMovement(),
                boxer.getFootworkTechnique(),
                boxer.getCounterpunchingAbility(),
                boxer.getCombinationEfficiency(),
                boxer.getRingIq(),
                boxer.getAdaptabilityMidFight(),
                boxer.getDistanceControl(),
                boxer.getTempoControl(),
                boxer.getOpponentPatternRecognition(),
                boxer.getFightPlanningDiscipline(),
                boxer.getComposureUnderPressure(),
                boxer.getAggressionControl(),
                boxer.getMentalToughness(),
                boxer.getFocusConsistency(),
                boxer.getResilienceAfterKnockdown(),
                boxer.getWinRatio(),
                boxer.getKnockoutRatio(),
                boxer.getTitleFightExperience(),
                boxer.getStrengthOfOpposition(),
                boxer.getRecentFightActivity(),
                boxer.getPerformanceConsistency(),
                boxer.getSourceNote(),
                boxer.getGeneratedAt()
        );
    }
}