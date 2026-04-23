package com.nexus.seed;

import com.nexus.dto.prediction.BoxerInput;
import com.nexus.dto.prediction.PredictMatchRequest;
import com.nexus.dto.prediction.PredictionResponse;
import com.nexus.dto.allTimeRankedBoxer.GeneratedBoxerResponse;
import com.nexus.repository.PredictionHistoryRepository;
import com.nexus.service.AllTimeRankedBoxerService;
import com.nexus.service.MatchPredictionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PredictionSeedRunner {

    private final MatchPredictionService matchPredictionService;
    private final AllTimeRankedBoxerService rankedBoxerService;
    private final PredictionHistoryRepository predictionHistoryRepository;

    @Value("${app.seed.predictions.enabled:false}")
    private boolean seedEnabled;

    public PredictionSeedRunner(
            MatchPredictionService matchPredictionService,
            AllTimeRankedBoxerService rankedBoxerService,
            PredictionHistoryRepository predictionHistoryRepository
    ) {
        this.matchPredictionService = matchPredictionService;
        this.rankedBoxerService = rankedBoxerService;
        this.predictionHistoryRepository = predictionHistoryRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void seedPredictions() {
        if (!seedEnabled) {
            return;
        }

        // simple guard so it does not run repeatedly
        if (predictionHistoryRepository.count() > 0) {
            System.out.println("[PredictionSeedRunner] Prediction history already has data. Skipping seed.");
            return;
        }

        List<SeedFight> fights = List.of(
                // --- Welterweight (11) examples ---
                new SeedFight(11, "Floyd Mayweather Jr", "Manny Pacquiao", "BOXER_A", "DECISION"),
                new SeedFight(11, "Sugar Ray Leonard", "Thomas Hearns", "BOXER_A", "TKO"),
                new SeedFight(11, "Manny Pacquiao", "Miguel Cotto", "BOXER_A", "TKO"),

                // add the rest of your fights here...
                // new SeedFight(weightClassId, boxerAName, boxerBName, actualWinner, actualMethod)
                // 목표: 10 per weight class
                new SeedFight(9, "Roberto Duran", "Sugar Ray Leonard", "BOXER_A", "DECISION")
        );

        int successCount = 0;

        for (SeedFight fight : fights) {
            try {
                GeneratedBoxerResponse boxerAProfile =
                        rankedBoxerService.generateBoxer(fight.boxerAName(), fight.weightClassId());

                GeneratedBoxerResponse boxerBProfile =
                        rankedBoxerService.generateBoxer(fight.boxerBName(), fight.weightClassId());

                if (!boxerAProfile.boxerFound()) {
                    System.out.printf("[PredictionSeedRunner] Skipping %s vs %s - boxer A not found%n",
                            fight.boxerAName(), fight.boxerBName());
                    continue;
                }

                if (!boxerBProfile.boxerFound()) {
                    System.out.printf("[PredictionSeedRunner] Skipping %s vs %s - boxer B not found%n",
                            fight.boxerAName(), fight.boxerBName());
                    continue;
                }

                BoxerInput boxerA = toBoxerInput(boxerAProfile);
                BoxerInput boxerB = toBoxerInput(boxerBProfile);

                PredictMatchRequest request = new PredictMatchRequest(
                        fight.weightClassId(),
                        boxerA,
                        boxerB
                );

                PredictionResponse response = matchPredictionService.predict(request);

                successCount++;

                System.out.printf("""
                        [PredictionSeedRunner] Seeded prediction #%d
                        Fight: %s vs %s
                        Predicted winner: %s
                        Actual winner: %s
                        Actual method: %s
                        
                        """,
                        response.predictionId(),
                        fight.boxerAName(),
                        fight.boxerBName(),
                        response.predictedWinner(),
                        fight.actualWinner(),
                        fight.actualMethod()
                );

            } catch (Exception e) {
                System.out.printf("[PredictionSeedRunner] Failed to seed %s vs %s: %s%n",
                        fight.boxerAName(), fight.boxerBName(), e.getMessage());
            }
        }

        System.out.printf("[PredictionSeedRunner] Done. Seeded %d predictions.%n", successCount);
    }

    private BoxerInput toBoxerInput(GeneratedBoxerResponse profile) {
        return new BoxerInput(
                profile.boxerName(),
                profile.confidence(),

                profile.heightCm(),
                profile.reachCm(),
                profile.weightClassAlignment(),
                profile.handSpeed(),
                profile.footSpeed(),
                profile.strength(),
                profile.endurance(),
                profile.reactionTime(),

                profile.punchAccuracy(),
                profile.punchVariety(),
                profile.defensiveGuardEfficiency(),
                profile.headMovement(),
                profile.footworkTechnique(),
                profile.counterpunchingAbility(),
                profile.combinationEfficiency(),

                profile.ringIq(),
                profile.adaptabilityMidFight(),
                profile.distanceControl(),
                profile.tempoControl(),
                profile.opponentPatternRecognition(),
                profile.fightPlanningDiscipline(),

                profile.composureUnderPressure(),
                profile.aggressionControl(),
                profile.mentalToughness(),
                profile.focusConsistency(),
                profile.resilienceAfterKnockdown(),

                profile.winRatio(),
                profile.knockoutRatio(),
                profile.titleFightExperience(),
                profile.strengthOfOpposition(),
                profile.recentFightActivity(),
                profile.performanceConsistency()
        );
    }

    private record SeedFight(
            Integer weightClassId,
            String boxerAName,
            String boxerBName,
            String actualWinner,
            String actualMethod
    ) {}
}