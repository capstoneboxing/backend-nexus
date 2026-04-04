package com.nexus.service;

import com.nexus.dto.prediction.AttributeRange;
import com.nexus.dto.prediction.BoxerInput;
import com.nexus.dto.prediction.CategoryScores;
import com.nexus.model.CategoryWeight;
import com.nexus.model.PerfectBoxer;
import com.nexus.util.AppUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PredictionScoringService {

    private final PredictionNormalizationService normalizationService;

    public PredictionScoringService(PredictionNormalizationService normalizationService) {
        this.normalizationService = normalizationService;
    }

    public CategoryScores scoreBoxer(
            BoxerInput boxer,
            Map<String, AttributeRange> ranges,
            CategoryWeight weights
    ) {
        double physicalAttributeWeight = weights.getPhysicalWeight() / 8.0;
        double technicalAttributeWeight = weights.getTechnicalWeight() / 7.0;
        double tacticalAttributeWeight = weights.getTacticalWeight() / 6.0;
        double psychologicalAttributeWeight = weights.getPsychologicalWeight() / 5.0;
        double experienceAttributeWeight = weights.getExperienceWeight() / 6.0;

        double physical =
                physicalAttributeWeight * m(boxer.heightCm(), ranges.get("heightCm")) +
                        physicalAttributeWeight * m(boxer.reachCm(), ranges.get("reachCm")) +
                        physicalAttributeWeight * s(boxer.weightClassAlignment()) +
                        physicalAttributeWeight * s(boxer.handSpeed()) +
                        physicalAttributeWeight * s(boxer.footSpeed()) +
                        physicalAttributeWeight * s(boxer.strength()) +
                        physicalAttributeWeight * s(boxer.endurance()) +
                        physicalAttributeWeight * s(boxer.reactionTime());

        double technical =
                technicalAttributeWeight * s(boxer.punchAccuracy()) +
                        technicalAttributeWeight * s(boxer.punchVariety()) +
                        technicalAttributeWeight * s(boxer.defensiveGuardEfficiency()) +
                        technicalAttributeWeight * s(boxer.headMovement()) +
                        technicalAttributeWeight * s(boxer.footworkTechnique()) +
                        technicalAttributeWeight * s(boxer.counterpunchingAbility()) +
                        technicalAttributeWeight * s(boxer.combinationEfficiency());

        double tactical =
                tacticalAttributeWeight * s(boxer.ringIq()) +
                        tacticalAttributeWeight * s(boxer.adaptabilityMidFight()) +
                        tacticalAttributeWeight * s(boxer.distanceControl()) +
                        tacticalAttributeWeight * s(boxer.tempoControl()) +
                        tacticalAttributeWeight * s(boxer.opponentPatternRecognition()) +
                        tacticalAttributeWeight * s(boxer.fightPlanningDiscipline());

        double psychological =
                psychologicalAttributeWeight * s(boxer.composureUnderPressure()) +
                        psychologicalAttributeWeight * s(boxer.aggressionControl()) +
                        psychologicalAttributeWeight * s(boxer.mentalToughness()) +
                        psychologicalAttributeWeight * s(boxer.focusConsistency()) +
                        psychologicalAttributeWeight * s(boxer.resilienceAfterKnockdown());

        double experience =
                experienceAttributeWeight * r(boxer.winRatio()) +
                        experienceAttributeWeight * r(boxer.knockoutRatio()) +
                        experienceAttributeWeight * s(boxer.titleFightExperience()) +
                        experienceAttributeWeight * s(boxer.strengthOfOpposition()) +
                        experienceAttributeWeight * s(boxer.recentFightActivity()) +
                        experienceAttributeWeight * s(boxer.performanceConsistency());

        physical = AppUtils.roundTo2DecimalPlaces(physical);
        technical = AppUtils.roundTo2DecimalPlaces(technical);
        tactical = AppUtils.roundTo2DecimalPlaces(tactical);
        psychological = AppUtils.roundTo2DecimalPlaces(psychological);
        experience = AppUtils.roundTo2DecimalPlaces(experience);

        return new CategoryScores(physical, technical, tactical, psychological, experience);
    }

    public CategoryScores scorePerfectBoxer(
            PerfectBoxer boxer,
            Map<String, AttributeRange> ranges,
            CategoryWeight weights
    ) {
        double physicalAttributeWeight = weights.getPhysicalWeight() / 8.0;
        double technicalAttributeWeight = weights.getTechnicalWeight() / 7.0;
        double tacticalAttributeWeight = weights.getTacticalWeight() / 6.0;
        double psychologicalAttributeWeight = weights.getPsychologicalWeight() / 5.0;
        double experienceAttributeWeight = weights.getExperienceWeight() / 6.0;

        double physical =
                physicalAttributeWeight * m(boxer.getHeightCm(), ranges.get("heightCm")) +
                        physicalAttributeWeight * m(boxer.getReachCm(), ranges.get("reachCm")) +
                        physicalAttributeWeight * s(boxer.getWeightClassAlignment()) +
                        physicalAttributeWeight * s(boxer.getHandSpeed()) +
                        physicalAttributeWeight * s(boxer.getFootSpeed()) +
                        physicalAttributeWeight * s(boxer.getStrength()) +
                        physicalAttributeWeight * s(boxer.getEndurance()) +
                        physicalAttributeWeight * s(boxer.getReactionTime());

        double technical =
                technicalAttributeWeight * s(boxer.getPunchAccuracy()) +
                        technicalAttributeWeight * s(boxer.getPunchVariety()) +
                        technicalAttributeWeight * s(boxer.getDefensiveGuardEfficiency()) +
                        technicalAttributeWeight * s(boxer.getHeadMovement()) +
                        technicalAttributeWeight * s(boxer.getFootworkTechnique()) +
                        technicalAttributeWeight * s(boxer.getCounterpunchingAbility()) +
                        technicalAttributeWeight * s(boxer.getCombinationEfficiency());

        double tactical =
                tacticalAttributeWeight * s(boxer.getRingIq()) +
                        tacticalAttributeWeight * s(boxer.getAdaptabilityMidFight()) +
                        tacticalAttributeWeight * s(boxer.getDistanceControl()) +
                        tacticalAttributeWeight * s(boxer.getTempoControl()) +
                        tacticalAttributeWeight * s(boxer.getOpponentPatternRecognition()) +
                        tacticalAttributeWeight * s(boxer.getFightPlanningDiscipline());

        double psychological =
                psychologicalAttributeWeight * s(boxer.getComposureUnderPressure()) +
                        psychologicalAttributeWeight * s(boxer.getAggressionControl()) +
                        psychologicalAttributeWeight * s(boxer.getMentalToughness()) +
                        psychologicalAttributeWeight * s(boxer.getFocusConsistency()) +
                        psychologicalAttributeWeight * s(boxer.getResilienceAfterKnockdown());

        double experience =
                experienceAttributeWeight * r(boxer.getWinRatio()) +
                        experienceAttributeWeight * r(boxer.getKnockoutRatio()) +
                        experienceAttributeWeight * s(boxer.getTitleFightExperience()) +
                        experienceAttributeWeight * s(boxer.getStrengthOfOpposition()) +
                        experienceAttributeWeight * s(boxer.getRecentFightActivity()) +
                        experienceAttributeWeight * s(boxer.getPerformanceConsistency());

        physical = AppUtils.roundTo2DecimalPlaces(physical);
        technical = AppUtils.roundTo2DecimalPlaces(technical);
        tactical = AppUtils.roundTo2DecimalPlaces(tactical);
        psychological = AppUtils.roundTo2DecimalPlaces(psychological);
        experience = AppUtils.roundTo2DecimalPlaces(experience);

        return new CategoryScores(physical, technical, tactical, psychological, experience);
    }

    public double overallScore(CategoryScores scores) {
        double overallScore = scores.physical()
                + scores.technical()
                + scores.tactical()
                + scores.psychological()
                + scores.experience();

        return AppUtils.roundTo2DecimalPlaces(overallScore);
    }

    public double closeness(CategoryScores fighter, CategoryScores perfect, CategoryWeight weights) {
        double distance =
                Math.abs(fighter.physical() - perfect.physical()) +
                        Math.abs(fighter.technical() - perfect.technical()) +
                        Math.abs(fighter.tactical() - perfect.tactical()) +
                        Math.abs(fighter.psychological() - perfect.psychological()) +
                        Math.abs(fighter.experience() - perfect.experience());

        double maxDistance =
                weights.getPhysicalWeight() +
                        weights.getTechnicalWeight() +
                        weights.getTacticalWeight() +
                        weights.getPsychologicalWeight() +
                        weights.getExperienceWeight();

        if (maxDistance == 0.0) {
            return 0.5;
        }

        double closeness = 1.0 - (distance / maxDistance);
        closeness = AppUtils.roundTo2DecimalPlaces(closeness);

        return Math.clamp(closeness, 0.0, 1.0);
    }

    public double probability(double closenessA, double closenessB) {
        double exponent = 3.0;

        double adjustedA = Math.pow(closenessA, exponent);
        double adjustedB = Math.pow(closenessB, exponent);

        double total = adjustedA + adjustedB;

        if (total == 0.0) {
            return 0.5;
        }

        return AppUtils.roundTo2DecimalPlaces(adjustedA / total);
    }

    private double m(Double value, AttributeRange range) {
        return normalizationService.normalizeMeasurement(value, range);
    }

    private double s(Double value) {
        return normalizationService.normalizeRubricScore(value);
    }

    private double r(Double value) {
        return normalizationService.normalizeRatio(value);
    }
}