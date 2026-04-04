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

                // Physical
                .heightCm(avg1(boxers, AllTimeRankedBoxer::getHeightCm))
                .reachCm(avg1(boxers, AllTimeRankedBoxer::getReachCm))
                .weightClassAlignment(avg1(boxers, AllTimeRankedBoxer::getWeightClassAlignment))
                .handSpeed(avg1(boxers, AllTimeRankedBoxer::getHandSpeed))
                .footSpeed(avg1(boxers, AllTimeRankedBoxer::getFootSpeed))
                .strength(avg1(boxers, AllTimeRankedBoxer::getStrength))
                .endurance(avg1(boxers, AllTimeRankedBoxer::getEndurance))
                .reactionTime(avg1(boxers, AllTimeRankedBoxer::getReactionTime))

                // Technical
                .punchAccuracy(avg1(boxers, AllTimeRankedBoxer::getPunchAccuracy))
                .punchVariety(avg1(boxers, AllTimeRankedBoxer::getPunchVariety))
                .defensiveGuardEfficiency(avg1(boxers, AllTimeRankedBoxer::getDefensiveGuardEfficiency))
                .headMovement(avg1(boxers, AllTimeRankedBoxer::getHeadMovement))
                .footworkTechnique(avg1(boxers, AllTimeRankedBoxer::getFootworkTechnique))
                .counterpunchingAbility(avg1(boxers, AllTimeRankedBoxer::getCounterpunchingAbility))
                .combinationEfficiency(avg1(boxers, AllTimeRankedBoxer::getCombinationEfficiency))

                // Tactical
                .ringIq(avg1(boxers, AllTimeRankedBoxer::getRingIq))
                .adaptabilityMidFight(avg1(boxers, AllTimeRankedBoxer::getAdaptabilityMidFight))
                .distanceControl(avg1(boxers, AllTimeRankedBoxer::getDistanceControl))
                .tempoControl(avg1(boxers, AllTimeRankedBoxer::getTempoControl))
                .opponentPatternRecognition(avg1(boxers, AllTimeRankedBoxer::getOpponentPatternRecognition))
                .fightPlanningDiscipline(avg1(boxers, AllTimeRankedBoxer::getFightPlanningDiscipline))

                // Psychological
                .composureUnderPressure(avg1(boxers, AllTimeRankedBoxer::getComposureUnderPressure))
                .aggressionControl(avg1(boxers, AllTimeRankedBoxer::getAggressionControl))
                .mentalToughness(avg1(boxers, AllTimeRankedBoxer::getMentalToughness))
                .focusConsistency(avg1(boxers, AllTimeRankedBoxer::getFocusConsistency))
                .resilienceAfterKnockdown(avg1(boxers, AllTimeRankedBoxer::getResilienceAfterKnockdown))

                // Performance
                .winRatio(avg2(boxers, AllTimeRankedBoxer::getWinRatio))
                .knockoutRatio(avg2(boxers, AllTimeRankedBoxer::getKnockoutRatio))
                .titleFightExperience(avg1(boxers, AllTimeRankedBoxer::getTitleFightExperience))
                .strengthOfOpposition(avg1(boxers, AllTimeRankedBoxer::getStrengthOfOpposition))
                .recentFightActivity(avg1(boxers, AllTimeRankedBoxer::getRecentFightActivity))
                .performanceConsistency(avg1(boxers, AllTimeRankedBoxer::getPerformanceConsistency))

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

    private Double avg1(List<AllTimeRankedBoxer> list, Function<AllTimeRankedBoxer, Double> extractor) {
        return AppUtils.roundTo1DecimalPlace(avg(list, extractor));
    }

    private Double avg2(List<AllTimeRankedBoxer> list, Function<AllTimeRankedBoxer, Double> extractor) {
        return AppUtils.roundTo2DecimalPlaces(avg(list, extractor));
    }
}