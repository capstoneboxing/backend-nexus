package com.nexus.seed;

import com.nexus.dto.allTimeRankedBoxer.GeneratedBoxerResponse;
import com.nexus.dto.prediction.BoxerInput;
import com.nexus.dto.prediction.PredictMatchRequest;
import com.nexus.dto.prediction.PredictionResponse;
import com.nexus.dto.prediction.PredictionResultUpdateRequest;
import com.nexus.repository.AllTimeRankedBoxerRepository;
import com.nexus.repository.CategoryWeightRepository;
import com.nexus.repository.PerfectBoxerGenerationBatchRepository;
import com.nexus.repository.PerfectBoxerRepository;
import com.nexus.service.AllTimeRankedBoxerService;
import com.nexus.service.MatchPredictionService;
import com.nexus.service.PredictionHistoryService;
import com.nexus.service.PredictionNormalizationService;
import com.nexus.service.PredictionScoringService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Order(3)
public class AllWeightClassMatchEvaluationRunner {

    private final AllTimeRankedBoxerService rankedBoxerService;
    private final MatchPredictionService predictionService;
    private final PredictionHistoryService predictionHistoryService;
    private final PredictionScoringService scoringService;
    private final PredictionNormalizationService normalizationService;
    private final PerfectBoxerGenerationBatchRepository batchRepository;
    private final PerfectBoxerRepository perfectBoxerRepository;
    private final AllTimeRankedBoxerRepository rankedBoxerRepository;
    private final CategoryWeightRepository categoryWeightRepository;

    @Value("${app.seed.ranking-evaluation.enabled:false}")
    private boolean enabled;

