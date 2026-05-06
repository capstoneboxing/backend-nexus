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

        if (predictionHistoryRepository.count() > 0) {
            System.out.println("[PredictionSeedRunner] Prediction history already has data. Skipping seed.");
            return;
        }

        List<SeedFight> fights = List.of(
                // --- Minimumweight (1) ---
                new SeedFight(1, "Ricardo Lopez", "Hideyuki Ohashi", "BOXER_A", "TKO"),
                new SeedFight(1, "Ricardo Lopez", "Rosendo Alvarez", "DRAW", "DECISION"),

                // --- Light Flyweight (2) ---
                new SeedFight(2, "Michael Carbajal", "Humberto Gonzalez", "BOXER_A", "KO"),
                new SeedFight(2, "Humberto Gonzalez", "Michael Carbajal", "BOXER_A", "DECISION"),

                // --- Flyweight (3) ---
                new SeedFight(3, "Roman Gonzalez", "Akira Yaegashi", "BOXER_A", "TKO"),
                new SeedFight(3, "Roman Gonzalez", "Brian Viloria", "BOXER_A", "TKO"),

                // --- Super Flyweight (4) ---
                new SeedFight(4, "Naoya Inoue", "Omar Narvaez", "BOXER_A", "TKO"),
                new SeedFight(4, "Juan Francisco Estrada", "Roman Gonzalez", "BOXER_A", "DECISION"),

                // --- Bantamweight (5) ---
                new SeedFight(5, "Nonito Donaire", "Fernando Montiel", "BOXER_A", "KO"),
                new SeedFight(5, "Naoya Inoue", "Nonito Donaire", "BOXER_A", "DECISION"),

                // --- Super Bantamweight (6) ---
                new SeedFight(6, "Murodjon Akhmadaliev", "Daniel Roman", "BOXER_A", "DECISION"),
                new SeedFight(6, "Naoya Inoue", "Stephen Fulton", "BOXER_A", "TKO"),

                // --- Featherweight (7) ---
                new SeedFight(7, "Salvador Sanchez", "Wilfredo Gomez", "BOXER_A", "TKO"),
                new SeedFight(7, "Manny Pacquiao", "Marco Antonio Barrera", "BOXER_A", "TKO"),

                // --- Super Featherweight (8) ---
                new SeedFight(8, "Floyd Mayweather Jr", "Genaro Hernandez", "BOXER_A", "TKO"),
                new SeedFight(8, "Manny Pacquiao", "Juan Manuel Marquez", "DRAW", "DECISION"),

                // --- Lightweight (9) ---
                new SeedFight(9, "Floyd Mayweather Jr", "Jose Luis Castillo", "BOXER_A", "DECISION"),
                new SeedFight(9, "Teofimo Lopez", "Vasiliy Lomachenko", "BOXER_A", "DECISION"),

                // --- Super Lightweight (10) ---
                new SeedFight(10, "Terence Crawford", "Viktor Postol", "BOXER_A", "DECISION"),
                new SeedFight(10, "Josh Taylor", "Jose Ramirez", "BOXER_A", "DECISION"),

                // --- Welterweight (11) ---
                new SeedFight(11, "Floyd Mayweather Jr", "Manny Pacquiao", "BOXER_A", "DECISION"),
                new SeedFight(11, "Terence Crawford", "Errol Spence Jr", "BOXER_A", "TKO"),

                // --- Super Welterweight (12) ---
                new SeedFight(12, "Floyd Mayweather Jr", "Canelo Alvarez", "BOXER_A", "DECISION"),
                new SeedFight(12, "Jermell Charlo", "Brian Castano", "DRAW", "DECISION"),

                // --- Middleweight (13) ---
                new SeedFight(13, "Gennady Golovkin", "David Lemieux", "BOXER_A", "TKO"),
                new SeedFight(13, "Canelo Alvarez", "Gennady Golovkin", "BOXER_A", "DECISION"),

                // --- Super Middleweight (14) ---
                new SeedFight(14, "Andre Ward", "Carl Froch", "BOXER_A", "DECISION"),

                // --- Light Heavyweight (15) ---
                new SeedFight(15, "Roy Jones Jr", "Virgil Hill", "BOXER_A", "KO"),

                // --- Cruiserweight (16) ---
                new SeedFight(16, "Oleksandr Usyk", "Murat Gassiev", "BOXER_A", "DECISION"),
                new SeedFight(16, "Tony Bellew", "Ilunga Makabu", "BOXER_A", "TKO"),

                // --- Heavyweight (17) ---
                new SeedFight(17, "Muhammad Ali", "George Foreman", "BOXER_A", "KO"),
                new SeedFight(17, "Mike Tyson", "Trevor Berbick", "BOXER_A", "TKO")
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

                predictionHistoryRepository.findById(response.predictionId())
                        .ifPresent(history -> {
                            history.setMatchWinner(fight.actualWinner());
                            history.setMatchWinMethod(fight.actualMethod());
                            predictionHistoryRepository.save(history);
                        });

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