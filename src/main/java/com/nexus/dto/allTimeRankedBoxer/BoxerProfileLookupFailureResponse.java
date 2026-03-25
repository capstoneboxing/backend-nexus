package com.nexus.dto.allTimeRankedBoxer;

public record BoxerProfileLookupFailureResponse(
        boolean boxerFound,
        double confidence,
        String message
) {
}