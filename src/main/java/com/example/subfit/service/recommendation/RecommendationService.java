package com.example.subfit.service.recommendation;

import com.example.subfit.dto.recommendation.RecommendationRequestDto;
import com.example.subfit.dto.recommendation.RecommendationResponseDto;
import com.example.subfit.dto.recommendation.RecommendationWithComparisonResponseDto;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public ResponseEntity<RecommendationWithComparisonResponseDto> generateRecommendation(RecommendationRequestDto requestDto) {
        try {
            // 인증된 사용자
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);  // 인증되지 않은 경우
            }

            User user = userService.getUserByUserId(authentication.getName());

            // gpt 프롬프트 생성
            StringBuilder prompt = new StringBuilder();
            // 사용자 대답
            StringBuilder userAnswers = new StringBuilder();
            prompt.append("아래가 사용자의 서비스 이용 질문과 질문에 대한 답이야:\n");

            List<RecommendationRequestDto.QuestionAnswerDto> questionAnswers = requestDto.getQuestionAnswers();
            for (int i = 0; i < questionAnswers.size(); i++) {
                RecommendationRequestDto.QuestionAnswerDto qa = questionAnswers.get(i);
                prompt.append(qa.getQuestion()).append(": ").append(qa.getAnswer()).append("\n");

                // 마지막 답변 뒤에는 /가 들어가지 않도록 처리
                userAnswers.append(qa.getAnswer());
                if (i < questionAnswers.size() - 1) {
                    userAnswers.append(" / ");
                }
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
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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

            // gpt 프롬프트 생성
            StringBuilder comparisonPrompt = new StringBuilder();

            switch (requestDto.getType()) {
                case CLOUD:
                    comparisonPrompt.append(cloudServicePromptGenerator.generateComparisonPrompt(recommendedService));
                    break;
                case DELIVERY:
                    comparisonPrompt.append(deliveryServicePromptGenerator.generateComparisonPrompt(recommendedService));
                    break;
                case EBOOK:
                    comparisonPrompt.append(ebookServicePromptGenerator.generateComparisonPrompt(recommendedService));
                    break;
                case MUSIC:
                    comparisonPrompt.append(musicServicePromptGenerator.generateComparisonPrompt(recommendedService));
                    break;
                case OTT:
                    comparisonPrompt.append(ottServicePromptGenerator.generateComparisonPrompt(recommendedService));
                    break;
                default:
                    return ResponseBuilder.badRequest();
            }

            // GPT API 호출 및 응답 처리
            String comparisonResponse = openAIService.getOpenAIResponse(comparisonPrompt.toString());
            List<RecommendationWithComparisonResponseDto.ComparisonData> comparisons = parseComparisonData(comparisonResponse);

            // RecommendationResult 생성 및 저장
            RecommendationResult recommendationResult = RecommendationResult.builder()
                    .user(user)
                    .type(requestDto.getType())
                    .serviceName(recommendedService)
                    .price(price)
                    .benefit1(benefit1)
                    .benefit2(benefit2)
                    .benefit3(benefit3)
                    .userAnswer(userAnswers.toString().trim())
                    .createdAt(LocalDateTime.now())
                    .build();
            recommendationResultRepository.save(recommendationResult);

            // RecommendationWithComparisonResponseDto 생성 및 반환
            RecommendationWithComparisonResponseDto responseDto = RecommendationWithComparisonResponseDto.builder()
                    .id(recommendationResult.getId())
                    .type(requestDto.getType())
                    .serviceName(recommendedService)
                    .price(price)
                    .benefit1(benefit1)
                    .benefit2(benefit2)
                    .benefit3(benefit3)
                    .userAnswer(userAnswers.toString().trim())
                    .comparisons(comparisons)
                    .createdAt(LocalDateTime.now())
                    .build();

            return ResponseBuilder.success(responseDto);

        } catch (JsonProcessingException e) {
            return ResponseBuilder.badRequest();  // JSON 파싱 에러
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBuilder.databaseErrorForRecommendation();  // 일반적인 오류는 500
        }
    }

    private List<RecommendationWithComparisonResponseDto.ComparisonData> parseComparisonData(String comparisonContent) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(comparisonContent);
            String content = responseJson.at("/choices/0/message/content").asText();

            // 단일 인용부호 처리
            String jsonFormattedResponse = content.replace("'", "\"");

            JsonNode rootNode = objectMapper.readTree(jsonFormattedResponse);

            JsonNode serviceList = rootNode.get("추천 서비스 리스트");
            if (serviceList == null || !serviceList.isArray()) {
                throw new RuntimeException("추천 서비스 리스트를 찾을 수 없습니다.");
            }

            List<RecommendationWithComparisonResponseDto.ComparisonData> comparisonList = new ArrayList<>();

            for (JsonNode serviceNode : serviceList) {
                String name = serviceNode.get("추천 서비스").asText();
                String price = serviceNode.get("가격").asText();
                String benefit1 = serviceNode.get("혜택 1").asText();
                String benefit2 = serviceNode.get("혜택 2").asText();

                // ComparisonData 객체 생성 및 리스트에 추가
                RecommendationWithComparisonResponseDto.ComparisonData comparisonData = RecommendationWithComparisonResponseDto.ComparisonData.builder()
                        .name(name)
                        .price(price)
                        .benefit1(benefit1)
                        .benefit2(benefit2)
                        .build();

                comparisonList.add(comparisonData);
            }

            return comparisonList;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return List.of();
        }
    }


    public ResponseEntity<List<RecommendationResponseDto>> getRecommendationList() {
        try {
            // 인증된 사용자
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);  // 인증되지 않은 경우
            }

            User user = userService.getUserByUserId(authentication.getName());

            List<RecommendationResult> recommendations = recommendationResultRepository.findByUser(user);
            List<RecommendationResponseDto> responseDtos = recommendations.stream()
                    .map(recommendation -> RecommendationResponseDto.builder()
                            .id(recommendation.getId())
                            .type(recommendation.getType())
                            .serviceName(recommendation.getServiceName())
                            .price(recommendation.getPrice())
                            .benefit1(recommendation.getBenefit1())
                            .benefit2(recommendation.getBenefit2())
                            .benefit3(recommendation.getBenefit3())
                            .userAnswer(recommendation.getUserAnswer())
                            .createdAt(recommendation.getCreatedAt())
                            .build())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(responseDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBuilder.databaseErrorForList();
        }
    }

    public ResponseEntity<?> deleteRecommendationById(Long recommendationId) {
        try {
            // 인증된 사용자
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);   // 인증되지 않은 경우
            }

            User user = userService.getUserByUserId(authentication.getName());

            RecommendationResult recommendationResult = recommendationResultRepository.findById(recommendationId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "추쳔 결과가 존재하지 않습니다"));

            // 삭제하려는 추천 결과가 현재 사용자의 것인지 확인
            if (!recommendationResult.getUser().equals(user)) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "자신의 추천 결과만 삭제할 수 있습니다.");
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }

            recommendationResultRepository.delete(recommendationResult);
            Map<String, String> response = new HashMap<>();
            response.put("message", "추천 결과가 삭제되었습니다.");
            return ResponseEntity.ok(response);


        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBuilder.databaseError();
        }
    }

}
