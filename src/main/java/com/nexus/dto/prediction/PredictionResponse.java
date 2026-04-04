package com.nexus.dto.prediction;

import java.time.OffsetDateTime;

public record PredictionResponse(
        Integer predictionId,
        String boxerAName,
        String boxerBName,
        String predictedWinner,
        Integer weightClassId,
        Double closenessA,
        Double closenessB,
        Double probabilityA,
        Double probabilityB,
        String explanation,
        OffsetDateTime predictionDate
) {}