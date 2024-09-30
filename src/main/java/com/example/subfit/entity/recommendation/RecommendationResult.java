package com.example.subfit.entity.recommendation;

import com.example.subfit.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "recommendation_results")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Column(name = "service_name", length = 50)
    private String serviceName;

    @Column(name = "price", length = 50)
    private String price;

    @Column(name = "benefit_1", length = 100)
    private String benefit1;

    @Column(name = "benefit_2", length = 100)
    private String benefit2;

    @Column(name = "benefit_3", length = 100)
    private String benefit3;

    @Column(name = "user_answer", length = 255)
    private String userAnswer;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
