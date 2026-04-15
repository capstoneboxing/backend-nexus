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
                physicalAttributeWeight * normalizeMeasurement(boxer.heightCm(), ranges.get("heightCm")) +
                        physicalAttributeWeight * normalizeMeasurement(boxer.reachCm(), ranges.get("reachCm")) +
                        physicalAttributeWeight * normalizeScore(boxer.weightClassAlignment()) +
                        physicalAttributeWeight * normalizeScore(boxer.handSpeed()) +
                        physicalAttributeWeight * normalizeScore(boxer.footSpeed()) +
                        physicalAttributeWeight * normalizeScore(boxer.strength()) +
                        physicalAttributeWeight * normalizeScore(boxer.endurance()) +
                        physicalAttributeWeight * normalizeScore(boxer.reactionTime());

        double technical =
                technicalAttributeWeight * normalizeScore(boxer.punchAccuracy()) +
                        technicalAttributeWeight * normalizeScore(boxer.punchVariety()) +
                        technicalAttributeWeight * normalizeScore(boxer.defensiveGuardEfficiency()) +
                        technicalAttributeWeight * normalizeScore(boxer.headMovement()) +
                        technicalAttributeWeight * normalizeScore(boxer.footworkTechnique()) +
                        technicalAttributeWeight * normalizeScore(boxer.counterpunchingAbility()) +
                        technicalAttributeWeight * normalizeScore(boxer.combinationEfficiency());

        double tactical =
                tacticalAttributeWeight * normalizeScore(boxer.ringIq()) +
                        tacticalAttributeWeight * normalizeScore(boxer.adaptabilityMidFight()) +
                        tacticalAttributeWeight * normalizeScore(boxer.distanceControl()) +
                        tacticalAttributeWeight * normalizeScore(boxer.tempoControl()) +
                        tacticalAttributeWeight * normalizeScore(boxer.opponentPatternRecognition()) +
                        tacticalAttributeWeight * normalizeScore(boxer.fightPlanningDiscipline());

        double psychological =
                psychologicalAttributeWeight * normalizeScore(boxer.composureUnderPressure()) +
                        psychologicalAttributeWeight * normalizeScore(boxer.aggressionControl()) +
                        psychologicalAttributeWeight * normalizeScore(boxer.mentalToughness()) +
                        psychologicalAttributeWeight * normalizeScore(boxer.focusConsistency()) +
                        psychologicalAttributeWeight * normalizeScore(boxer.resilienceAfterKnockdown());

        double experience =
                experienceAttributeWeight * normalizeRatio(boxer.winRatio()) +
                        experienceAttributeWeight * normalizeRatio(boxer.knockoutRatio()) +
                        experienceAttributeWeight * normalizeScore(boxer.titleFightExperience()) +
                        experienceAttributeWeight * normalizeScore(boxer.strengthOfOpposition()) +
                        experienceAttributeWeight * normalizeScore(boxer.recentFightActivity()) +
                        experienceAttributeWeight * normalizeScore(boxer.performanceConsistency());

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
                physicalAttributeWeight * normalizeMeasurement(boxer.getHeightCm(), ranges.get("heightCm")) +
                        physicalAttributeWeight * normalizeMeasurement(boxer.getReachCm(), ranges.get("reachCm")) +
                        physicalAttributeWeight * normalizeScore(boxer.getWeightClassAlignment()) +
                        physicalAttributeWeight * normalizeScore(boxer.getHandSpeed()) +
                        physicalAttributeWeight * normalizeScore(boxer.getFootSpeed()) +
                        physicalAttributeWeight * normalizeScore(boxer.getStrength()) +
                        physicalAttributeWeight * normalizeScore(boxer.getEndurance()) +
                        physicalAttributeWeight * normalizeScore(boxer.getReactionTime());

        double technical =
                technicalAttributeWeight * normalizeScore(boxer.getPunchAccuracy()) +
                        technicalAttributeWeight * normalizeScore(boxer.getPunchVariety()) +
                        technicalAttributeWeight * normalizeScore(boxer.getDefensiveGuardEfficiency()) +
                        technicalAttributeWeight * normalizeScore(boxer.getHeadMovement()) +
                        technicalAttributeWeight * normalizeScore(boxer.getFootworkTechnique()) +
                        technicalAttributeWeight * normalizeScore(boxer.getCounterpunchingAbility()) +
                        technicalAttributeWeight * normalizeScore(boxer.getCombinationEfficiency());

        double tactical =
                tacticalAttributeWeight * normalizeScore(boxer.getRingIq()) +
                        tacticalAttributeWeight * normalizeScore(boxer.getAdaptabilityMidFight()) +
                        tacticalAttributeWeight * normalizeScore(boxer.getDistanceControl()) +
                        tacticalAttributeWeight * normalizeScore(boxer.getTempoControl()) +
                        tacticalAttributeWeight * normalizeScore(boxer.getOpponentPatternRecognition()) +
                        tacticalAttributeWeight * normalizeScore(boxer.getFightPlanningDiscipline());

        double psychological =
                psychologicalAttributeWeight * normalizeScore(boxer.getComposureUnderPressure()) +
                        psychologicalAttributeWeight * normalizeScore(boxer.getAggressionControl()) +
                        psychologicalAttributeWeight * normalizeScore(boxer.getMentalToughness()) +
                        psychologicalAttributeWeight * normalizeScore(boxer.getFocusConsistency()) +
                        psychologicalAttributeWeight * normalizeScore(boxer.getResilienceAfterKnockdown());

        double experience =
                experienceAttributeWeight * normalizeRatio(boxer.getWinRatio()) +
                        experienceAttributeWeight * normalizeRatio(boxer.getKnockoutRatio()) +
                        experienceAttributeWeight * normalizeScore(boxer.getTitleFightExperience()) +
                        experienceAttributeWeight * normalizeScore(boxer.getStrengthOfOpposition()) +
                        experienceAttributeWeight * normalizeScore(boxer.getRecentFightActivity()) +
                        experienceAttributeWeight * normalizeScore(boxer.getPerformanceConsistency());

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

    private double normalizeMeasurement(Double value, AttributeRange range) {
        return normalizationService.normalizeMeasurement(value, range);
    }

    private double normalizeScore(Double value) {
        return normalizationService.normalizeRubricScore(value);
    }

    private double normalizeRatio(Double value) {
        return normalizationService.normalizeRatio(value);
    }

    public double applyAttributeConfidence(double baseCloseness, double attributeConfidence) {
        double confidence = Math.clamp(attributeConfidence, 0.0, 1.0);
        double adjusted = 0.5 + confidence * (baseCloseness - 0.5);
        return AppUtils.roundTo2DecimalPlaces(Math.clamp(adjusted, 0.0, 1.0));
    }
}