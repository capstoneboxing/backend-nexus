package com.nexus.controller;

import com.nexus.dto.perfectboxer.PerfectBoxerBatchStatusResponse;
import com.nexus.dto.perfectboxer.PerfectBoxerGenerationRequest;
import com.nexus.dto.perfectboxer.PerfectBoxerGenerationStartedResponse;
import com.nexus.dto.perfectboxer.PerfectBoxerResponse;
import com.nexus.model.PerfectBoxer;
import com.nexus.service.PerfectBoxerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/perfect-boxers")
public class PerfectBoxerController {
    private final PerfectBoxerService perfectBoxerService;

    public PerfectBoxerController(
            PerfectBoxerService generationService
    ) {
        this.perfectBoxerService = generationService;
    }

    @GetMapping("/weight-class/{weightClassId}")
    public ResponseEntity<PerfectBoxer> getByWeightClass(@PathVariable Integer weightClassId) {
        return ResponseEntity.ok(perfectBoxerService.getByWeightClassId(weightClassId));
    }

    @PostMapping("/generate")
    public ResponseEntity<PerfectBoxerGenerationStartedResponse> generate(
            @Valid @RequestBody PerfectBoxerGenerationRequest request
    ) {
        PerfectBoxerGenerationStartedResponse response =
                perfectBoxerService.generateForWeightClassAsync(request.weightClassId(), request.amount());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/batches/{batchId}/status")
    public ResponseEntity<PerfectBoxerBatchStatusResponse> getBatchStatus(@PathVariable Integer batchId) {
        return ResponseEntity.ok(perfectBoxerService.getBatchStatus(batchId));
    }

    @PostMapping("/regenerate/weight-class/{weightClassId}")
    public ResponseEntity<PerfectBoxerResponse> regenerateByWeightClass(@PathVariable Integer weightClassId) {
        PerfectBoxerResponse response = perfectBoxerService.regenerateForWeightClass(weightClassId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/regenerate/batch/{batchId}")
    public ResponseEntity<PerfectBoxerResponse> regenerateByBatch(@PathVariable Integer batchId) {
        PerfectBoxerResponse response = perfectBoxerService.regenerateForBatch(batchId);
        return ResponseEntity.ok(response);
    }

}