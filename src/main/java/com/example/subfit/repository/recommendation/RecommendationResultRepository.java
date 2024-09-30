package com.example.subfit.repository.recommendation;

import com.example.subfit.entity.recommendation.RecommendationResult;
import com.example.subfit.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendationResultRepository extends JpaRepository<RecommendationResult, Long> {
    List<RecommendationResult> findByUser(User user);
}
