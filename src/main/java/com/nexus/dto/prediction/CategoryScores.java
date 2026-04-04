package com.nexus.dto.prediction;

public record CategoryScores(
        Double physical,
        Double technical,
        Double tactical,
        Double psychological,
        Double experience
) {}