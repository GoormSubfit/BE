package com.example.subfit.repository.recommendation;

import com.example.subfit.entity.recommendation.RecommendationResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationResultRepository extends JpaRepository<RecommendationResult, Long> {
}
