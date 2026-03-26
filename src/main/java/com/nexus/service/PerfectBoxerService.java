package com.nexus.service;

import com.nexus.dto.perfectboxer.PerfectBoxerBatchStatusResponse;
import com.nexus.dto.perfectboxer.PerfectBoxerGenerationStartedResponse;
import com.nexus.dto.perfectboxer.PerfectBoxerResponse;
import com.nexus.model.*;
import com.nexus.repository.AllTimeRankedBoxerRepository;
import com.nexus.repository.PerfectBoxerGenerationBatchRepository;
import com.nexus.repository.PerfectBoxerRepository;
import com.nexus.repository.WeightClassRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PerfectBoxerService {

    private final WeightClassRepository weightClassRepository;
    private final PerfectBoxerGenerationBatchRepository batchRepository;
    private final AllTimeRankedBoxerRepository rankedBoxerRepository;
    private final PerfectBoxerRepository perfectBoxerRepository;
    private final PerfectBoxerAsyncService perfectBoxerAsyncService;
    private final PerfectBoxerCalculator perfectBoxerCalculator;

    public PerfectBoxerService(
            WeightClassRepository weightClassRepository,
            PerfectBoxerGenerationBatchRepository batchRepository,
            AllTimeRankedBoxerRepository rankedBoxerRepository,
            PerfectBoxerRepository perfectBoxerRepository,
            PerfectBoxerAsyncService perfectBoxerAsyncService,
            PerfectBoxerCalculator perfectBoxerCalculator
    ) {
        this.weightClassRepository = weightClassRepository;
        this.batchRepository = batchRepository;
        this.rankedBoxerRepository = rankedBoxerRepository;
        this.perfectBoxerRepository = perfectBoxerRepository;
        this.perfectBoxerAsyncService = perfectBoxerAsyncService;
        this.perfectBoxerCalculator = perfectBoxerCalculator;
    }

    public PerfectBoxer getByWeightClassId(Integer weightClassId) {
        return perfectBoxerRepository.findByWeightClassId(weightClassId)
                .orElseThrow(() -> new IllegalArgumentException("Perfect boxer not found for weight class: " + weightClassId));
    }

    @Transactional
    public PerfectBoxerGenerationStartedResponse generateForWeightClassAsync(Integer weightClassId, Integer amount) {
        WeightClass weightClass = weightClassRepository.findById(weightClassId)
                .orElseThrow(() -> new IllegalArgumentException("Weight class not found: " + weightClassId));

        PerfectBoxerGenerationBatch batch = batchRepository.save(
                PerfectBoxerGenerationBatch.builder()
                        .weightClassId(weightClass.getWeightClassId())
                        .amount(amount)
                        .status("PENDING")
                        .isActive(false)
                        .errorMessage(null)
                        .build()
        );

        perfectBoxerAsyncService.generateForBatchAsync(batch.getBatchId());

        return new PerfectBoxerGenerationStartedResponse(
                batch.getBatchId(),
                batch.getWeightClassId(),
                batch.getAmount(),
                batch.getStatus(),
                "Perfect boxer generation started"
        );
    }

    public PerfectBoxerBatchStatusResponse getBatchStatus(Integer batchId) {
        PerfectBoxerGenerationBatch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("Batch not found: " + batchId));

        Integer perfectBoxerId = perfectBoxerRepository.findByBatchId(batchId)
                .map(PerfectBoxer::getPerfectBoxerId)
                .orElse(null);

        return new PerfectBoxerBatchStatusResponse(
                batch.getBatchId(),
                batch.getWeightClassId(),
                batch.getAmount(),
                batch.getStatus(),
                batch.getIsActive(),
                batch.getErrorMessage(),
                batch.getCreatedAt(),
                perfectBoxerId
        );
    }

    @Transactional
    public PerfectBoxerResponse regenerateForWeightClass(Integer weightClassId) {
        PerfectBoxerGenerationBatch activeBatch = batchRepository.findByWeightClassIdAndIsActiveTrue(weightClassId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No active batch found for weight class: " + weightClassId
                ));

        return regenerateForBatch(activeBatch.getBatchId());
    }

    @Transactional
    public PerfectBoxerResponse regenerateForBatch(Integer batchId) {
        PerfectBoxerGenerationBatch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("Batch not found: " + batchId));

        if (!Boolean.TRUE.equals(batch.getIsActive())) {
            throw new IllegalArgumentException("Batch is not active: " + batchId);
        }

        List<AllTimeRankedBoxer> rankedBoxers =
                rankedBoxerRepository.findByBatchIdOrderByRankingPositionAsc(batchId);

        if (rankedBoxers.isEmpty()) {
            throw new IllegalArgumentException("No ranked boxers found for batch: " + batchId);
        }

        Integer weightClassId = rankedBoxers.getFirst().getWeightClassId();

        PerfectBoxer recalculated = perfectBoxerCalculator.buildFromRankedBoxers(
                batchId,
                weightClassId,
                rankedBoxers
        );

        PerfectBoxer savedPerfectBoxer = perfectBoxerRepository.findByBatchId(batchId)
                .map(existing -> {
                    recalculated.setPerfectBoxerId(existing.getPerfectBoxerId());
                    return perfectBoxerRepository.save(recalculated);
                })
                .orElseGet(() -> perfectBoxerRepository.save(recalculated));

        return mapToResponse(savedPerfectBoxer);
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