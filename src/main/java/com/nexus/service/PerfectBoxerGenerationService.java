package com.nexus.service;

import com.nexus.dto.TopBoxerAiProfile;
import com.nexus.dto.TopBoxerAiResponse;
import com.nexus.dto.perfectboxer.PerfectBoxerResponse;
import com.nexus.model.*;
import com.nexus.repository.AllTimeRankedBoxerRepository;
import com.nexus.repository.PerfectBoxerGenerationBatchRepository;
import com.nexus.repository.PerfectBoxerRepository;
import com.nexus.repository.WeightClassRepository;
import com.nexus.service.ai.TopBoxerAiService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PerfectBoxerGenerationService {

    private final WeightClassRepository weightClassRepository;
    private final PerfectBoxerGenerationBatchRepository batchRepository;
    private final AllTimeRankedBoxerRepository rankedBoxerRepository;
    private final PerfectBoxerRepository perfectBoxerRepository;
    private final TopBoxerAiService topBoxerAiService;
    private final PerfectBoxerCalculator perfectBoxerCalculator;

    public PerfectBoxerGenerationService(
            WeightClassRepository weightClassRepository,
            PerfectBoxerGenerationBatchRepository batchRepository,
            AllTimeRankedBoxerRepository rankedBoxerRepository,
            PerfectBoxerRepository perfectBoxerRepository,
            TopBoxerAiService topBoxerAiService,
            PerfectBoxerCalculator perfectBoxerCalculator
    ) {
        this.weightClassRepository = weightClassRepository;
        this.batchRepository = batchRepository;
        this.rankedBoxerRepository = rankedBoxerRepository;
        this.perfectBoxerRepository = perfectBoxerRepository;
        this.topBoxerAiService = topBoxerAiService;
        this.perfectBoxerCalculator = perfectBoxerCalculator;
    }

    @Transactional
    public PerfectBoxerResponse generateForWeightClass(Integer weightClassId) {
        WeightClass weightClass = weightClassRepository.findById(weightClassId)
                .orElseThrow(() -> new IllegalArgumentException("Weight class not found: " + weightClassId));

        batchRepository.deactivateActiveBatchByWeightClassId(weightClassId);

        PerfectBoxerGenerationBatch batch = batchRepository.save(
                PerfectBoxerGenerationBatch.builder()
                        .weightClassId(weightClassId)
                        .createdAt(OffsetDateTime.now())
                        .isActive(true)
                        .build()
        );

        TopBoxerAiResponse aiResponse = topBoxerAiService.getTop10ForWeightClass(weightClass.getClassName());

        validateAiResponse(aiResponse);

        List<AllTimeRankedBoxer> rankedBoxers = mapAndSaveRankedBoxers(
                batch.getBatchId(),
                weightClassId,
                aiResponse.getBoxers()
        );

        PerfectBoxer perfectBoxer = perfectBoxerCalculator.buildFromRankedBoxers(
                batch.getBatchId(),
                weightClassId,
                rankedBoxers
        );

        PerfectBoxer savedPerfectBoxer = perfectBoxerRepository.save(perfectBoxer);
        System.out.println("Created Admin: " + savedPerfectBoxer);
        return mapToResponse(savedPerfectBoxer);
    }

    private void validateAiResponse(TopBoxerAiResponse aiResponse) {
        if (aiResponse == null || aiResponse.getBoxers() == null || aiResponse.getBoxers().size() != 10) {
            throw new IllegalArgumentException("AI must return exactly 10 boxers.");
        }

        List<Integer> rankings = aiResponse.getBoxers().stream()
                .map(TopBoxerAiProfile::getRankingPosition)
                .sorted()
                .toList();

        for (int i = 0; i < 10; i++) {
            if (!rankings.get(i).equals(i + 1)) {
                throw new IllegalArgumentException("AI rankings must be exactly 1 through 10.");
            }
        }
    }

    private List<AllTimeRankedBoxer> mapAndSaveRankedBoxers(
            Integer batchId,
            Integer weightClassId,
            List<TopBoxerAiProfile> aiProfiles
    ) {
        List<AllTimeRankedBoxer> saved = new ArrayList<>();

        aiProfiles.stream()
                .sorted(Comparator.comparing(TopBoxerAiProfile::getRankingPosition))
                .forEach(profile -> {
                    AllTimeRankedBoxer boxer = AllTimeRankedBoxer.builder()
                            .batchId(batchId)
                            .weightClassId(weightClassId)
                            .boxerName(profile.getBoxerName())
                            .rankingPosition(profile.getRankingPosition())
                            .heightCm(profile.getHeightCm())
                            .reachCm(profile.getReachCm())
                            .weightClassAlignment(profile.getWeightClassAlignment())
                            .handSpeed(profile.getHandSpeed())
                            .footSpeed(profile.getFootSpeed())
                            .strength(profile.getStrength())
                            .endurance(profile.getEndurance())
                            .reactionTime(profile.getReactionTime())
                            .punchAccuracy(profile.getPunchAccuracy())
                            .punchVariety(profile.getPunchVariety())
                            .defensiveGuardEfficiency(profile.getDefensiveGuardEfficiency())
                            .headMovement(profile.getHeadMovement())
                            .footworkTechnique(profile.getFootworkTechnique())
                            .counterpunchingAbility(profile.getCounterpunchingAbility())
                            .combinationEfficiency(profile.getCombinationEfficiency())
                            .ringIq(profile.getRingIq())
                            .adaptabilityMidFight(profile.getAdaptabilityMidFight())
                            .distanceControl(profile.getDistanceControl())
                            .tempoControl(profile.getTempoControl())
                            .opponentPatternRecognition(profile.getOpponentPatternRecognition())
                            .fightPlanningDiscipline(profile.getFightPlanningDiscipline())
                            .composureUnderPressure(profile.getComposureUnderPressure())
                            .aggressionControl(profile.getAggressionControl())
                            .mentalToughness(profile.getMentalToughness())
                            .focusConsistency(profile.getFocusConsistency())
                            .resilienceAfterKnockdown(profile.getResilienceAfterKnockdown())
                            .winRatio(roundTo2DecimalPlaces(profile.getWinRatio()))
                            .knockoutRatio(roundTo2DecimalPlaces(profile.getKnockoutRatio()))
                            .titleFightExperience(profile.getTitleFightExperience())
                            .strengthOfOpposition(profile.getStrengthOfOpposition())
                            .recentFightActivity(profile.getRecentFightActivity())
                            .performanceConsistency(profile.getPerformanceConsistency())
                            .sourceNote(profile.getSourceNote())
                            .generatedAt(OffsetDateTime.now())
                            .build();

                    saved.add(rankedBoxerRepository.save(boxer));
                });

        return saved;
    }

    private double roundTo2DecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private PerfectBoxerResponse mapToResponse(PerfectBoxer perfectBoxer) {
        return new PerfectBoxerResponse(
                perfectBoxer.getPerfectBoxerId(),
                perfectBoxer.getBatchId(),
                perfectBoxer.getWeightClassId(),
                perfectBoxer.getCreatedAt()
        );
    }
}