package com.nexus.controller;

import com.nexus.dto.allTimeRankedBoxer.AllTimeRankedBoxerResponse;
import com.nexus.dto.allTimeRankedBoxer.AllTimeRankedBoxerUpdateRequest;
import com.nexus.service.AllTimeRankedBoxerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/all-time-ranked-boxers")
public class AllTimeRankedBoxerController {
    private final AllTimeRankedBoxerService rankedBoxerService;

    public AllTimeRankedBoxerController(AllTimeRankedBoxerService rankedBoxerService) {
        this.rankedBoxerService = rankedBoxerService;
    }

    @GetMapping
    public ResponseEntity<List<AllTimeRankedBoxerResponse>> getAll() {
        return ResponseEntity.ok(rankedBoxerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AllTimeRankedBoxerResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(rankedBoxerService.findById(id));
    }

    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<AllTimeRankedBoxerResponse>> getByBatchId(@PathVariable Integer batchId) {
        return ResponseEntity.ok(rankedBoxerService.findByBatchId(batchId));
    }

    @GetMapping("/weight-class/{weightClassId}")
    public ResponseEntity<List<AllTimeRankedBoxerResponse>> getByWeightClassId(@PathVariable Integer weightClassId) {
        return ResponseEntity.ok(rankedBoxerService.findByWeightClassId(weightClassId));
    }

    @GetMapping("/weight-class/{weightClassId}/active")
    public ResponseEntity<List<AllTimeRankedBoxerResponse>> getActiveByWeightClassId(@PathVariable Integer weightClassId) {
        return ResponseEntity.ok(rankedBoxerService.findActiveByWeightClassId(weightClassId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AllTimeRankedBoxerResponse> update(
            @PathVariable Integer id,
            @RequestBody AllTimeRankedBoxerUpdateRequest request
    ) {
        return ResponseEntity.ok(rankedBoxerService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        rankedBoxerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}