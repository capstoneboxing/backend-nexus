package com.nexus.util;

public record CategoryAttributeWeights(
        double physical,
        double technical,
        double tactical,
        double psychological,
        double experience
) {
}
