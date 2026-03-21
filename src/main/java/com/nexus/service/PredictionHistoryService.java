package com.nexus.service;

import com.nexus.model.PredictionHistory;
import com.nexus.repository.PredictionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PredictionHistoryService {
    private final PredictionHistoryRepository predictionHistoryRepository;

    @Autowired
    public PredictionHistoryService(PredictionHistoryRepository predictionHistoryRepository) {
        this.predictionHistoryRepository = predictionHistoryRepository;
    }

    public void addNewPredictionHistory(PredictionHistory predictionHistory) {
        predictionHistoryRepository.save(predictionHistory);
        System.out.println(predictionHistory);
    }

    // Get all prediction history
    public List<PredictionHistory> getPredictionHistories() {
        return predictionHistoryRepository.findAll();
    }

    // Get prediction history by ID
    public Optional<PredictionHistory> getPredictionHistoryById(Integer id) {
        return predictionHistoryRepository.findById(id);
    }

    public PredictionHistory updatePredictionHistory(Integer id, PredictionHistory updatedPrediction) {

        PredictionHistory existingPrediction = predictionHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "Prediction history with id " + id + " does not exist"));

        existingPrediction.setBoxerAName(updatedPrediction.getBoxerAName());
        existingPrediction.setBoxerBName(updatedPrediction.getBoxerBName());
        existingPrediction.setMatchDecision(updatedPrediction.getMatchDecision());
        existingPrediction.setWeightClassId(updatedPrediction.getWeightClassId());

        existingPrediction.setProbabilityA(updatedPrediction.getProbabilityA());
        existingPrediction.setProbabilityB(updatedPrediction.getProbabilityB());

        existingPrediction.setBreakdownSnapshot(updatedPrediction.getBreakdownSnapshot());

        return predictionHistoryRepository.save(existingPrediction);
    }

    public void deletePredictionHistory(Integer id) {
        predictionHistoryRepository.deleteById(id);
    }
}
