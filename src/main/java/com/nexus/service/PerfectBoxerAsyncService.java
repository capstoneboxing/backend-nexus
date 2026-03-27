package com.nexus.service;

import com.nexus.exception.ResourceNotFoundException;
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
                    .orElseThrow(() -> new ResourceNotFoundException("Batch not found: " + batchId));

            Integer weightClassId = batch.getWeightClassId();

            batchRepository.updateStatusAndIsActive(batchId, "PROCESSING", false, null);

            generationService.runGeneration(batchId);

            batchRepository.deactivateOtherActiveBatchesByWeightClassId(weightClassId, batchId);

            batchRepository.updateStatusAndIsActive(batchId, "COMPLETED", true, null);

        } catch (Exception e) {
            batchRepository.updateStatusAndIsActive(batchId, "FAILED", false, e.getMessage());
        }
    }
}