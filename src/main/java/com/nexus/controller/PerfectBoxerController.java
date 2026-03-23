package com.nexus.controller;

import com.nexus.dto.admin.AdminResponse;
import com.nexus.dto.perfectboxer.PerfectBoxerResponse;
import com.nexus.model.PerfectBoxer;
import com.nexus.service.PerfectBoxerGenerationService;
import com.nexus.service.PerfectBoxerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/perfect-boxers")
public class PerfectBoxerController {

    private final PerfectBoxerService perfectBoxerService;
    private final PerfectBoxerGenerationService generationService;

    public PerfectBoxerController(
            PerfectBoxerService perfectBoxerService,
            PerfectBoxerGenerationService generationService
    ) {
        this.perfectBoxerService = perfectBoxerService;
        this.generationService = generationService;
    }

    @GetMapping("/weight-class/{weightClassId}")
    public ResponseEntity<PerfectBoxer> getByWeightClass(@PathVariable Integer weightClassId) {
        return ResponseEntity.ok(perfectBoxerService.getByWeightClassId(weightClassId));
    }

    @PostMapping("/generate/{weightClassId}")
    public ResponseEntity<PerfectBoxerResponse> generate(@PathVariable Integer weightClassId) {
        PerfectBoxerResponse response = generationService.generateForWeightClass(weightClassId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/regenerate/weight-class/{weightClassId}")
    public ResponseEntity<PerfectBoxerResponse> regenerateByWeightClass(@PathVariable Integer weightClassId) {
        PerfectBoxerResponse response = generationService.regenerateForWeightClass(weightClassId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/regenerate/batch/{batchId}")
    public ResponseEntity<PerfectBoxerResponse> regenerateByBatch(@PathVariable Integer batchId) {
        PerfectBoxerResponse response = generationService.regenerateForBatch(batchId);
        return ResponseEntity.ok(response);
    }

}