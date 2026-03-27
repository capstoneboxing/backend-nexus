package com.nexus.service;

import com.nexus.dto.predictionHistory.PredictionHistoryResponse;
import com.nexus.dto.predictionHistory.PredictionHistoryUpdateRequest;
import com.nexus.exception.ResourceNotFoundException;
import com.nexus.mapper.PredictionHistoryMapper;
import com.nexus.model.PredictionHistory;
import com.nexus.repository.PredictionHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PredictionHistoryService {
    private final PredictionHistoryRepository predictionHistoryRepository;
    private final PredictionHistoryMapper predictionHistoryMapper;

    public PredictionHistoryService(
            PredictionHistoryRepository predictionHistoryRepository,
            PredictionHistoryMapper predictionHistoryMapper
    ) {
        this.predictionHistoryRepository = predictionHistoryRepository;
        this.predictionHistoryMapper = predictionHistoryMapper;
    }

    public void addNewPredictionHistory(PredictionHistory predictionHistory) {
        predictionHistoryRepository.save(predictionHistory);
    }

    public List<PredictionHistoryResponse> getPredictionHistories() {
        return predictionHistoryRepository.findAll()
                .stream()
                .map(predictionHistoryMapper::toResponse)
                .toList();
    }

    public PredictionHistoryResponse getPredictionHistoryById(Integer id) {
        PredictionHistory predictionHistory = predictionHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prediction history with id " + id + " does not exist"
                ));

        return predictionHistoryMapper.toResponse(predictionHistory);
    }

    public PredictionHistoryResponse updatePredictionHistory(Integer id, PredictionHistoryUpdateRequest request) {
        PredictionHistory existingPrediction = predictionHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prediction history with id " + id + " does not exist"
                ));

        existingPrediction.setBoxerAName(request.boxerAName());
        existingPrediction.setBoxerBName(request.boxerBName());
        existingPrediction.setMatchDecision(request.matchDecision());
        existingPrediction.setWeightClassId(request.weightClassId());
        existingPrediction.setProbabilityA(request.probabilityA());
        existingPrediction.setProbabilityB(request.probabilityB());
        existingPrediction.setBreakdownSnapshot(request.breakdownSnapshot());

        PredictionHistory savedPrediction = predictionHistoryRepository.save(existingPrediction);
        return predictionHistoryMapper.toResponse(savedPrediction);
    }

    public void deletePredictionHistory(Integer id) {
        if (!predictionHistoryRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Prediction history with id " + id + " does not exist"
            );
        }
        predictionHistoryRepository.deleteById(id);
    }
}