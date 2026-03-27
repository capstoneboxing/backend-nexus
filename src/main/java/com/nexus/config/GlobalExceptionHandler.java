package com.nexus.config;

import com.nexus.dto.allTimeRankedBoxer.BoxerProfileLookupFailureResponse;
import com.nexus.dto.error.ApiErrorResponse;
import com.nexus.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiErrorResponse(
                        OffsetDateTime.now(),
                        404,
                        "Not Found",
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(
                        OffsetDateTime.now(),
                        400,
                        "Bad Request",
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiErrorResponse(
                        OffsetDateTime.now(),
                        409,
                        "Conflict",
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ApiErrorResponse(
                        OffsetDateTime.now(),
                        401,
                        "Unauthorized",
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(BoxerProfileLookupException.class)
    public ResponseEntity<BoxerProfileLookupFailureResponse> handleBoxerProfileLookup(BoxerProfileLookupException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new BoxerProfileLookupFailureResponse(
                        false,
                        ex.getConfidence(),
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(
                        OffsetDateTime.now(),
                        400,
                        "Bad Request",
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(
                        OffsetDateTime.now(),
                        400,
                        "Validation Failed",
                        message
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiErrorResponse(
                        OffsetDateTime.now(),
                        500,
                        "Internal Server Error",
                        ex.getMessage()
                )
        );
    }
}