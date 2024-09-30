package com.example.subfit.dto.recommendation;

import com.example.subfit.entity.recommendation.Type;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecommendationRequestDto {
    @NotNull(message = "타입은 필수 입력 사항입니다.")
    private Type type;
    private List<QuestionAnswerDto> questionAnswers;

    public static class QuestionAnswerDto {
        private String question;
        private String answer;

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }
    }
}
