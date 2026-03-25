package com.nexus.service;

import com.nexus.model.PerfectBoxerGenerationBatch;
import com.nexus.repository.PerfectBoxerGenerationBatchRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PerfectBoxerAsyncService {

    private final PerfectBoxerGenerationBatchRepository batchRepository;
    private final PerfectBoxerGenerationService generationService;

    public PerfectBoxerAsyncService(
            PerfectBoxerGenerationBatchRepository batchRepository,
            PerfectBoxerGenerationService generationService
    ) {
        this.batchRepository = batchRepository;
        this.generationService = generationService;
    }

    @Async
    public void generateForBatchAsync(Integer batchId) {
        try {
            PerfectBoxerGenerationBatch batch = batchRepository.findById(batchId)
                    .orElseThrow(() -> new IllegalArgumentException("Batch not found: " + batchId));

            Integer weightClassId = batch.getWeightClassId();

            batchRepository.updateStatusAndIsActive(batchId, "PROCESSING", null, false);

            generationService.runGeneration(batchId);

            batchRepository.deactivateOtherActiveBatchesByWeightClassId(weightClassId, batchId);

            batchRepository.updateStatusAndIsActive(batchId, "COMPLETED", null, true);

        } catch (Exception e) {
            batchRepository.updateStatusAndIsActive(batchId, "FAILED", e.getMessage(), false);
        }
    }
}