    public AllWeightClassMatchEvaluationRunner(
            AllTimeRankedBoxerService rankedBoxerService,
            MatchPredictionService predictionService,
            PredictionHistoryService predictionHistoryService,
            PredictionScoringService scoringService,
            PredictionNormalizationService normalizationService,
            PerfectBoxerGenerationBatchRepository batchRepository,
            PerfectBoxerRepository perfectBoxerRepository,
            AllTimeRankedBoxerRepository rankedBoxerRepository,
            CategoryWeightRepository categoryWeightRepository
    ) {
        this.rankedBoxerService = rankedBoxerService;
        this.predictionService = predictionService;
        this.predictionHistoryService = predictionHistoryService;
        this.scoringService = scoringService;
        this.normalizationService = normalizationService;
        this.batchRepository = batchRepository;
        this.perfectBoxerRepository = perfectBoxerRepository;
        this.rankedBoxerRepository = rankedBoxerRepository;
        this.categoryWeightRepository = categoryWeightRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runEvaluation() throws Exception {
        if (!enabled) {
            System.out.println("All weight class match evaluation disabled.");
            return;
        }

        System.out.println("""
==================================================
Starting All Weight Class Match Evaluation
==================================================
""");

        List<SeedFight> testFights = List.of(
                // FEATHERWEIGHT - weightClassId 7
                new SeedFight(7, "Featherweight", "Nick Ball", "TJ Doheny", "BOXER_A", "TKO", "March 2025"),
                new SeedFight(7, "Featherweight", "Rafael Espinoza", "Robeisy Ramirez", "BOXER_A", "DECISION", "December 2024"),
                new SeedFight(7, "Featherweight", "Nick Ball", "Raymond Ford", "BOXER_A", "DECISION", "June 2024"),
                new SeedFight(7, "Featherweight", "Luis Alberto Lopez", "Reiya Abe", "BOXER_A", "TKO", "March 2024"),
                new SeedFight(7, "Featherweight", "Rafael Espinoza", "Robeisy Ramirez", "BOXER_A", "DECISION", "December 2023"),
                new SeedFight(7, "Featherweight", "Emanuel Navarrete", "Oscar Valdez", "BOXER_A", "DECISION", "August 2023"),
                new SeedFight(7, "Featherweight", "Luis Alberto Lopez", "Michael Conlan", "BOXER_A", "DECISION", "May 2023"),
                new SeedFight(7, "Featherweight", "Mauricio Lara", "Leigh Wood", "BOXER_A", "TKO", "February 2023"),
                new SeedFight(7, "Featherweight", "Leigh Wood", "Mauricio Lara", "BOXER_A", "DECISION", "May 2023"),
                new SeedFight(7, "Featherweight", "Luis Alberto Lopez", "Josh Warrington", "BOXER_A", "DECISION", "December 2022"),
                new SeedFight(7, "Featherweight", "Leigh Wood", "Michael Conlan", "BOXER_A", "TKO", "March 2022"),
                new SeedFight(7, "Featherweight", "Emanuel Navarrete", "Joet Gonzalez", "BOXER_A", "DECISION", "October 2021"),
                new SeedFight(7, "Featherweight", "Kid Galahad", "Jazza Dickens", "BOXER_A", "TKO", "August 2021"),
                new SeedFight(7, "Featherweight", "Gary Russell Jr", "Mark Magsayo", "BOXER_B", "DECISION", "January 2022"),
                new SeedFight(7, "Featherweight", "Xu Can", "Leigh Wood", "BOXER_B", "TKO", "July 2021"),
                new SeedFight(7, "Featherweight", "Josh Warrington", "Mauricio Lara", "BOXER_B", "TKO", "February 2021"),
                new SeedFight(7, "Featherweight", "Emanuel Navarrete", "Ruben Villa", "BOXER_A", "DECISION", "October 2020"),
                new SeedFight(7, "Featherweight", "Shakur Stevenson", "Joet Gonzalez", "BOXER_A", "DECISION", "October 2019"),
                new SeedFight(7, "Featherweight", "Josh Warrington", "Kid Galahad", "BOXER_A", "DECISION", "June 2019"),
                new SeedFight(7, "Featherweight", "Leo Santa Cruz", "Rafael Rivera", "BOXER_A", "DECISION", "February 2019"),
                new SeedFight(7, "Featherweight", "Josh Warrington", "Carl Frampton", "BOXER_A", "DECISION", "December 2018"),
                new SeedFight(7, "Featherweight", "Oscar Valdez", "Scott Quigg", "BOXER_A", "DECISION", "March 2018"),
                new SeedFight(7, "Featherweight", "Leo Santa Cruz", "Abner Mares", "BOXER_A", "DECISION", "June 2018"),
                new SeedFight(7, "Featherweight", "Gary Russell Jr", "Joseph Diaz Jr", "BOXER_A", "DECISION", "May 2018"),
                new SeedFight(7, "Featherweight", "Leo Santa Cruz", "Carl Frampton", "BOXER_A", "DECISION", "January 2017"),

                // WELTERWEIGHT - weightClassId 11
                new SeedFight(11, "Welterweight", "Jaron Ennis", "Eimantas Stanionis", "BOXER_A", "TKO", "April 2025"),
                new SeedFight(11, "Welterweight", "Mario Barrios", "Abel Ramos", "DRAW", "DECISION", "November 2024"),
                new SeedFight(11, "Welterweight", "Eimantas Stanionis", "Gabriel Maestre", "BOXER_A", "DECISION", "May 2024"),
                new SeedFight(11, "Welterweight", "Brian Norman Jr", "Giovani Santillan", "BOXER_A", "KO", "May 2024"),
                new SeedFight(11, "Welterweight", "Mario Barrios", "Fabian Maidana", "BOXER_A", "DECISION", "May 2024"),
                new SeedFight(11, "Welterweight", "Conor Benn", "Peter Dobson", "BOXER_A", "DECISION", "February 2024"),
                new SeedFight(11, "Welterweight", "Alexis Rocha", "George Ashie", "BOXER_A", "KO", "October 2023"),
                new SeedFight(11, "Welterweight", "Giovanni Santillan", "Alexis Rocha", "BOXER_A", "KO", "October 2023"),
                new SeedFight(11, "Welterweight", "Mario Barrios", "Yordenis Ugas", "BOXER_A", "DECISION", "September 2023"),
                new SeedFight(11, "Welterweight", "Terence Crawford", "Errol Spence Jr", "BOXER_A", "TKO", "July 2023"),
                new SeedFight(11, "Welterweight", "Jaron Ennis", "Roiman Villa", "BOXER_A", "KO", "July 2023"),
                new SeedFight(11, "Welterweight", "Cody Crowley", "Abel Ramos", "BOXER_A", "DECISION", "March 2023"),
                new SeedFight(11, "Welterweight", "Jaron Ennis", "Karen Chukhadzhian", "BOXER_A", "DECISION", "January 2023"),
                new SeedFight(11, "Welterweight", "Terence Crawford", "David Avanesyan", "BOXER_A", "KO", "December 2022"),
                new SeedFight(11, "Welterweight", "Vergil Ortiz Jr", "Michael McKinson", "BOXER_A", "TKO", "August 2022"),
                new SeedFight(11, "Welterweight", "Vergil Ortiz Jr", "Egidijus Kavaliauskas", "BOXER_A", "TKO", "August 2022"),
                new SeedFight(11, "Welterweight", "Errol Spence Jr", "Yordenis Ugas", "BOXER_A", "TKO", "April 2022"),
                new SeedFight(11, "Welterweight", "Eimantas Stanionis", "Radzhab Butaev", "BOXER_A", "DECISION", "April 2022"),
                new SeedFight(11, "Welterweight", "Keith Thurman", "Mario Barrios", "BOXER_A", "DECISION", "February 2022"),
                new SeedFight(11, "Welterweight", "Conor Benn", "Chris Algieri", "BOXER_A", "KO", "December 2021"),
                new SeedFight(11, "Welterweight", "Cody Crowley", "Kudratillo Abdukakhorov", "BOXER_A", "DECISION", "December 2021"),
                new SeedFight(11, "Welterweight", "Radzhab Butaev", "Jamal James", "BOXER_A", "TKO", "October 2021"),
                new SeedFight(11, "Welterweight", "Yordenis Ugas", "Manny Pacquiao", "BOXER_A", "DECISION", "August 2021"),
                new SeedFight(11, "Welterweight", "David Avanesyan", "Josh Kelly", "BOXER_A", "TKO", "February 2021"),
                new SeedFight(11, "Welterweight", "Errol Spence Jr", "Danny Garcia", "BOXER_A", "DECISION", "December 2020"),
                new SeedFight(11, "Welterweight", "Shawn Porter", "Sebastian Formella", "BOXER_A", "DECISION", "August 2020"),
                new SeedFight(11, "Welterweight", "Vergil Ortiz Jr", "Samuel Vargas", "BOXER_A", "TKO", "July 2020"),
                new SeedFight(11, "Welterweight", "Mikey Garcia", "Jessie Vargas", "BOXER_A", "DECISION", "February 2020"),
                new SeedFight(11, "Welterweight", "Danny Garcia", "Ivan Redkach", "BOXER_A", "DECISION", "January 2020"),
                new SeedFight(11, "Welterweight", "Terence Crawford", "Egidijus Kavaliauskas", "BOXER_A", "TKO", "December 2019"),
                new SeedFight(11, "Welterweight", "Errol Spence Jr", "Shawn Porter", "BOXER_A", "DECISION", "September 2019"),
                new SeedFight(11, "Welterweight", "Manny Pacquiao", "Keith Thurman", "BOXER_A", "DECISION", "July 2019"),
                new SeedFight(11, "Welterweight", "Terence Crawford", "Amir Khan", "BOXER_A", "TKO", "April 2019"),
                new SeedFight(11, "Welterweight", "Errol Spence Jr", "Mikey Garcia", "BOXER_A", "DECISION", "March 2019"),
                new SeedFight(11, "Welterweight", "Shawn Porter", "Yordenis Ugas", "BOXER_A", "DECISION", "March 2019"),
                new SeedFight(11, "Welterweight", "Keith Thurman", "Josesito Lopez", "BOXER_A", "DECISION", "January 2019"),
                new SeedFight(11, "Welterweight", "Terence Crawford", "Jose Benavidez Jr", "BOXER_A", "TKO", "October 2018"),
                new SeedFight(11, "Welterweight", "Shawn Porter", "Danny Garcia", "BOXER_A", "DECISION", "September 2018"),
                new SeedFight(11, "Welterweight", "Manny Pacquiao", "Lucas Matthysse", "BOXER_A", "TKO", "July 2018"),
                new SeedFight(11, "Welterweight", "Errol Spence Jr", "Carlos Ocampo", "BOXER_A", "KO", "June 2018"),
                new SeedFight(11, "Welterweight", "Manny Pacquiao", "Jeff Horn", "BOXER_B", "DECISION", "July 2017"),
                new SeedFight(11, "Welterweight", "Kell Brook", "Errol Spence Jr", "BOXER_B", "TKO", "May 2017"),
                new SeedFight(11, "Welterweight", "Keith Thurman", "Danny Garcia", "BOXER_A", "DECISION", "March 2017"),
                new SeedFight(11, "Welterweight", "Manny Pacquiao", "Jessie Vargas", "BOXER_A", "DECISION", "November 2016"),
                new SeedFight(11, "Welterweight", "Danny Garcia", "Robert Guerrero", "BOXER_A", "DECISION", "January 2016"),
                new SeedFight(11, "Welterweight", "Floyd Mayweather Jr", "Andre Berto", "BOXER_A", "DECISION", "September 2015"),
                new SeedFight(11, "Welterweight", "Floyd Mayweather Jr", "Manny Pacquiao", "BOXER_A", "DECISION", "May 2015"),
                new SeedFight(11, "Welterweight", "Floyd Mayweather Jr", "Marcos Maidana", "BOXER_A", "DECISION", "September 2014"),
                new SeedFight(11, "Welterweight", "Kell Brook", "Shawn Porter", "BOXER_A", "DECISION", "August 2014"),
                new SeedFight(11, "Welterweight", "Floyd Mayweather Jr", "Marcos Maidana", "BOXER_A", "DECISION", "May 2014"),

                // MIDDLEWEIGHT - weightClassId 13
                new SeedFight(13, "Middleweight", "Janibek Alimkhanuly", "Andrei Mikhailovich", "BOXER_A", "TKO", "October 2024"),
                new SeedFight(13, "Middleweight", "Carlos Adames", "Terrell Gausha", "BOXER_A", "DECISION", "June 2024"),
                new SeedFight(13, "Middleweight", "Janibek Alimkhanuly", "Vincenzo Gualtieri", "BOXER_A", "TKO", "October 2023"),
                new SeedFight(13, "Middleweight", "Chris Eubank Jr", "Liam Smith", "BOXER_A", "TKO", "September 2023"),
                new SeedFight(13, "Middleweight", "Liam Smith", "Chris Eubank Jr", "BOXER_A", "TKO", "January 2023"),
                new SeedFight(13, "Middleweight", "Gennady Golovkin", "Ryota Murata", "BOXER_A", "TKO", "April 2022"),
                new SeedFight(13, "Middleweight", "Jaime Munguia", "Gabriel Rosado", "BOXER_A", "DECISION", "November 2021"),
                new SeedFight(13, "Middleweight", "Jermall Charlo", "Juan Macias Montiel", "BOXER_A", "DECISION", "June 2021"),
                new SeedFight(13, "Middleweight", "Jermall Charlo", "Sergiy Derevyanchenko", "BOXER_A", "DECISION", "September 2020"),
                new SeedFight(13, "Middleweight", "Demetrius Andrade", "Luke Keeler", "BOXER_A", "TKO", "January 2020"),

                // SUPER MIDDLEWEIGHT - weightClassId 14
                new SeedFight(14, "Super Middleweight", "Canelo Alvarez", "Edgar Berlanga", "BOXER_A", "DECISION", "September 2024"),
                new SeedFight(14, "Super Middleweight", "Canelo Alvarez", "Jaime Munguia", "BOXER_A", "DECISION", "May 2024"),
                new SeedFight(14, "Super Middleweight", "Christian Mbilli", "Mark Heffron", "BOXER_A", "TKO", "May 2024"),
                new SeedFight(14, "Super Middleweight", "David Benavidez", "Demetrius Andrade", "BOXER_A", "RTD", "November 2023"),
                new SeedFight(14, "Super Middleweight", "Canelo Alvarez", "Jermell Charlo", "BOXER_A", "DECISION", "September 2023"),
                new SeedFight(14, "Super Middleweight", "David Benavidez", "Caleb Plant", "BOXER_A", "DECISION", "March 2023"),
                new SeedFight(14, "Super Middleweight", "Canelo Alvarez", "Gennady Golovkin", "BOXER_A", "DECISION", "September 2022"),
                new SeedFight(14, "Super Middleweight", "Dmitry Bivol", "Gilberto Ramirez", "BOXER_A", "DECISION", "November 2022"),
                new SeedFight(14, "Super Middleweight", "Canelo Alvarez", "Caleb Plant", "BOXER_A", "TKO", "November 2021"),
                new SeedFight(14, "Super Middleweight", "David Benavidez", "Ronald Ellis", "BOXER_A", "TKO", "March 2021"),

                // HEAVYWEIGHT - weightClassId 17
                new SeedFight(17, "Heavyweight", "Oleksandr Usyk", "Tyson Fury", "BOXER_A", "DECISION", "December 2025"),
                new SeedFight(17, "Heavyweight", "Daniel Dubois", "Joseph Parker", "BOXER_B", "TKO", "November 2025"),
                new SeedFight(17, "Heavyweight", "Anthony Joshua", "Deontay Wilder", "BOXER_A", "KO", "September 2025"),
                new SeedFight(17, "Heavyweight", "Filip Hrgovic", "Zhilei Zhang", "BOXER_B", "TKO", "June 2025"),
                new SeedFight(17, "Heavyweight", "Oleksandr Usyk", "Tyson Fury", "BOXER_A", "DECISION", "May 2024"),
                new SeedFight(17, "Heavyweight", "Anthony Joshua", "Francis Ngannou", "BOXER_A", "KO", "March 2024"),
                new SeedFight(17, "Heavyweight", "Joseph Parker", "Deontay Wilder", "BOXER_A", "DECISION", "December 2023"),
                new SeedFight(17, "Heavyweight", "Anthony Joshua", "Otto Wallin", "BOXER_A", "RTD", "December 2023"),
                new SeedFight(17, "Heavyweight", "Zhilei Zhang", "Joe Joyce", "BOXER_A", "KO", "September 2023"),
                new SeedFight(17, "Heavyweight", "Oleksandr Usyk", "Daniel Dubois", "BOXER_A", "KO", "August 2023"),
                new SeedFight(17, "Heavyweight", "Anthony Joshua", "Robert Helenius", "BOXER_A", "KO", "August 2023"),
                new SeedFight(17, "Heavyweight", "Tyson Fury", "Francis Ngannou", "BOXER_A", "DECISION", "October 2023"),
                new SeedFight(17, "Heavyweight", "Zhilei Zhang", "Joe Joyce", "BOXER_A", "TKO", "April 2023"),
                new SeedFight(17, "Heavyweight", "Anthony Joshua", "Jermaine Franklin", "BOXER_A", "DECISION", "April 2023"),
                new SeedFight(17, "Heavyweight", "Tyson Fury", "Derek Chisora", "BOXER_A", "TKO", "December 2022"),
                new SeedFight(17, "Heavyweight", "Oleksandr Usyk", "Anthony Joshua", "BOXER_A", "DECISION", "August 2022"),
                new SeedFight(17, "Heavyweight", "Joe Joyce", "Joseph Parker", "BOXER_A", "KO", "September 2022"),
                new SeedFight(17, "Heavyweight", "Tyson Fury", "Dillian Whyte", "BOXER_A", "KO", "April 2022"),
                new SeedFight(17, "Heavyweight", "Oleksandr Usyk", "Anthony Joshua", "BOXER_A", "DECISION", "September 2021"),
                new SeedFight(17, "Heavyweight", "Tyson Fury", "Deontay Wilder", "BOXER_A", "KO", "October 2021"),
                new SeedFight(17, "Heavyweight", "Anthony Joshua", "Kubrat Pulev", "BOXER_A", "KO", "December 2020"),
                new SeedFight(17, "Heavyweight", "Dillian Whyte", "Alexander Povetkin", "BOXER_A", "TKO", "March 2021"),
                new SeedFight(17, "Heavyweight", "Tyson Fury", "Deontay Wilder", "BOXER_A", "TKO", "February 2020"),
                new SeedFight(17, "Heavyweight", "Anthony Joshua", "Andy Ruiz Jr", "BOXER_A", "DECISION", "December 2019"),
                new SeedFight(17, "Heavyweight", "Andy Ruiz Jr", "Anthony Joshua", "BOXER_A", "TKO", "June 2019")
        );

        Map<BoxerKey, SeedBoxerProfile> generatedProfiles = generateUniqueBoxerProfiles(testFights);

        List<EvaluationRow> matchRows = runMatchPredictions(testFights, generatedProfiles);

        exportCsv(generatedProfiles, matchRows);

        System.out.printf("""
==================================================
All weight class match evaluation completed.
File: %s
Successful generated boxer profiles: %d
Successful fights tested: %d
==================================================

%n""",
                Paths.get("all-weightclass-match-evaluation.csv").toAbsolutePath(),
                generatedProfiles.size(),
                matchRows.size()
        );
    }

    private Map<BoxerKey, SeedBoxerProfile> generateUniqueBoxerProfiles(List<SeedFight> testFights) {
        Set<BoxerKey> uniqueBoxers = new LinkedHashSet<>();

        for (SeedFight fight : testFights) {
            uniqueBoxers.add(new BoxerKey(fight.weightClassId(), fight.weightClassName(), fight.boxerAName()));
            uniqueBoxers.add(new BoxerKey(fight.weightClassId(), fight.weightClassName(), fight.boxerBName()));
        }

        Map<BoxerKey, SeedBoxerProfile> generatedProfiles = new LinkedHashMap<>();

        int current = 1;
        int total = uniqueBoxers.size();

        for (BoxerKey key : uniqueBoxers) {
            try {
                System.out.printf("""
==================================================
[%d/%d]
Generating boxer profile
Boxer: %s
Weight class: %s
==================================================

""",
                        current++,
                        total,
                        key.boxerName(),
                        key.weightClassName()
                );

                GeneratedBoxerResponse generated = rankedBoxerService.generateBoxer(
                        key.boxerName(),
                        key.weightClassId()
                );

                if (!generated.boxerFound()) {
                    System.out.println("Skipped profile because boxer was not found: " + key.boxerName());
                    continue;
                }

                BoxerInput input = toBoxerInput(generated);
                SeedBoxerProfile scoredProfile = scoreGeneratedProfile(key, input);

                generatedProfiles.put(key, scoredProfile);

                System.out.printf("""
Generated successfully:
- Boxer: %s
- Confidence: %.2f
- Base closeness: %.2f
- Adjusted closeness: %.2f

%n""",
                        key.boxerName(),
                        input.attributeConfidence(),
                        scoredProfile.baseCloseness(),
                        scoredProfile.adjustedCloseness()
                );

            } catch (Exception e) {
                System.out.println("Skipped profile because generation/scoring failed for "
                        + key.boxerName()
                        + ": "
                        + e.getMessage());
            }
        }

        return generatedProfiles;
    }

    private SeedBoxerProfile scoreGeneratedProfile(BoxerKey key, BoxerInput input) {
        var activeBatch = batchRepository
                .findByWeightClassIdAndIsActiveTrue(key.weightClassId())
                .orElseThrow(() -> new RuntimeException(
                        "No active perfect boxer batch found for " + key.weightClassName()
                ));

        var perfectBoxer = perfectBoxerRepository
                .findByBatchId(activeBatch.getBatchId())
                .orElseThrow(() -> new RuntimeException(
                        "No perfect boxer found for batch " + activeBatch.getBatchId()
                ));

        var rankedBoxers = rankedBoxerRepository
                .findByBatchIdOrderByRankingPositionAsc(activeBatch.getBatchId());

        var ranges = normalizationService.buildRanges(rankedBoxers);

        var weights = categoryWeightRepository
                .findById(key.weightClassId())
                .orElseThrow(() -> new RuntimeException(
                        "No category weights found for " + key.weightClassName()
                ));

        var perfectScores = scoringService.scorePerfectBoxer(perfectBoxer, ranges, weights);
        var scores = scoringService.scoreBoxer(input, ranges, weights);

        double overallScore = scoringService.overallScore(scores);

        double baseCloseness = scoringService.closeness(
                scores,
                perfectScores,
                weights
        );

        double adjustedCloseness = scoringService.applyAttributeConfidence(
                baseCloseness,
                input.attributeConfidence()
        );

        return new SeedBoxerProfile(
                key.weightClassId(),
                key.weightClassName(),
                key.boxerName(),
                input,

                scores.physical(),
                scores.technical(),
                scores.tactical(),
                scores.psychological(),
                scores.experience(),

                overallScore,
                baseCloseness,
                adjustedCloseness
        );
    }

    private List<EvaluationRow> runMatchPredictions(
            List<SeedFight> testFights,
            Map<BoxerKey, SeedBoxerProfile> generatedProfiles
    ) {
        List<EvaluationRow> rows = new ArrayList<>();

        int current = 1;
        int total = testFights.size();

        for (SeedFight fight : testFights) {
            BoxerKey boxerAKey = new BoxerKey(
                    fight.weightClassId(),
                    fight.weightClassName(),
                    fight.boxerAName()
            );

            BoxerKey boxerBKey = new BoxerKey(
                    fight.weightClassId(),
                    fight.weightClassName(),
                    fight.boxerBName()
            );

            SeedBoxerProfile boxerAProfile = generatedProfiles.get(boxerAKey);
            SeedBoxerProfile boxerBProfile = generatedProfiles.get(boxerBKey);

            if (boxerAProfile == null || boxerBProfile == null) {
                System.out.printf("""
==================================================
[%d/%d]
Skipping fight because one or both profiles failed:
%s vs %s
==================================================

""",
                        current++,
                        total,
                        fight.boxerAName(),
                        fight.boxerBName()
                );
                continue;
            }

            try {
                System.out.printf("""
==================================================
[%d/%d]
Predicting fight: %s vs %s
Weight class: %s
Date: %s
==================================================

""",
                        current++,
                        total,
                        fight.boxerAName(),
                        fight.boxerBName(),
                        fight.weightClassName(),
                        fight.fightDate()
                );

                PredictMatchRequest request = new PredictMatchRequest(
                        fight.weightClassId(),
                        boxerAProfile.input(),
                        boxerBProfile.input()
                );

                PredictionResponse prediction = predictionService.predict(request);

                predictionHistoryService.updatePredictionHistory(
                        prediction.predictionId(),
                        new PredictionResultUpdateRequest(
                                fight.actualWinner(),
                                fight.actualMethod()
                        )
                );

                boolean isCorrect = prediction.predictedWinner().equals(fight.actualWinner());

                rows.add(new EvaluationRow(
                        fight.weightClassId(),
                        fight.weightClassName(),
                        fight.fightDate(),
                        fight.boxerAName(),
                        fight.boxerBName(),
                        fight.actualWinner(),
                        fight.actualMethod(),
                        prediction.predictedWinner(),
                        prediction.probabilityA(),
                        prediction.probabilityB(),
                        prediction.closenessA(),
                        prediction.closenessB(),
                        boxerAProfile.input().attributeConfidence(),
                        boxerBProfile.input().attributeConfidence(),
                        isCorrect ? "YES" : "NO"
                ));

            } catch (Exception e) {
                System.out.println("Skipped fight because prediction failed for "
                        + fight.boxerAName()
                        + " vs "
                        + fight.boxerBName()
                        + ": "
                        + e.getMessage());
            }
        }

        return rows;
    }

    private void exportCsv(
            Map<BoxerKey, SeedBoxerProfile> generatedProfiles,
            List<EvaluationRow> matchRows
    ) throws IOException {
        Path outputPath = Paths.get("all-weightclass-match-evaluation.csv");

        List<String> lines = new ArrayList<>();

        lines.add("GENERATED BOXER PROFILES");
        lines.add(String.join(",",
                "rank",
                "weightClassId",
                "weightClassName",
                "boxerName",
                "confidence",
                "overallScore",
                "physicalScore",
                "technicalScore",
                "tacticalScore",
                "psychologicalScore",
                "experienceScore",
                "baseCloseness",
                "adjustedCloseness",
                "heightCm",
                "reachCm",
                "weightClassAlignment",
                "handSpeed",
                "footSpeed",
                "strength",
                "endurance",
                "reactionTime",
                "punchAccuracy",
                "punchVariety",
                "defensiveGuardEfficiency",
                "headMovement",
                "footworkTechnique",
                "counterpunchingAbility",
                "combinationEfficiency",
                "ringIq",
                "adaptabilityMidFight",
                "distanceControl",
                "tempoControl",
                "opponentPatternRecognition",
                "fightPlanningDiscipline",
                "composureUnderPressure",
                "aggressionControl",
                "mentalToughness",
                "focusConsistency",
                "resilienceAfterKnockdown",
                "winRatio",
                "knockoutRatio",
                "titleFightExperience",
                "strengthOfOpposition",
                "recentFightActivity",
                "performanceConsistency"
        ));

        Map<String, List<SeedBoxerProfile>> groupedProfiles =
                generatedProfiles.values()
                        .stream()
                        .collect(Collectors.groupingBy(
                                SeedBoxerProfile::weightClassName,
                                LinkedHashMap::new,
                                Collectors.toList()
                        ));

        for (Map.Entry<String, List<SeedBoxerProfile>> entry : groupedProfiles.entrySet()) {

            List<SeedBoxerProfile> rankedProfiles = entry.getValue()
                    .stream()
                    .sorted(
                            Comparator.comparing(
                                    SeedBoxerProfile::adjustedCloseness
                            ).reversed()
                    )
                    .toList();

            int rank = 1;

            for (SeedBoxerProfile profile : rankedProfiles) {
                BoxerInput input = profile.input();

                lines.add(String.join(",",
                        value(rank++),
                        value(profile.weightClassId()),
                        escapeCsv(profile.weightClassName()),
                        escapeCsv(profile.boxerName()),
                        formatDouble(input.attributeConfidence()),
                        formatDouble(profile.overallScore()),
                        formatDouble(profile.physicalScore()),
                        formatDouble(profile.technicalScore()),
                        formatDouble(profile.tacticalScore()),
                        formatDouble(profile.psychologicalScore()),
                        formatDouble(profile.experienceScore()),
                        formatDouble(profile.baseCloseness()),
                        formatDouble(profile.adjustedCloseness()),
                        formatInt(input.heightCm()),
                        formatInt(input.reachCm()),
                        formatOneDecimal(input.weightClassAlignment()),
                        formatOneDecimal(input.handSpeed()),
                        formatOneDecimal(input.footSpeed()),
                        formatOneDecimal(input.strength()),
                        formatOneDecimal(input.endurance()),
                        formatOneDecimal(input.reactionTime()),
                        formatOneDecimal(input.punchAccuracy()),
                        formatOneDecimal(input.punchVariety()),
                        formatOneDecimal(input.defensiveGuardEfficiency()),
                        formatOneDecimal(input.headMovement()),
                        formatOneDecimal(input.footworkTechnique()),
                        formatOneDecimal(input.counterpunchingAbility()),
                        formatOneDecimal(input.combinationEfficiency()),
                        formatOneDecimal(input.ringIq()),
                        formatOneDecimal(input.adaptabilityMidFight()),
                        formatOneDecimal(input.distanceControl()),
                        formatOneDecimal(input.tempoControl()),
                        formatOneDecimal(input.opponentPatternRecognition()),
                        formatOneDecimal(input.fightPlanningDiscipline()),
                        formatOneDecimal(input.composureUnderPressure()),
                        formatOneDecimal(input.aggressionControl()),
                        formatOneDecimal(input.mentalToughness()),
                        formatOneDecimal(input.focusConsistency()),
                        formatOneDecimal(input.resilienceAfterKnockdown()),
                        formatDouble(input.winRatio()),
                        formatDouble(input.knockoutRatio()),
                        formatOneDecimal(input.titleFightExperience()),
                        formatOneDecimal(input.strengthOfOpposition()),
                        formatOneDecimal(input.recentFightActivity()),
                        formatOneDecimal(input.performanceConsistency())
                ));
            }

            lines.add("");
        }

        lines.add("");
        lines.add("MATCH PREDICTION RESULTS");
        lines.add(String.join(",",
                "weightClassId",
                "weightClassName",
                "fightDate",
                "boxerA",
                "boxerB",
                "actualWinner",
                "actualMethod",
                "predictedWinner",
                "probabilityA",
                "probabilityB",
                "boxerACloseness",
                "boxerBCloseness",
                "boxerAConfidence",
                "boxerBConfidence",
                "correct"
        ));

        for (EvaluationRow row : matchRows) {
            lines.add(String.join(",",
                    value(row.weightClassId()),
                    escapeCsv(row.weightClassName()),
                    escapeCsv(row.fightDate()),
                    escapeCsv(row.boxerAName()),
                    escapeCsv(row.boxerBName()),
                    escapeCsv(row.actualWinner()),
                    escapeCsv(row.actualMethod()),
                    escapeCsv(row.predictedWinner()),
                    formatDouble(row.probabilityA()),
                    formatDouble(row.probabilityB()),
                    formatDouble(row.boxerACloseness()),
                    formatDouble(row.boxerBCloseness()),
                    formatDouble(row.boxerAConfidence()),
                    formatDouble(row.boxerBConfidence()),
                    escapeCsv(row.correct())
            ));
        }

        long correctPredictions = matchRows.stream()
                .filter(row -> "YES".equals(row.correct()))
                .count();

        double accuracy = matchRows.isEmpty()
                ? 0.0
                : ((double) correctPredictions / matchRows.size()) * 100.0;

        lines.add("");
        lines.add("SUMMARY");
        lines.add("successfulProfiles,successfulPredictions,correctPredictions,accuracyPercentage");
        lines.add(String.format(
                Locale.US,
                "%d,%d,%d,%.2f%%",
                generatedProfiles.size(),
                matchRows.size(),
                correctPredictions,
                accuracy
        ));

        Files.write(outputPath, lines);

        System.out.println("CSV exported to: " + outputPath.toAbsolutePath());
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

    private String value(Integer value) {
        return value == null ? "" : String.valueOf(value);
    }

    private String formatDouble(Double value) {
        return value == null ? "" : String.format(Locale.US, "%.2f", value);
    }

    private String formatInt(Double value) {
        return value == null ? "" : String.format(Locale.US, "%.0f", value);
    }

    private String formatOneDecimal(Double value) {
        return value == null ? "" : String.format(Locale.US, "%.1f", value);
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        String escaped = value.replace("\"", "\"\"");

        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }

        return escaped;
    }

    private record BoxerKey(
            Integer weightClassId,
            String weightClassName,
            String boxerName
    ) {}

    private record SeedFight(
            Integer weightClassId,
            String weightClassName,
            String boxerAName,
            String boxerBName,
            String actualWinner,
            String actualMethod,
            String fightDate
    ) {}

    private record SeedBoxerProfile(
            Integer weightClassId,
            String weightClassName,
            String boxerName,
            BoxerInput input,

            Double physicalScore,
            Double technicalScore,
            Double tacticalScore,
            Double psychologicalScore,
            Double experienceScore,

            Double overallScore,
            Double baseCloseness,
            Double adjustedCloseness
    ) {}

    private record EvaluationRow(
            Integer weightClassId,
            String weightClassName,
            String fightDate,
            String boxerAName,
            String boxerBName,
            String actualWinner,
            String actualMethod,
            String predictedWinner,
            Double probabilityA,
            Double probabilityB,
            Double boxerACloseness,
            Double boxerBCloseness,
            Double boxerAConfidence,
            Double boxerBConfidence,
            String correct
    ) {}
}