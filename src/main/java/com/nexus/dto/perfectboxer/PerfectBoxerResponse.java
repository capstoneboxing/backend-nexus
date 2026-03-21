package com.nexus.dto.perfectboxer;

import java.time.OffsetDateTime;

public record PerfectBoxerResponse(
        Integer perfectBoxerId,
        Integer batchId,
        Integer weightClassId,
        OffsetDateTime createdAt
) {
}