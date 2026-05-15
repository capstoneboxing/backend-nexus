package com.nexus.service;

import com.nexus.dto.prediction.AttributeRange;
import com.nexus.dto.prediction.BoxerInput;
import com.nexus.dto.prediction.CategoryScores;
import com.nexus.model.CategoryWeight;
import com.nexus.model.PerfectBoxer;
import com.nexus.util.AppUtils;
import com.nexus.util.CategoryAttributeWeights;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PredictionScoringService {

    private final PredictionNormalizationService normalizationService;

    private static final double TOTAL_PHYSICAL_ATTRIBUTES = 8.0;
    private static final double TOTAL_TECHNICAL_ATTRIBUTES = 7.0;
    private static final double TOTAL_TACTICAL_ATTRIBUTES = 6.0;
    private static final double TOTAL_PSYCHOLOGICAL_ATTRIBUTES = 5.0;
    private static final double TOTAL_EXPERIENCE_ATTRIBUTES = 6.0;

    public PredictionScoringService(PredictionNormalizationService normalizationService) {
        this.normalizationService = normalizationService;
    }

    public CategoryScores scoreBoxer(
            BoxerInput boxer,
            Map<String, AttributeRange> ranges,
            CategoryWeight weights
    ) {

        CategoryAttributeWeights categoryAttributeWeights = computeAttributeWeights(weights);

        double physicalAttributeWeight = categoryAttributeWeights.physical();
        double technicalAttributeWeight = categoryAttributeWeights.technical();
        double tacticalAttributeWeight = categoryAttributeWeights.tactical();
        double psychologicalAttributeWeight = categoryAttributeWeights.psychological();
        double experienceAttributeWeight = categoryAttributeWeights.experience();

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

        return roundScores(physical, technical, tactical, psychological, experience);
    }

    public CategoryScores scorePerfectBoxer(
            PerfectBoxer boxer,
            Map<String, AttributeRange> ranges,
            CategoryWeight weights
    ) {
        CategoryAttributeWeights categoryAttributeWeights = computeAttributeWeights(weights);

        double physicalAttributeWeight = categoryAttributeWeights.physical();
        double technicalAttributeWeight = categoryAttributeWeights.technical();
        double tacticalAttributeWeight = categoryAttributeWeights.tactical();
        double psychologicalAttributeWeight = categoryAttributeWeights.psychological();
        double experienceAttributeWeight = categoryAttributeWeights.experience();

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

        return roundScores(physical, technical, tactical, psychological, experience);
    }

    private CategoryAttributeWeights computeAttributeWeights(CategoryWeight weights) {
        return new CategoryAttributeWeights(
                weights.getPhysicalWeight() / TOTAL_PHYSICAL_ATTRIBUTES,
                weights.getTechnicalWeight() / TOTAL_TECHNICAL_ATTRIBUTES,
                weights.getTacticalWeight() / TOTAL_TACTICAL_ATTRIBUTES,
                weights.getPsychologicalWeight() / TOTAL_PSYCHOLOGICAL_ATTRIBUTES,
                weights.getExperienceWeight() / TOTAL_EXPERIENCE_ATTRIBUTES
        );
    }

    private CategoryScores roundScores(
            double physical,
            double technical,
            double tactical,
            double psychological,
            double experience
    ) {
        return new CategoryScores(
                AppUtils.roundTo2DecimalPlaces(physical),
                AppUtils.roundTo2DecimalPlaces(technical),
                AppUtils.roundTo2DecimalPlaces(tactical),
                AppUtils.roundTo2DecimalPlaces(psychological),
                AppUtils.roundTo2DecimalPlaces(experience)
        );
    }

    public double overallScore(CategoryScores scores) {
        double overallScore = scores.physical()
                + scores.technical()
                + scores.tactical()
                + scores.psychological()
                + scores.experience();

        return AppUtils.roundTo2DecimalPlaces(overallScore);
    }

    public double closeness(CategoryScores fighter, CategoryScores perfect) {
        double distance =
                Math.abs(fighter.physical() - perfect.physical()) +
                        Math.abs(fighter.technical() - perfect.technical()) +
                        Math.abs(fighter.tactical() - perfect.tactical()) +
                        Math.abs(fighter.psychological() - perfect.psychological()) +
                        Math.abs(fighter.experience() - perfect.experience());

        double closeness = 1.0 - distance;

        return AppUtils.roundTo2DecimalPlaces(
                Math.clamp(closeness, 0.0, 1.0)
        );
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
        double confidencePenaltyStrength = 0.35;

        double adjusted =
                baseCloseness - (
                        (1.0 - confidence)
                                * confidencePenaltyStrength
                                * (baseCloseness - 0.5)
                );

        return AppUtils.roundTo2DecimalPlaces(
                Math.clamp(adjusted, 0.0, 1.0)
        );
    }
}