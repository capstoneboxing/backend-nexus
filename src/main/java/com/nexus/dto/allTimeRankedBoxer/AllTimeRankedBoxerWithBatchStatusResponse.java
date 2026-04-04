package com.nexus.dto.allTimeRankedBoxer;

public record AllTimeRankedBoxerWithBatchStatusResponse(
        AllTimeRankedBoxerResponse boxer,
        boolean isActive
) {}