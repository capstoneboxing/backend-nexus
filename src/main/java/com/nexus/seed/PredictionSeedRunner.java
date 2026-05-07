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
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
@Order(2)
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
            System.out.println("Prediction seeding disabled.");
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
                new SeedFight(1, "Ricardo Lopez", "Alex Sanchez", "BOXER_A", "TKO"),
                new SeedFight(1, "Chayaphon Moonsri", "Tatsuya Fukuhara", "BOXER_A", "DECISION"),
                new SeedFight(1, "Panya Pradabsri", "Chayaphon Moonsri", "BOXER_A", "DECISION"),

                // --- Light Flyweight (2) ---
                new SeedFight(2, "Michael Carbajal", "Humberto Gonzalez", "BOXER_A", "KO"),
                new SeedFight(2, "Humberto Gonzalez", "Saman Sorjaturong", "BOXER_B", "KO"),
                new SeedFight(2, "Kenshiro Teraji", "Hiroto Kyoguchi", "BOXER_A", "TKO"),
                new SeedFight(2, "Kenshiro Teraji", "Hekkie Budler", "BOXER_A", "TKO"),
                new SeedFight(2, "Hekkie Budler", "Ryoichi Taguchi", "BOXER_A", "DECISION"),

                // --- Flyweight (3) ---
                new SeedFight(3, "Roman Gonzalez", "Akira Yaegashi", "BOXER_A", "TKO"),
                new SeedFight(3, "Roman Gonzalez", "Brian Viloria", "BOXER_A", "TKO"),
                new SeedFight(3, "Juan Francisco Estrada", "Brian Viloria", "BOXER_A", "DECISION"),
                new SeedFight(3, "Sunny Edwards", "Moruti Mthalane", "BOXER_A", "DECISION"),
                new SeedFight(3, "Jesse Rodriguez", "Sunny Edwards", "BOXER_A", "TKO"),

                // --- Super Flyweight (4) ---
                new SeedFight(4, "Naoya Inoue", "Omar Narvaez", "BOXER_A", "TKO"),
                new SeedFight(4, "Juan Francisco Estrada", "Roman Gonzalez", "BOXER_A", "DECISION"),
                new SeedFight(4, "Srisaket Sor Rungvisai", "Roman Gonzalez", "BOXER_A", "KO"),
                new SeedFight(4, "Jesse Rodriguez", "Juan Francisco Estrada", "BOXER_A", "KO"),
                new SeedFight(4, "Kazuto Ioka", "Joshua Franco", "BOXER_A", "DECISION"),
                new SeedFight(4, "Fernando Martinez", "Kazuto Ioka", "BOXER_A", "DECISION"),

                // --- Bantamweight (5) ---
                new SeedFight(5, "Nonito Donaire", "Fernando Montiel", "BOXER_A", "KO"),
                new SeedFight(5, "Naoya Inoue", "Nonito Donaire", "BOXER_A", "TKO"),
                new SeedFight(5, "Naoya Inoue", "Emmanuel Rodriguez", "BOXER_A", "TKO"),
                new SeedFight(5, "Guillermo Rigondeaux", "Nonito Donaire", "BOXER_A", "DECISION"),
                new SeedFight(5, "John Riel Casimero", "Zolani Tete", "BOXER_A", "TKO"),
                new SeedFight(5, "Junto Nakatani", "Alexandro Santiago", "BOXER_A", "TKO"),

                // --- Super Bantamweight (6) ---
                new SeedFight(6, "Naoya Inoue", "Stephen Fulton", "BOXER_A", "TKO"),
                new SeedFight(6, "Naoya Inoue", "Marlon Tapales", "BOXER_A", "KO"),
                new SeedFight(6, "Naoya Inoue", "Luis Nery", "BOXER_A", "KO"),
                new SeedFight(6, "Naoya Inoue", "Junto Nakatani", "BOXER_A", "DECISION"),
                new SeedFight(6, "Murodjon Akhmadaliev", "Daniel Roman", "BOXER_A", "DECISION"),
                new SeedFight(6, "Stephen Fulton", "Brandon Figueroa", "BOXER_A", "DECISION"),
                new SeedFight(6, "Carl Frampton", "Scott Quigg", "BOXER_A", "DECISION"),

                // --- Featherweight (7) ---
                new SeedFight(7, "Salvador Sanchez", "Wilfredo Gomez", "BOXER_A", "TKO"),
                new SeedFight(7, "Manny Pacquiao", "Marco Antonio Barrera", "BOXER_A", "TKO"),
                new SeedFight(7, "Marco Antonio Barrera", "Erik Morales", "BOXER_A", "DECISION"),
                new SeedFight(7, "Erik Morales", "Manny Pacquiao", "BOXER_A", "DECISION"),
                new SeedFight(7, "Vasiliy Lomachenko", "Gary Russell Jr", "BOXER_A", "DECISION"),
                new SeedFight(7, "Rafael Espinoza", "Robson Conceicao", "BOXER_A", "DECISION"),

                // --- Super Featherweight (8) ---
                new SeedFight(8, "Floyd Mayweather Jr", "Genaro Hernandez", "BOXER_A", "TKO"),
                new SeedFight(8, "Manny Pacquiao", "Juan Manuel Marquez", "DRAW", "DECISION"),
                new SeedFight(8, "Vasiliy Lomachenko", "Guillermo Rigondeaux", "BOXER_A", "TKO"),
                new SeedFight(8, "Oscar Valdez", "Miguel Berchelt", "BOXER_A", "KO"),
                new SeedFight(8, "Shakur Stevenson", "Oscar Valdez", "BOXER_A", "DECISION"),
                new SeedFight(8, "Anthony Cacace", "Joe Cordina", "BOXER_A", "TKO"),

                // --- Lightweight (9) ---
                new SeedFight(9, "Floyd Mayweather Jr", "Jose Luis Castillo", "BOXER_A", "DECISION"),
                new SeedFight(9, "Teofimo Lopez", "Vasiliy Lomachenko", "BOXER_A", "DECISION"),
                new SeedFight(9, "George Kambosos Jr", "Teofimo Lopez", "BOXER_A", "DECISION"),
                new SeedFight(9, "Devin Haney", "George Kambosos Jr", "BOXER_A", "DECISION"),
                new SeedFight(9, "Gervonta Davis", "Ryan Garcia", "BOXER_A", "KO"),
                new SeedFight(9, "Shakur Stevenson", "Artem Harutyunyan", "BOXER_A", "DECISION"),

                // --- Super Lightweight (10) ---
                new SeedFight(10, "Terence Crawford", "Viktor Postol", "BOXER_A", "DECISION"),
                new SeedFight(10, "Josh Taylor", "Jose Ramirez", "BOXER_A", "DECISION"),
                new SeedFight(10, "Regis Prograis", "Jose Zepeda", "BOXER_A", "TKO"),
                new SeedFight(10, "Teofimo Lopez", "Josh Taylor", "BOXER_A", "DECISION"),
                new SeedFight(10, "Devin Haney", "Regis Prograis", "BOXER_A", "DECISION"),
                new SeedFight(10, "Jack Catterall", "Josh Taylor", "BOXER_A", "DECISION"),

                // --- Welterweight (11) ---
                new SeedFight(11, "Floyd Mayweather Jr", "Manny Pacquiao", "BOXER_A", "DECISION"),
                new SeedFight(11, "Terence Crawford", "Errol Spence Jr", "BOXER_A", "TKO"),
                new SeedFight(11, "Errol Spence Jr", "Shawn Porter", "BOXER_A", "DECISION"),
                new SeedFight(11, "Keith Thurman", "Shawn Porter", "BOXER_A", "DECISION"),
                new SeedFight(11, "Manny Pacquiao", "Keith Thurman", "BOXER_A", "DECISION"),

                // --- Super Welterweight (12) ---
                new SeedFight(12, "Floyd Mayweather Jr", "Canelo Alvarez", "BOXER_A", "DECISION"),
                new SeedFight(12, "Jermell Charlo", "Brian Castano", "BOXER_A", "KO"),
                new SeedFight(12, "Sebastian Fundora", "Tim Tszyu", "BOXER_A", "DECISION"),
                new SeedFight(12, "Brian Mendoza", "Sebastian Fundora", "BOXER_A", "KO"),
                new SeedFight(12, "Miguel Cotto", "Yuri Foreman", "BOXER_A", "TKO"),
                new SeedFight(12, "Bakhram Murtazaliev", "Tim Tszyu", "BOXER_A", "TKO"),

                // --- Middleweight (13) ---
                new SeedFight(13, "Gennady Golovkin", "David Lemieux", "BOXER_A", "TKO"),
                new SeedFight(13, "Canelo Alvarez", "Gennady Golovkin", "BOXER_A", "DECISION"),
                new SeedFight(13, "Gennady Golovkin", "Daniel Jacobs", "BOXER_A", "DECISION"),
                new SeedFight(13, "Sergio Martinez", "Kelly Pavlik", "BOXER_A", "DECISION"),
                new SeedFight(13, "Marvin Hagler", "Thomas Hearns", "BOXER_A", "TKO"),
                new SeedFight(13, "Janibek Alimkhanuly", "Andrei Mikhailovich", "BOXER_A", "TKO"),

                // --- Super Middleweight (14) ---
                new SeedFight(14, "Andre Ward", "Carl Froch", "BOXER_A", "DECISION"),
                new SeedFight(14, "Canelo Alvarez", "Caleb Plant", "BOXER_A", "TKO"),
                new SeedFight(14, "Canelo Alvarez", "Billy Joe Saunders", "BOXER_A", "TKO"),
                new SeedFight(14, "Canelo Alvarez", "Callum Smith", "BOXER_A", "DECISION"),
                new SeedFight(14, "David Benavidez", "Caleb Plant", "BOXER_A", "DECISION"),
                new SeedFight(14, "Canelo Alvarez", "Jaime Munguia", "BOXER_A", "DECISION"),

                // --- Light Heavyweight (15) ---
                new SeedFight(15, "Roy Jones Jr", "Virgil Hill", "BOXER_A", "KO"),
                new SeedFight(15, "Andre Ward", "Sergey Kovalev", "BOXER_A", "TKO"),
                new SeedFight(15, "Dmitry Bivol", "Canelo Alvarez", "BOXER_A", "DECISION"),
                new SeedFight(15, "Artur Beterbiev", "Joe Smith Jr", "BOXER_A", "TKO"),
                new SeedFight(15, "Artur Beterbiev", "Dmitry Bivol", "BOXER_A", "DECISION"),
                new SeedFight(15, "Dmitry Bivol", "Gilberto Ramirez", "BOXER_A", "DECISION"),

                // --- Cruiserweight (16) ---
                new SeedFight(16, "Oleksandr Usyk", "Murat Gassiev", "BOXER_A", "DECISION"),
                new SeedFight(16, "Oleksandr Usyk", "Mairis Briedis", "BOXER_A", "DECISION"),
                new SeedFight(16, "Oleksandr Usyk", "Tony Bellew", "BOXER_A", "KO"),
                new SeedFight(16, "Jai Opetaia", "Mairis Briedis", "BOXER_A", "DECISION"),
                new SeedFight(16, "Jai Opetaia", "Ellis Zorro", "BOXER_A", "KO"),
                new SeedFight(16, "Evander Holyfield", "Dwight Muhammad Qawi", "BOXER_A", "DECISION"),

                // --- Heavyweight (17) ---
                new SeedFight(17, "Muhammad Ali", "George Foreman", "BOXER_A", "KO"),
                new SeedFight(17, "Mike Tyson", "Trevor Berbick", "BOXER_A", "TKO"),
                new SeedFight(17, "Lennox Lewis", "Mike Tyson", "BOXER_A", "KO"),
                new SeedFight(17, "Wladimir Klitschko", "Anthony Joshua", "BOXER_B", "TKO"),
                new SeedFight(17, "Tyson Fury", "Deontay Wilder", "BOXER_A", "KO"),
                new SeedFight(17, "Oleksandr Usyk", "Tyson Fury", "BOXER_A", "DECISION"),
                new SeedFight(17, "Daniel Dubois", "Anthony Joshua", "BOXER_A", "KO"),

                // Recent (0-2 years)
                new SeedFight(17, "Joseph Parker", "Deontay Wilder", "BOXER_A", "DECISION"), // 2023 - Recent
                new SeedFight(15, "David Benavidez", "Oleksandr Gvozdyk", "BOXER_A", "DECISION"), // 2024 - Recent
                new SeedFight(9, "William Zepeda", "Tevin Farmer", "BOXER_A", "DECISION"), // 2024 - Recent
                new SeedFight(6, "Nick Ball", "Raymond Ford", "BOXER_A", "DECISION"), // 2024 - Recent

                // 2-5 years
                new SeedFight(13, "Demetrius Andrade", "Liam Williams", "BOXER_A", "DECISION"), // 2022 - 2-5 years
                new SeedFight(11, "Yordenis Ugas", "Manny Pacquiao", "BOXER_A", "DECISION"), // 2021 - 2-5 years
                new SeedFight(14, "Caleb Plant", "Anthony Dirrell", "BOXER_A", "KO"), // 2021 - 2-5 years

                // 5+ years
                new SeedFight(10, "Mikey Garcia", "Adrien Broner", "BOXER_A", "DECISION"), // 2017 - 5+ years
                new SeedFight(7, "Juan Manuel Marquez", "Marco Antonio Barrera", "BOXER_A", "DECISION"), // 2007 - 10+ years

                // 10+ years
                new SeedFight(17, "Evander Holyfield", "Riddick Bowe", "BOXER_B", "DECISION"), // 1992 - 10+ years

                new SeedFight(17, "Agit Kabayel", "Frank Sanchez", "BOXER_A", "TKO"), // 2024 - Recent
                new SeedFight(15, "Joshua Buatsi", "Willy Hutchinson", "BOXER_A", "DECISION"), // 2024 - Recent
                new SeedFight(9, "Keyshawn Davis", "Jose Pedraza", "BOXER_A", "TKO"), // 2024 - Recent
                new SeedFight(11, "Jaron Ennis", "David Avanesyan", "BOXER_A", "TKO"), // 2024 - Recent
                new SeedFight(14, "Christian Mbilli", "Mark Heffron", "BOXER_A", "TKO") // 2024 - Recent
        );

        int successCount = 0;

        for (SeedFight fight : fights) {
            try {
                CompletableFuture<GeneratedBoxerResponse> boxerAFuture =
                        rankedBoxerService.generateBoxerAsync(
                                fight.boxerAName(),
                                fight.weightClassId()
                        );

                CompletableFuture<GeneratedBoxerResponse> boxerBFuture =
                        rankedBoxerService.generateBoxerAsync(
                                fight.boxerBName(),
                                fight.weightClassId()
                        );

                GeneratedBoxerResponse boxerAProfile = boxerAFuture
                        .orTimeout(90, TimeUnit.SECONDS)
                        .join();

                GeneratedBoxerResponse boxerBProfile = boxerBFuture
                        .orTimeout(90, TimeUnit.SECONDS)
                        .join();

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