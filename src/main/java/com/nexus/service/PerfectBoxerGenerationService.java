package com.nexus.service;

import com.nexus.dto.ai.TopBoxerAiProfile;
import com.nexus.dto.ai.TopBoxerAiResponse;
import com.nexus.exception.ResourceNotFoundException;
import com.nexus.model.AllTimeRankedBoxer;
import com.nexus.model.PerfectBoxer;
import com.nexus.model.PerfectBoxerGenerationBatch;
import com.nexus.model.WeightClass;
import com.nexus.repository.AllTimeRankedBoxerRepository;
import com.nexus.repository.PerfectBoxerGenerationBatchRepository;
import com.nexus.repository.PerfectBoxerRepository;
import com.nexus.repository.WeightClassRepository;
import com.nexus.service.ai.TopBoxerAiService;
import com.nexus.util.AppUtils;
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
    public void runGeneration(Integer batchId) {
        PerfectBoxerGenerationBatch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found: " + batchId));

        WeightClass weightClass = weightClassRepository.findById(batch.getWeightClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Weight class not found: " + batch.getWeightClassId()));

        Integer amount = batch.getAmount();

        TopBoxerAiResponse aiResponse =
                topBoxerAiService.getTopBoxersForWeightClass(weightClass.getClassName(), amount);

        validateAiResponse(aiResponse, amount);

        List<AllTimeRankedBoxer> rankedBoxers = mapAndSaveRankedBoxers(
                batchId,
                batch.getWeightClassId(),
                aiResponse.getBoxers()
        );

        PerfectBoxer perfectBoxer = perfectBoxerCalculator.buildFromRankedBoxers(
                batchId,
                batch.getWeightClassId(),
                rankedBoxers
        );

        perfectBoxerRepository.save(perfectBoxer);
    }

    private void validateAiResponse(TopBoxerAiResponse aiResponse, Integer amount) {
        if (aiResponse == null || aiResponse.getBoxers() == null || aiResponse.getBoxers().size() != amount) {
            throw new IllegalArgumentException("AI must return exactly " + amount + " boxers.");
        }

        List<Integer> rankings = aiResponse.getBoxers().stream()
                .map(TopBoxerAiProfile::getRankingPosition)
                .sorted()
                .toList();

        for (int i = 0; i < amount; i++) {
            if (!rankings.get(i).equals(i + 1)) {
                throw new IllegalArgumentException("AI rankings must be exactly 1 through " + amount + ".");
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
                            .winRatio(AppUtils.roundTo2DecimalPlaces(profile.getWinRatio()))
                            .knockoutRatio(AppUtils.roundTo2DecimalPlaces(profile.getKnockoutRatio()))
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
}