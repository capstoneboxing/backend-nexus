package com.nexus.dto.perfectboxer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Response returned when perfect boxer generation has started")
public class PerfectBoxerGenerationStartedResponse {

    @Schema(description = "Generated batch ID", example = "12")
    private Integer batchId;

    @Schema(description = "Weight class ID being processed", example = "11")
    private Integer weightClassId;

    @Schema(description = "Number of top ranked boxers requested for the generation", example = "10")
    private Integer amount;

    @Schema(description = "Current batch status", example = "PENDING")
    private String status;

    @Schema(description = "User-friendly status message", example = "Perfect boxer generation started")
    private String message;
}