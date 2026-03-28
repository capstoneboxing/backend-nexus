package com.nexus.dto.perfectboxer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@Schema(description = "Response containing the status of a perfect boxer generation batch")
public class PerfectBoxerBatchStatusResponse {

    @Schema(description = "Batch ID", example = "12")
    private Integer batchId;

    @Schema(description = "Weight class ID being processed in the batch", example = "11")
    private Integer weightClassId;

    @Schema(description = "Number of ranked boxers used for the batch", example = "10")
    private Integer amount;

    @Schema(description = "Current batch status", example = "COMPLETED")
    private String status;

    @Schema(description = "Whether this batch is the currently active batch for the weight class", example = "true")
    private Boolean isActive;

    @Schema(description = "Error message if the batch failed, otherwise null", example = "null", nullable = true)
    private String errorMessage;

    @Schema(description = "Date and time when the batch was created", example = "2026-03-27T18:30:00Z")
    private OffsetDateTime createdAt;

    @Schema(description = "Generated perfect boxer ID if available, otherwise null", example = "3", nullable = true)
    private Integer perfectBoxerId;
}