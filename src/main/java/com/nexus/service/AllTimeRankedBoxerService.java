package com.nexus.service;

import com.nexus.dto.allTimeRankedBoxer.AllTimeRankedBoxerResponse;
import com.nexus.dto.allTimeRankedBoxer.AllTimeRankedBoxerUpdateRequest;
import com.nexus.model.AllTimeRankedBoxer;
import com.nexus.model.PerfectBoxerGenerationBatch;
import com.nexus.repository.AllTimeRankedBoxerRepository;
import com.nexus.repository.PerfectBoxerGenerationBatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AllTimeRankedBoxerService {

    private final AllTimeRankedBoxerRepository rankedBoxerRepository;
    private final PerfectBoxerGenerationBatchRepository batchRepository;

    public AllTimeRankedBoxerService(
            AllTimeRankedBoxerRepository rankedBoxerRepository,
            PerfectBoxerGenerationBatchRepository batchRepository
    ) {
        this.rankedBoxerRepository = rankedBoxerRepository;
        this.batchRepository = batchRepository;
    }

    public List<AllTimeRankedBoxerResponse> findAll() {
        return rankedBoxerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AllTimeRankedBoxerResponse findById(Integer id) {
        AllTimeRankedBoxer boxer = rankedBoxerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ranked boxer not found: " + id));

        return mapToResponse(boxer);
    }

    public List<AllTimeRankedBoxerResponse> findByBatchId(Integer batchId) {
        return rankedBoxerRepository.findByBatchIdOrderByRankingPositionAsc(batchId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<AllTimeRankedBoxerResponse> findByWeightClassId(Integer weightClassId) {
        return rankedBoxerRepository.findByWeightClassIdOrderByRankingPositionAsc(weightClassId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<AllTimeRankedBoxerResponse> findActiveByWeightClassId(Integer weightClassId) {
        PerfectBoxerGenerationBatch activeBatch = batchRepository.findByWeightClassIdAndIsActiveTrue(weightClassId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No active batch found for weight class: " + weightClassId
                ));

        return rankedBoxerRepository.findByBatchIdOrderByRankingPositionAsc(activeBatch.getBatchId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public AllTimeRankedBoxerResponse update(Integer id, AllTimeRankedBoxerUpdateRequest request) {
        AllTimeRankedBoxer boxer = rankedBoxerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ranked boxer not found: " + id));

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

        if (request.winRatio() != null) boxer.setWinRatio(round2(request.winRatio()));
        if (request.knockoutRatio() != null) boxer.setKnockoutRatio(round2(request.knockoutRatio()));
        if (request.titleFightExperience() != null) boxer.setTitleFightExperience(request.titleFightExperience());
        if (request.strengthOfOpposition() != null) boxer.setStrengthOfOpposition(request.strengthOfOpposition());
        if (request.recentFightActivity() != null) boxer.setRecentFightActivity(request.recentFightActivity());
        if (request.performanceConsistency() != null) boxer.setPerformanceConsistency(request.performanceConsistency());

        if (request.sourceNote() != null) boxer.setSourceNote(request.sourceNote());

        AllTimeRankedBoxer saved = rankedBoxerRepository.save(boxer);
        return mapToResponse(saved);
    }

    public void delete(Integer id) {
        if (!rankedBoxerRepository.existsById(id)) {
            throw new IllegalArgumentException("Ranked boxer not found: " + id);
        }
        rankedBoxerRepository.deleteById(id);
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private AllTimeRankedBoxerResponse mapToResponse(AllTimeRankedBoxer boxer) {
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