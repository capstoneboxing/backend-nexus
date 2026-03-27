package com.nexus.service;

import com.nexus.model.AllTimeRankedBoxer;
import com.nexus.model.PerfectBoxer;
import com.nexus.util.AppUtils;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Component
public class PerfectBoxerCalculator {

    public PerfectBoxer buildFromRankedBoxers(Integer batchId, Integer weightClassId, List<AllTimeRankedBoxer> boxers) {
        if (boxers == null || boxers.isEmpty()) {
            throw new IllegalArgumentException("Ranked boxer list cannot be empty.");
        }

        return PerfectBoxer.builder()
                .batchId(batchId)
                .weightClassId(weightClassId)

                .heightCm(avg(boxers, AllTimeRankedBoxer::getHeightCm))
                .reachCm(avg(boxers, AllTimeRankedBoxer::getReachCm))
                .weightClassAlignment(avg(boxers, AllTimeRankedBoxer::getWeightClassAlignment))
                .handSpeed(avg(boxers, AllTimeRankedBoxer::getHandSpeed))
                .footSpeed(avg(boxers, AllTimeRankedBoxer::getFootSpeed))
                .strength(avg(boxers, AllTimeRankedBoxer::getStrength))
                .endurance(avg(boxers, AllTimeRankedBoxer::getEndurance))
                .reactionTime(avg(boxers, AllTimeRankedBoxer::getReactionTime))

                .punchAccuracy(avg(boxers, AllTimeRankedBoxer::getPunchAccuracy))
                .punchVariety(avg(boxers, AllTimeRankedBoxer::getPunchVariety))
                .defensiveGuardEfficiency(avg(boxers, AllTimeRankedBoxer::getDefensiveGuardEfficiency))
                .headMovement(avg(boxers, AllTimeRankedBoxer::getHeadMovement))
                .footworkTechnique(avg(boxers, AllTimeRankedBoxer::getFootworkTechnique))
                .counterpunchingAbility(avg(boxers, AllTimeRankedBoxer::getCounterpunchingAbility))
                .combinationEfficiency(avg(boxers, AllTimeRankedBoxer::getCombinationEfficiency))

                .ringIq(avg(boxers, AllTimeRankedBoxer::getRingIq))
                .adaptabilityMidFight(avg(boxers, AllTimeRankedBoxer::getAdaptabilityMidFight))
                .distanceControl(avg(boxers, AllTimeRankedBoxer::getDistanceControl))
                .tempoControl(avg(boxers, AllTimeRankedBoxer::getTempoControl))
                .opponentPatternRecognition(avg(boxers, AllTimeRankedBoxer::getOpponentPatternRecognition))
                .fightPlanningDiscipline(avg(boxers, AllTimeRankedBoxer::getFightPlanningDiscipline))

                .composureUnderPressure(avg(boxers, AllTimeRankedBoxer::getComposureUnderPressure))
                .aggressionControl(avg(boxers, AllTimeRankedBoxer::getAggressionControl))
                .mentalToughness(avg(boxers, AllTimeRankedBoxer::getMentalToughness))
                .focusConsistency(avg(boxers, AllTimeRankedBoxer::getFocusConsistency))
                .resilienceAfterKnockdown(avg(boxers, AllTimeRankedBoxer::getResilienceAfterKnockdown))

                .winRatio(AppUtils.roundTo2DecimalPlaces(avg(boxers, AllTimeRankedBoxer::getWinRatio)))
                .knockoutRatio(AppUtils.roundTo2DecimalPlaces(avg(boxers, AllTimeRankedBoxer::getKnockoutRatio)))
                .titleFightExperience(avg(boxers, AllTimeRankedBoxer::getTitleFightExperience))
                .strengthOfOpposition(avg(boxers, AllTimeRankedBoxer::getStrengthOfOpposition))
                .recentFightActivity(avg(boxers, AllTimeRankedBoxer::getRecentFightActivity))
                .performanceConsistency(avg(boxers, AllTimeRankedBoxer::getPerformanceConsistency))

                .createdAt(OffsetDateTime.now())
                .build();
    }

    private Double avg(List<AllTimeRankedBoxer> list, Function<AllTimeRankedBoxer, Double> extractor) {
        return list.stream()
                .map(extractor)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElseThrow(() -> new IllegalArgumentException("Cannot calculate average from null values"));
    }
}