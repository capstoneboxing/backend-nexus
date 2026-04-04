package com.nexus.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.dto.prediction.PredictionHistoryResponse;
import com.nexus.model.PredictionHistory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PredictionHistoryMapper {

    private final ObjectMapper objectMapper;

    public PredictionHistoryMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public PredictionHistoryResponse toResponse(PredictionHistory predictionHistory) {
        Map<String, Object> breakdownSnapshot = null;

        if (predictionHistory.getBreakdownSnapshot() != null) {
            breakdownSnapshot = objectMapper.convertValue(
                    predictionHistory.getBreakdownSnapshot(),
                    new TypeReference<Map<String, Object>>() {}
            );
        }

        return new PredictionHistoryResponse(
                predictionHistory.getPredictionId(),
                predictionHistory.getBoxerAName(),
                predictionHistory.getBoxerBName(),
                predictionHistory.getPredictedWinner(),
                predictionHistory.getMatchWinner(),
                predictionHistory.getMatchWinMethod(),
                predictionHistory.getWeightClassId(),
                predictionHistory.getBoxerAClosenessScore(),
                predictionHistory.getBoxerBClosenessScore(),
                predictionHistory.getProbabilityA(),
                predictionHistory.getProbabilityB(),
                breakdownSnapshot,
                predictionHistory.getPredictionDate()
        );
    }
}