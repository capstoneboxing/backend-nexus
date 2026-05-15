package com.nexus.service;

import com.nexus.dto.prediction.AttributeRange;
import com.nexus.model.AllTimeRankedBoxer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class PredictionNormalizationService {

    public Map<String, AttributeRange> buildRanges(List<AllTimeRankedBoxer> rankedBoxers) {
        if (rankedBoxers == null || rankedBoxers.isEmpty()) {
            throw new IllegalArgumentException("Ranked boxers are required to build normalization ranges.");
        }

        Map<String, AttributeRange> ranges = new HashMap<>();

        // Only raw measurement fields use dynamic min-max normalization
        ranges.put("heightCm", range(rankedBoxers, AllTimeRankedBoxer::getHeightCm));
        ranges.put("reachCm", range(rankedBoxers, AllTimeRankedBoxer::getReachCm));

        return ranges;
    }

    private AttributeRange range(List<AllTimeRankedBoxer> list, Function<AllTimeRankedBoxer, Double> extractor) {
        double min = list.stream()
                .map(extractor)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .min()
                .orElse(0.0);

        double max = list.stream()
                .map(extractor)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(1.0);

        return new AttributeRange(min, max);
    }

    public double normalizeMeasurement(Double value, AttributeRange range) {
        if (value == null || range == null) {
            System.out.println("measurement -> default 0.5 (null input)");
            return 0.5;
        }

        if (Double.compare(range.max(), range.min()) == 0) {
            System.out.println("measurement -> default 0.5 (min == max)");
            return 0.5;
        }

        double raw = (value - range.min()) / (range.max() - range.min());
        return Math.clamp(raw, 0.0, 1.0);
    }

    public double normalizeRubricScore(Double value) {
        if (value == null) {
            System.out.println("rubric -> default 0.5 (null input)");
            return 0.5;
        }

        double raw = (value - 1.0) / 9.0;
        return Math.clamp(raw, 0.0, 1.0);
    }

    public double normalizeRatio(Double value) {
        if (value == null) {
            System.out.println("ratio -> default 0.5 (null input)");
            return 0.5;
        }

        return Math.clamp(value, 0.0, 1.0);
    }
}