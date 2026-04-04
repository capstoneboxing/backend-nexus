package com.nexus.mapper;

import com.nexus.dto.perfectboxer.PerfectBoxerResponse;
import com.nexus.model.PerfectBoxer;
import org.springframework.stereotype.Component;

@Component
public class PerfectBoxerMapper {

    public PerfectBoxerResponse toResponse(PerfectBoxer p) {
        return new PerfectBoxerResponse(
                p.getPerfectBoxerId(),
                p.getBatchId(),
                p.getWeightClassId(),

                p.getHeightCm(),
                p.getReachCm(),
                p.getWeightClassAlignment(),
                p.getHandSpeed(),
                p.getFootSpeed(),
                p.getStrength(),
                p.getEndurance(),
                p.getReactionTime(),

                p.getPunchAccuracy(),
                p.getPunchVariety(),
                p.getDefensiveGuardEfficiency(),
                p.getHeadMovement(),
                p.getFootworkTechnique(),
                p.getCounterpunchingAbility(),
                p.getCombinationEfficiency(),

                p.getRingIq(),
                p.getAdaptabilityMidFight(),
                p.getDistanceControl(),
                p.getTempoControl(),
                p.getOpponentPatternRecognition(),
                p.getFightPlanningDiscipline(),

                p.getComposureUnderPressure(),
                p.getAggressionControl(),
                p.getMentalToughness(),
                p.getFocusConsistency(),
                p.getResilienceAfterKnockdown(),

                p.getWinRatio(),
                p.getKnockoutRatio(),
                p.getTitleFightExperience(),
                p.getStrengthOfOpposition(),
                p.getRecentFightActivity(),
                p.getPerformanceConsistency(),

                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}