package com.nexus.service;

import com.nexus.dto.allTimeRankedBoxer.AllTimeRankedBoxerResponse;
import com.nexus.dto.allTimeRankedBoxer.AllTimeRankedBoxerUpdateRequest;
import com.nexus.dto.allTimeRankedBoxer.AllTimeRankedBoxerWithBatchStatusResponse;
import com.nexus.dto.allTimeRankedBoxer.GeneratedBoxerResponse;
import com.nexus.exception.BoxerProfileLookupException;
import com.nexus.exception.ResourceNotFoundException;
import com.nexus.mapper.AllTimeRankedBoxerMapper;
import com.nexus.model.AllTimeRankedBoxer;
import com.nexus.model.PerfectBoxerGenerationBatch;
import com.nexus.model.WeightClass;
import com.nexus.repository.AllTimeRankedBoxerRepository;
import com.nexus.repository.PerfectBoxerGenerationBatchRepository;
import com.nexus.repository.WeightClassRepository;
import com.nexus.service.ai.SingleBoxerAiService;
import com.nexus.util.AppUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AllTimeRankedBoxerService {
    private final AllTimeRankedBoxerRepository rankedBoxerRepository;
    private final PerfectBoxerGenerationBatchRepository batchRepository;
    private final WeightClassRepository weightClassRepository;
    private final SingleBoxerAiService singleBoxerAiService;
    private final AllTimeRankedBoxerMapper allTimeRankedBoxerMapper;

    public AllTimeRankedBoxerService(
            AllTimeRankedBoxerRepository rankedBoxerRepository,
            PerfectBoxerGenerationBatchRepository batchRepository,
            WeightClassRepository weightClassRepository,
            SingleBoxerAiService singleBoxerAiService,
            AllTimeRankedBoxerMapper allTimeRankedBoxerMapper
    ) {
        this.rankedBoxerRepository = rankedBoxerRepository;
        this.batchRepository = batchRepository;
        this.weightClassRepository = weightClassRepository;
        this.singleBoxerAiService = singleBoxerAiService;
        this.allTimeRankedBoxerMapper = allTimeRankedBoxerMapper;
    }

    public AllTimeRankedBoxerResponse findById(Integer id) {
        AllTimeRankedBoxer boxer = rankedBoxerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ranked boxer not found: " + id));

        return allTimeRankedBoxerMapper.toResponse(boxer);
    }

    public List<AllTimeRankedBoxerResponse> findByBatchId(Integer batchId) {
        return rankedBoxerRepository.findByBatchIdOrderByRankingPositionAsc(batchId)
                .stream()
                .map(allTimeRankedBoxerMapper::toResponse)
                .toList();
    }

    public List<AllTimeRankedBoxerResponse> findByWeightClassId(Integer weightClassId) {
        return rankedBoxerRepository.findByWeightClassIdOrderByRankingPositionAsc(weightClassId)
                .stream()
                .map(allTimeRankedBoxerMapper::toResponse)
                .toList();
    }

    public List<AllTimeRankedBoxerResponse> findAllActive() {
        List<PerfectBoxerGenerationBatch> activeBatches = batchRepository.findByIsActiveTrue();

        if (activeBatches.isEmpty()) {
            return List.of();
        }

        List<Integer> batchIds = activeBatches.stream()
                .map(PerfectBoxerGenerationBatch::getBatchId)
                .toList();

        return rankedBoxerRepository
                .findByBatchIdInOrderByWeightClassIdAscRankingPositionAsc(batchIds)
                .stream()
                .map(allTimeRankedBoxerMapper::toResponse)
                .toList();
    }

    public List<AllTimeRankedBoxerResponse> findActiveByWeightClassId(Integer weightClassId) {
        PerfectBoxerGenerationBatch activeBatch = batchRepository.findByWeightClassIdAndIsActiveTrue(weightClassId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No active batch found for weight class: " + weightClassId
                ));

        return rankedBoxerRepository.findByBatchIdOrderByRankingPositionAsc(activeBatch.getBatchId())
                .stream()
                .map(allTimeRankedBoxerMapper::toResponse)
                .toList();
    }

    public List<AllTimeRankedBoxerWithBatchStatusResponse> findAllWithBatchStatus() {

        List<AllTimeRankedBoxer> boxers = rankedBoxerRepository.findAll();

        // Get all batches once (avoid querying inside loop)
        List<PerfectBoxerGenerationBatch> batches = batchRepository.findAll();

        // Map batchId -> isActive
        Map<Integer, Boolean> batchStatusMap = batches.stream()
                .collect(Collectors.toMap(
                        PerfectBoxerGenerationBatch::getBatchId,
                        PerfectBoxerGenerationBatch::getIsActive
                ));

        return boxers.stream()
                .map(boxer -> new AllTimeRankedBoxerWithBatchStatusResponse(
                        allTimeRankedBoxerMapper.toResponse(boxer),
                        batchStatusMap.getOrDefault(boxer.getBatchId(), false)
                ))
                .toList();
    }

    @Transactional
    public AllTimeRankedBoxerResponse update(Integer id, AllTimeRankedBoxerUpdateRequest request) {
        AllTimeRankedBoxer boxer = rankedBoxerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ranked boxer not found: " + id));

        if (request.heightCm() != null) boxer.setHeightCm(request.heightCm());
        if (request.reachCm() != null) boxer.setReachCm(request.reachCm());
        if (request.weightClassAlignment() != null) boxer.setWeightClassAlignment(request.weightClassAlignment());
        if (request.handSpeed() != null) boxer.setHandSpeed(request.handSpeed());
        if (request.footSpeed() != null) boxer.setFootSpeed(request.footSpeed());
        if (request.strength() != null) boxer.setStrength(request.strength());
        if (request.endurance() != null) boxer.setEndurance(request.endurance());
        if (request.reactionTime() != null) boxer.setReactionTime(request.reactionTime());

        if (request.punchAccuracy() != null) boxer.setPunchAccuracy(request.punchAccuracy());
        if (request.punchVariety() != null) boxer.setPunchVariety(request.punchVariety());
        if (request.defensiveGuardEfficiency() != null) boxer.setDefensiveGuardEfficiency(request.defensiveGuardEfficiency());
        if (request.headMovement() != null) boxer.setHeadMovement(request.headMovement());
        if (request.footworkTechnique() != null) boxer.setFootworkTechnique(request.footworkTechnique());
        if (request.counterpunchingAbility() != null) boxer.setCounterpunchingAbility(request.counterpunchingAbility());
        if (request.combinationEfficiency() != null) boxer.setCombinationEfficiency(request.combinationEfficiency());

        if (request.ringIq() != null) boxer.setRingIq(request.ringIq());
        if (request.adaptabilityMidFight() != null) boxer.setAdaptabilityMidFight(request.adaptabilityMidFight());
        if (request.distanceControl() != null) boxer.setDistanceControl(request.distanceControl());
        if (request.tempoControl() != null) boxer.setTempoControl(request.tempoControl());
        if (request.opponentPatternRecognition() != null) boxer.setOpponentPatternRecognition(request.opponentPatternRecognition());
        if (request.fightPlanningDiscipline() != null) boxer.setFightPlanningDiscipline(request.fightPlanningDiscipline());

        if (request.composureUnderPressure() != null) boxer.setComposureUnderPressure(request.composureUnderPressure());
        if (request.aggressionControl() != null) boxer.setAggressionControl(request.aggressionControl());
        if (request.mentalToughness() != null) boxer.setMentalToughness(request.mentalToughness());
        if (request.focusConsistency() != null) boxer.setFocusConsistency(request.focusConsistency());
        if (request.resilienceAfterKnockdown() != null) boxer.setResilienceAfterKnockdown(request.resilienceAfterKnockdown());

        if (request.winRatio() != null) boxer.setWinRatio(AppUtils.roundTo2DecimalPlaces(request.winRatio()));
        if (request.knockoutRatio() != null) boxer.setKnockoutRatio(AppUtils.roundTo2DecimalPlaces(request.knockoutRatio()));
        if (request.titleFightExperience() != null) boxer.setTitleFightExperience(request.titleFightExperience());
        if (request.strengthOfOpposition() != null) boxer.setStrengthOfOpposition(request.strengthOfOpposition());
        if (request.recentFightActivity() != null) boxer.setRecentFightActivity(request.recentFightActivity());
        if (request.performanceConsistency() != null) boxer.setPerformanceConsistency(request.performanceConsistency());

        if (request.sourceNote() != null) boxer.setSourceNote(request.sourceNote());

        AllTimeRankedBoxer saved = rankedBoxerRepository.save(boxer);
        return allTimeRankedBoxerMapper.toResponse(saved);
    }

    public GeneratedBoxerResponse generateBoxer(String boxerName, Integer weightClassId) {
        WeightClass weightClass = weightClassRepository.findById(weightClassId)
                .orElseThrow(() -> new ResourceNotFoundException("Weight class not found: " + weightClassId));

        var aiResponse = singleBoxerAiService.getBoxerProfile(boxerName, weightClass.getClassName());

        boolean boxerFound = Boolean.TRUE.equals(aiResponse.getBoxerFound());
        double confidence = aiResponse.getConfidence() != null ? aiResponse.getConfidence() : 0.0;
        String matchReason = aiResponse.getMatchReason();

        if (!boxerFound || aiResponse.getBoxer() == null) {
            throw new BoxerProfileLookupException(
                    "AI could not confidently identify boxer '" + boxerName +
                            "' for weight class '" + weightClass.getClassName() +
                            "'. Reason: " + matchReason,
                    confidence
            );
        }

        var boxer = aiResponse.getBoxer();

        return new GeneratedBoxerResponse(
                true,
                confidence,
                matchReason,
                weightClassId,
                boxer.getBoxerName(),
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
                AppUtils.roundTo2DecimalPlaces(boxer.getWinRatio()),
                AppUtils.roundTo2DecimalPlaces(boxer.getKnockoutRatio()),
                boxer.getTitleFightExperience(),
                boxer.getStrengthOfOpposition(),
                boxer.getRecentFightActivity(),
                boxer.getPerformanceConsistency(),
                boxer.getSourceNote()
        );
    }

    @Async("taskExecutor")
    public CompletableFuture<GeneratedBoxerResponse> generateBoxerAsync(
            String boxerName,
            Integer weightClassId
    ) {
        GeneratedBoxerResponse response =
                generateBoxer(boxerName, weightClassId);

        return CompletableFuture.completedFuture(response);
    }
}