package com.example.subfit.dto.recommendation;

import com.example.subfit.entity.recommendation.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RecommendationResponseDto {
    private Long id;
    private Type type;
    private String serviceName;
    private String price;
    private String benefit1;
    private String benefit2;
    private String benefit3;
    private String userAnswer;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
}
