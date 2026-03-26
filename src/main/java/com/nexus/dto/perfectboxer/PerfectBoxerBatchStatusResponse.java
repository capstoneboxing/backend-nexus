package com.nexus.dto.perfectboxer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class PerfectBoxerBatchStatusResponse {
    private Integer batchId;
    private Integer weightClassId;
    private Integer amount;
    private String status;
    private Boolean isActive;
    private String errorMessage;
    private OffsetDateTime createdAt;
    private Integer perfectBoxerId;
}