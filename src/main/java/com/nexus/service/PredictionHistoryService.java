package com.nexus.service;

import com.nexus.dto.prediction.PredictionHistoryResponse;
import com.nexus.dto.prediction.PredictionResultUpdateRequest;
import com.nexus.exception.ResourceNotFoundException;
import com.nexus.mapper.PredictionHistoryMapper;
import com.nexus.model.PredictionHistory;
import com.nexus.repository.PredictionHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

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

    public PredictionHistoryResponse updatePredictionHistory(Integer id, PredictionResultUpdateRequest request) {
        PredictionHistory existingPrediction = predictionHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prediction history with id " + id + " does not exist"
                ));

        if (request.matchWinner() == null && request.matchWinMethod() != null) {
            throw new IllegalArgumentException(
                    "matchWinMethod cannot be provided when matchWinner is null"
            );
        }

        String normalizedMatchWinner = normalizeToUpper(request.matchWinner());
        String normalizedMatchWinMethod = normalizeToUpper(request.matchWinMethod());

        existingPrediction.setMatchWinner(normalizedMatchWinner);
        existingPrediction.setMatchWinMethod(normalizedMatchWinMethod);

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

    private String normalizeToUpper(String value) {
        return value == null ? null : value.toUpperCase(Locale.ROOT);
    }
}