package com.nexus.mapper;

import com.nexus.dto.predictionHistory.PredictionHistoryResponse;
import com.nexus.model.PredictionHistory;
import org.springframework.stereotype.Component;

@Component
public class PredictionHistoryMapper {

    public PredictionHistoryResponse toResponse(PredictionHistory predictionHistory) {
        return new PredictionHistoryResponse(
                predictionHistory.getPredictionId(),
                predictionHistory.getBoxerAName(),
                predictionHistory.getBoxerBName(),
                predictionHistory.getMatchDecision(),
                predictionHistory.getWeightClassId(),
                predictionHistory.getProbabilityA(),
                predictionHistory.getProbabilityB(),
                predictionHistory.getBreakdownSnapshot(),
                predictionHistory.getPredictionDate()
        );
    }
}