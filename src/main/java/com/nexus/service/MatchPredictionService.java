package com.nexus.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.dto.prediction.*;
import com.nexus.exception.ResourceNotFoundException;
import com.nexus.model.*;
import com.nexus.repository.*;
import com.nexus.service.ai.PredictionExplanationService;
import com.nexus.util.AppUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
public class MatchPredictionService {

    private final PerfectBoxerGenerationBatchRepository batchRepository;
    private final PerfectBoxerRepository perfectBoxerRepository;
    private final AllTimeRankedBoxerRepository rankedBoxerRepository;
    private final CategoryWeightRepository categoryWeightRepository;
    private final PredictionHistoryRepository predictionHistoryRepository;
    private final PredictionNormalizationService normalizationService;
    private final PredictionScoringService scoringService;
    private final PredictionExplanationService explanationService;
    private final ObjectMapper objectMapper;
    private final WeightClassRepository weightClassRepository;

    public MatchPredictionService(
            PerfectBoxerGenerationBatchRepository batchRepository,
            PerfectBoxerRepository perfectBoxerRepository,
            AllTimeRankedBoxerRepository rankedBoxerRepository,
            CategoryWeightRepository categoryWeightRepository,
            PredictionHistoryRepository predictionHistoryRepository,
            PredictionNormalizationService normalizationService,
            PredictionScoringService scoringService,
            PredictionExplanationService explanationService,
            ObjectMapper objectMapper,
            WeightClassRepository weightClassRepository
    ) {
        this.batchRepository = batchRepository;
        this.perfectBoxerRepository = perfectBoxerRepository;
        this.rankedBoxerRepository = rankedBoxerRepository;
        this.categoryWeightRepository = categoryWeightRepository;
        this.predictionHistoryRepository = predictionHistoryRepository;
        this.normalizationService = normalizationService;
        this.scoringService = scoringService;
        this.explanationService = explanationService;
        this.objectMapper = objectMapper;
        this.weightClassRepository = weightClassRepository;
    }

    @Transactional
    public PredictionResponse predict(PredictMatchRequest request) {
        PerfectBoxerGenerationBatch activeBatch = batchRepository.findByWeightClassIdAndIsActiveTrue(request.weightClassId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No active batch found for weight class: " + request.weightClassId()
                ));

