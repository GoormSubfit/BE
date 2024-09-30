package com.example.subfit.controller.recommendation;

import com.example.subfit.dto.recommendation.RecommendationRequestDto;
import com.example.subfit.dto.recommendation.RecommendationResponseDto;
import com.example.subfit.service.recommendation.RecommendationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    // 추천 결과 생성
    @PostMapping("/create")
    public ResponseEntity<RecommendationResponseDto> generateRecommendation(@Valid @RequestBody RecommendationRequestDto recommendationRequestDto) {
        return recommendationService.generateRecommendation(recommendationRequestDto);
    }

}
