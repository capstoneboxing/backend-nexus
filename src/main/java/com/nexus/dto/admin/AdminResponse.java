package com.nexus.dto.admin;

import java.time.OffsetDateTime;

public record AdminResponse(
        Integer adminId,
        String username,
        OffsetDateTime createdAt
) {
}