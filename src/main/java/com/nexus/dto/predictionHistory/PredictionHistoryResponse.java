package com.nexus.dto.predictionHistory;

import java.time.OffsetDateTime;

public record PredictionHistoryResponse(
        Integer predictionId,
        String boxerAName,
        String boxerBName,
        String matchDecision,
        Integer weightClassId,
        Double probabilityA,
        Double probabilityB,
        String breakdownSnapshot,
        OffsetDateTime predictionDate
) {
}
