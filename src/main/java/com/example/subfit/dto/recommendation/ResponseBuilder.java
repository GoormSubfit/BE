package com.example.subfit.dto.recommendation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseBuilder {

    public static ResponseEntity<RecommendationResponseDto> success(RecommendationResponseDto responseBody) {
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    public static ResponseEntity<RecommendationResponseDto> databaseError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public static ResponseEntity<List<RecommendationResponseDto>> databaseErrorForList() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public static ResponseEntity<RecommendationResponseDto> badRequest() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    public static ResponseEntity<RecommendationResponseDto> serviceUnavailable() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}