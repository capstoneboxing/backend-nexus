package com.nexus.seed;

import com.nexus.model.PerfectBoxerGenerationBatch;
import com.nexus.model.WeightClass;
import com.nexus.repository.PerfectBoxerGenerationBatchRepository;
import com.nexus.repository.WeightClassRepository;
import com.nexus.service.PerfectBoxerGenerationService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
public class PerfectBoxerSeeder implements CommandLineRunner {

    private final WeightClassRepository weightClassRepository;
    private final PerfectBoxerGenerationBatchRepository batchRepository;
    private final PerfectBoxerGenerationService generationService;

    @Value("${app.seed.perfect-boxers.enabled:false}")
    private boolean enabled;

    @Value("${app.seed.perfect-boxers.amount:5}")
    private int amount;

    @Value("${app.seed.perfect-boxers.weight-class-ids:}")
    private String weightClassIds;

    public PerfectBoxerSeeder(
            WeightClassRepository weightClassRepository,
            PerfectBoxerGenerationBatchRepository batchRepository,
            PerfectBoxerGenerationService generationService
    ) {
        this.weightClassRepository = weightClassRepository;
        this.batchRepository = batchRepository;
        this.generationService = generationService;
    }

    @Override
    public void run(String @NonNull ... args) {
        if (!enabled) {
            System.out.println("Perfect boxer seeding disabled.");
            return;
        }

        List<Integer> selectedWeightClassIds = parseWeightClassIds(weightClassIds);

        if (selectedWeightClassIds.isEmpty()) {
            System.out.println("No weight class IDs provided. Perfect boxer seeding skipped.");
            return;
        }

        System.out.println("Starting perfect boxer seeding for weight classes: " + selectedWeightClassIds);

        for (Integer weightClassId : selectedWeightClassIds) {
            WeightClass weightClass = weightClassRepository.findById(weightClassId).orElse(null);

            if (weightClass == null) {
                System.out.println("Skipping invalid weight class ID: " + weightClassId);
                continue;
            }

            try {
                System.out.println("Generating perfect boxer for weight class ID: " + weightClassId);

                PerfectBoxerGenerationBatch batch = new PerfectBoxerGenerationBatch();
                batch.setWeightClassId(weightClassId);
                batch.setAmount(amount);
                batch.setStatus("PROCESSING");
                batch.setIsActive(false);

                PerfectBoxerGenerationBatch savedBatch = batchRepository.save(batch);

                generationService.runGeneration(savedBatch.getBatchId());

                batchRepository.deactivateOtherActiveBatchesByWeightClassId(
                        weightClassId,
                        savedBatch.getBatchId()
                );

                batchRepository.updateStatusAndIsActive(
                        savedBatch.getBatchId(),
                        "COMPLETED",
                        true,
                        null
                );

                System.out.println("Completed perfect boxer for weight class ID: " + weightClassId);

            } catch (Exception e) {
                System.out.println("Failed to generate perfect boxer for weight class ID: " + weightClassId);
                System.out.println("Reason: " + e.getMessage());
            }
        }

        System.out.println("Perfect boxer seeding finished.");
    }

    private List<Integer> parseWeightClassIds(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }

        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(id -> !id.isBlank())
                .map(Integer::parseInt)
                .distinct()
                .toList();
    }
}