package com.example.subfit.controller.recommendation;

import com.example.subfit.dto.recommendation.RecommendationRequestDto;
import com.example.subfit.dto.recommendation.RecommendationResponseDto;
import com.example.subfit.dto.recommendation.RecommendationWithComparisonResponseDto;
import com.example.subfit.service.recommendation.RecommendationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    // 추천 결과 생성
    @PostMapping("/create")
    public ResponseEntity<RecommendationWithComparisonResponseDto> generateRecommendation(@Valid @RequestBody RecommendationRequestDto recommendationRequestDto) {
        return recommendationService.generateRecommendation(recommendationRequestDto);
    }

    // 사용자가 생성한 추천 결과 리스트 반환
    @GetMapping("/list")
    public ResponseEntity<List<RecommendationResponseDto>> getRecommendationList() {
        return recommendationService.getRecommendationList();
    }

    // 추천 결과 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRecommendation(@PathVariable Long id) {
        return recommendationService.deleteRecommendationById(id);
    }

}