        PerfectBoxer perfectBoxer = perfectBoxerRepository.findByBatchId(activeBatch.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No perfect boxer found for active batch."
                ));

        CategoryWeight weights = categoryWeightRepository.findById(request.weightClassId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prediction category weights not found for weight class: " + request.weightClassId()
                ));

        WeightClass weightClass = weightClassRepository.findById(request.weightClassId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Weight class not found: " + request.weightClassId()
                ));

        List<AllTimeRankedBoxer> rankedBoxers =
                rankedBoxerRepository.findByBatchIdOrderByRankingPositionAsc(activeBatch.getBatchId());

        Map<String, AttributeRange> ranges = normalizationService.buildRanges(rankedBoxers);

        CategoryScores scoresA = scoringService.scoreBoxer(request.boxerA(), ranges, weights);
        CategoryScores scoresB = scoringService.scoreBoxer(request.boxerB(), ranges, weights);
        CategoryScores perfectScores = scoringService.scorePerfectBoxer(perfectBoxer, ranges, weights);

        double overallScoreA = scoringService.overallScore(scoresA);
        double overallScoreB = scoringService.overallScore(scoresB);
        double overallPerfectScore = scoringService.overallScore(perfectScores);

        double baseClosenessA = scoringService.closeness(scoresA, perfectScores);
        double baseClosenessB = scoringService.closeness(scoresB, perfectScores);

        double closenessA = scoringService.applyAttributeConfidence(
                baseClosenessA,
                request.boxerA().attributeConfidence()
        );

        double closenessB = scoringService.applyAttributeConfidence(
                baseClosenessB,
                request.boxerB().attributeConfidence()
        );

        double probabilityA = scoringService.probability(closenessA, closenessB);
        double probabilityB = AppUtils.roundTo2DecimalPlaces(1.0 - probabilityA);

        String predictedWinner;

        if (closenessA == closenessB) {
            predictedWinner = "DRAW";
        } else if (closenessA > closenessB) {
            predictedWinner = "BOXER_A";
        } else {
            predictedWinner = "BOXER_B";
        }

        String explanation = explanationService.generateExplanation(
                weightClass.getClassName(),
                predictedWinner,
                request.boxerA(),
                request.boxerB(),
                scoresA,
                scoresB,
                baseClosenessA,
                baseClosenessB,
                closenessA,
                closenessB,
                probabilityA,
                probabilityB,
                weights
        );

        JsonNode breakdownSnapshot = buildBreakdownSnapshot(
                scoresA,
                scoresB,
                perfectScores,
                overallScoreA,
                overallScoreB,
                overallPerfectScore,
                request.boxerA().attributeConfidence(),
                request.boxerB().attributeConfidence(),
                baseClosenessA,
                baseClosenessB,
                closenessA,
                closenessB,
                probabilityA,
                probabilityB,
                predictedWinner,
                explanation
        );

        PredictionHistory saved = predictionHistoryRepository.save(
                PredictionHistory.builder()
                        .boxerAName(request.boxerA().boxerName())
                        .boxerBName(request.boxerB().boxerName())
                        .predictedWinner(predictedWinner)
                        .matchWinner(null)
                        .matchWinMethod(null)
                        .weightClassId(request.weightClassId())
                        .boxerAClosenessScore(closenessA)
                        .boxerBClosenessScore(closenessB)
                        .probabilityA(probabilityA)
                        .probabilityB(probabilityB)
                        .breakdownSnapshot(breakdownSnapshot)
                        .predictionDate(OffsetDateTime.now())
                        .build()
        );

        return new PredictionResponse(
                saved.getPredictionId(),
                saved.getBoxerAName(),
                saved.getBoxerBName(),
                saved.getPredictedWinner(),
                saved.getWeightClassId(),
                saved.getBoxerAClosenessScore(),
                saved.getBoxerBClosenessScore(),
                saved.getProbabilityA(),
                saved.getProbabilityB(),
                explanation,
                saved.getPredictionDate()
        );
    }

    private JsonNode buildBreakdownSnapshot(
            CategoryScores scoresA,
            CategoryScores scoresB,
            CategoryScores perfectScores,
            double overallScoreA,
            double overallScoreB,
            double overallPerfectScore,
            double attributeConfidenceA,
            double attributeConfidenceB,
            double baseClosenessA,
            double baseClosenessB,
            double closenessA,
            double closenessB,
            double probabilityA,
            double probabilityB,
            String predictedWinner,
            String explanation
    ) {
        Map<String, Object> snapshot = new LinkedHashMap<>();

        snapshot.put("predictedWinner", predictedWinner);

        snapshot.put("overallScores", Map.of(
                "boxerA", overallScoreA,
                "boxerB", overallScoreB,
                "perfectBoxer", overallPerfectScore
        ));

        snapshot.put("attributeConfidence", Map.of(
                "boxerA", attributeConfidenceA,
                "boxerB", attributeConfidenceB
        ));

        snapshot.put("closeness", Map.of(
                "boxerA", Map.of(
                        "base", baseClosenessA,
                        "adjusted", closenessA
                ),
                "boxerB", Map.of(
                        "base", baseClosenessB,
                        "adjusted", closenessB
                )
        ));

        snapshot.put("probabilities", Map.of(
                "boxerA", probabilityA,
                "boxerB", probabilityB
        ));

        snapshot.put("categoryScores", Map.of(
                "boxerA", Map.of(
                        "physical", scoresA.physical(),
                        "technical", scoresA.technical(),
                        "tactical", scoresA.tactical(),
                        "psychological", scoresA.psychological(),
                        "experience", scoresA.experience()
                ),
                "boxerB", Map.of(
                        "physical", scoresB.physical(),
                        "technical", scoresB.technical(),
                        "tactical", scoresB.tactical(),
                        "psychological", scoresB.psychological(),
                        "experience", scoresB.experience()
                ),
                "perfectBoxer", Map.of(
                        "physical", perfectScores.physical(),
                        "technical", perfectScores.technical(),
                        "tactical", perfectScores.tactical(),
                        "psychological", perfectScores.psychological(),
                        "experience", perfectScores.experience()
                )
        ));

        snapshot.put("aiExplanation", explanation);

        return objectMapper.valueToTree(snapshot);
    }
}