package com.nexus.dto.perfectboxer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PerfectBoxerGenerationStartedResponse {
    private Integer batchId;
    private Integer weightClassId;
    private Integer amount;
    private String status;
    private String message;
}