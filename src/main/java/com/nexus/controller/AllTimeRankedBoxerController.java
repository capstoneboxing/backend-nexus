package com.nexus.controller;

import com.nexus.dto.allTimeRankedBoxer.AllTimeRankedBoxerResponse;
import com.nexus.dto.allTimeRankedBoxer.AllTimeRankedBoxerUpdateRequest;
import com.nexus.dto.allTimeRankedBoxer.BoxerProfileLookupFailureResponse;
import com.nexus.dto.allTimeRankedBoxer.GenerateBoxerProfileRequest;
import com.nexus.exception.BoxerProfileLookupException;
import com.nexus.service.AllTimeRankedBoxerService;
import jakarta.validation.Valid;
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
            @Valid @RequestBody AllTimeRankedBoxerUpdateRequest request
    ) {
        return ResponseEntity.ok(rankedBoxerService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        rankedBoxerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/generate-profile")
    public ResponseEntity<?> generateProfile(@Valid @RequestBody GenerateBoxerProfileRequest request) {
        try {
            return ResponseEntity.ok(
                    rankedBoxerService.generateBoxerProfile(
                            request.boxerName(),
                            request.weightClassId()
                    )
            );
        } catch (BoxerProfileLookupException ex) {
            return ResponseEntity.status(404).body(
                    new BoxerProfileLookupFailureResponse(
                            false,
                            ex.getConfidence(),
                            ex.getMessage()
                    )
            );
        }
    }
}