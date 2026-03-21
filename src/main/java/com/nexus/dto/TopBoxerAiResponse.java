package com.nexus.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopBoxerAiResponse {
    private List<TopBoxerAiProfile> boxers;
}