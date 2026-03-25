package com.nexus.exception;

import lombok.Getter;

@Getter
public class BoxerProfileLookupException extends RuntimeException {
    private final double confidence;

    public BoxerProfileLookupException(String message, double confidence) {
        super(message);
        this.confidence = confidence;
    }

}