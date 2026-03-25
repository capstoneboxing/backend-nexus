package com.nexus.dto.ai;

import lombok.Data;

@Data
public class SingleBoxerAiResponse {
    private Boolean boxerFound;
    private Double confidence;
    private String matchReason;
    private BoxerAiProfile boxer;
}