package com.example.subfit.service.recommendation;

import com.example.subfit.dto.recommendation.RecommendationRequestDto;
import com.example.subfit.dto.recommendation.RecommendationResponseDto;
import com.example.subfit.dto.recommendation.ResponseBuilder;
import com.example.subfit.entity.recommendation.RecommendationResult;
import com.example.subfit.entity.user.User;
import com.example.subfit.repository.recommendation.RecommendationResultRepository;
import com.example.subfit.service.recommendation.promptgenerator.*;
import com.example.subfit.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class RecommendationService {

    private final CloudServicePromptGenerator cloudServicePromptGenerator;
    private final DeliveryServicePromptGenerator deliveryServicePromptGenerator;
    private final EbookServicePromptGenerator ebookServicePromptGenerator;
    private final MusicServicePromptGenerator musicServicePromptGenerator;
    private final OTTServicePromptGenerator ottServicePromptGenerator;
    private final UserService userService;
    private final OpenAIService openAIService;
    private final RecommendationResultRepository recommendationResultRepository;

    public RecommendationService(
            CloudServicePromptGenerator cloudServicePromptGenerator,
            DeliveryServicePromptGenerator deliveryServicePromptGenerator,
            EbookServicePromptGenerator ebookServicePromptGenerator,
            MusicServicePromptGenerator musicServicePromptGenerator,
            OTTServicePromptGenerator ottServicePromptGenerator,
            UserService userService,
            OpenAIService openAIService,
            RecommendationResultRepository recommendationResultRepository) {
        this.cloudServicePromptGenerator = cloudServicePromptGenerator;
        this.deliveryServicePromptGenerator = deliveryServicePromptGenerator;
        this.ebookServicePromptGenerator = ebookServicePromptGenerator;
        this.musicServicePromptGenerator = musicServicePromptGenerator;
        this.ottServicePromptGenerator = ottServicePromptGenerator;
        this.userService = userService;
        this.openAIService = openAIService;
        this.recommendationResultRepository = recommendationResultRepository;
    }

    public ResponseEntity<RecommendationResponseDto> generateRecommendation(RecommendationRequestDto requestDto) {
        try {
            // 인증된 사용자
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);  // 인증되지 않은 경우
            }

            User user = userService.getUserByUserId(authentication.getName());

            // gpt 프롬프트 생성
            StringBuilder prompt = new StringBuilder();
            prompt.append("아래가 사용자의 서비스 이용 질문과 질문에 대한 답이야:\n");

            for (RecommendationRequestDto.QuestionAnswerDto qa : requestDto.getQuestionAnswers()) {
                prompt.append(qa.getQuestion()).append(": ").append(qa.getAnswer()).append("\n");
            }

            switch (requestDto.getType()) {
                case CLOUD:
                    prompt.append(cloudServicePromptGenerator.generatePrompt());
                    break;
                case DELIVERY:
                    prompt.append(deliveryServicePromptGenerator.generatePrompt());
                    break;
                case EBOOK:
                    prompt.append(ebookServicePromptGenerator.generatePrompt());
                    break;
                case MUSIC:
                    prompt.append(musicServicePromptGenerator.generatePrompt());
                    break;
                case OTT:
                    prompt.append(ottServicePromptGenerator.generatePrompt());
                    break;
                default:
                    return ResponseBuilder.badRequest();
            }

            // GPT API 호출 및 응답 처리
            String gptResponse = openAIService.getOpenAIResponse(prompt.toString());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(gptResponse);
            String content = responseJson.at("/choices/0/message/content").asText();

            // 단일 인용부호 처리
            String jsonFormattedResponse = content.replace("'", "\"");

            // 파싱하여 추천 결과 추출
            JsonNode parsedResponse = objectMapper.readTree(jsonFormattedResponse);
            String recommendedService = parsedResponse.get("추천 서비스").asText();
            String price = parsedResponse.get("가격").asText();
            String benefit1 = parsedResponse.get("혜택 1").asText();
            String benefit2 = parsedResponse.get("혜택 2").asText();
            String benefit3 = parsedResponse.get("혜택 3").asText();

            // RecommendationResult 생성 및 저장
            RecommendationResult recommendationResult = RecommendationResult.builder()
                    .user(user)
                    .type(requestDto.getType())
                    .serviceName(recommendedService)
                    .price(price)
                    .benefit1(benefit1)
                    .benefit2(benefit2)
                    .benefit3(benefit3)
                    .createdAt(LocalDateTime.now())
                    .build();
            recommendationResultRepository.save(recommendationResult);

            // RecommendationResponseDto 생성 및 반환
            RecommendationResponseDto responseDto = RecommendationResponseDto.builder()
                    .type(requestDto.getType())
                    .serviceName(recommendedService)
                    .price(price)
                    .benefit1(benefit1)
                    .benefit2(benefit2)
                    .benefit3(benefit3)
                    .createdAt(LocalDateTime.now())
                    .build();

            return ResponseBuilder.success(responseDto);

        } catch (JsonProcessingException e) {
            return ResponseBuilder.badRequest();  // JSON 파싱 에러
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBuilder.databaseError();  // 일반적인 오류는 500
        }
    }
}
