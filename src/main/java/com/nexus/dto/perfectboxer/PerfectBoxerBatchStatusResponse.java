package com.nexus.dto.perfectboxer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class PerfectBoxerBatchStatusResponse {
    private Integer batchId;
    private Integer weightClassId;
    private String status;
    private String errorMessage;
    private Boolean isActive;
    private OffsetDateTime createdAt;
    private Integer perfectBoxerId;
}