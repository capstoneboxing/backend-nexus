package com.nexus.dto.weightClass;

public record WeightClassResponse(
        Integer weightClassId,
        String className,
        Double maxWeightLb,
        Double minWeightLb
) {
}
