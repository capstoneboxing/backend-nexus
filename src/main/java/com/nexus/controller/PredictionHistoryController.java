package com.nexus.controller;

import com.nexus.model.PredictionHistory;
import com.nexus.service.PredictionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="api/v1/prediction-history")
public class PredictionHistoryController {
    private final PredictionHistoryService predictionHistoryService;

    @Autowired
    public PredictionHistoryController(PredictionHistoryService predictionHistoryService) {
        this.predictionHistoryService = predictionHistoryService;
    }

    @GetMapping
    public List<PredictionHistory> getPredictionHistories() {
        return predictionHistoryService.getPredictionHistories();
    }

    // Get prediction history class by ID
    @GetMapping(path = "/{id}")
    public Optional<PredictionHistory> getPredictionHistoryById(@PathVariable Integer id) {
        return predictionHistoryService.getPredictionHistoryById(id);
    }

    // Delete prediction history class
    @DeleteMapping(path = "/{id}")
    public void deleteWeightClass(@PathVariable Integer id) {
        predictionHistoryService.deletePredictionHistory(id);
    }

}
