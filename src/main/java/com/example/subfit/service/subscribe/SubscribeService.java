package com.example.subfit.service.subscribe;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.subfit.dto.subscribe.SubscribeRequestDto;
import com.example.subfit.dto.subscribe.SubscribeResponseDto;
import com.example.subfit.entity.subscribe.Subscribe;
import com.example.subfit.entity.user.User;
import com.example.subfit.repository.subscribe.SubscribeRepository;
import com.example.subfit.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final UserService userService;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Autowired
    public SubscribeService(SubscribeRepository subscribeRepository, UserService userService, AmazonS3 amazonS3) {
        this.subscribeRepository = subscribeRepository;
        this.userService = userService;
        this.amazonS3 = amazonS3;
    }

    public SubscribeResponseDto addSubscription(SubscribeRequestDto requestDto, String userId) {
        Subscribe subscribe = new Subscribe();

        User user = userService.getUserByUserId(userId);
        subscribe.setUser(user);

        subscribe.setName(requestDto.getName());
        subscribe.setPrice(requestDto.getPrice());
        subscribe.setCycle(requestDto.getCycle());

        // subscribeDate를 문자열로 받은 후 LocalDate로 변환
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedSubscribeDate = LocalDate.parse(requestDto.getSubscribeDate(), dateFormatter);
        subscribe.setSubscribeDate(parsedSubscribeDate);
        // 로고 이미지 S3 업로드
        MultipartFile logoImage = requestDto.getLogoImage();
        String logoUrl = uploadFileToS3(logoImage);
        subscribe.setLogo(logoUrl);

        subscribe.setCreatedAt(LocalDateTime.now());
        subscribe.setUpdatedAt(LocalDateTime.now());

        Subscribe savedSubscribe = subscribeRepository.save(subscribe);

        return convertToResponseDto(savedSubscribe);
    }

    public List<SubscribeResponseDto> getSubscriptions(String userId) {
        User user = userService.getUserByUserId(userId);
        List<Subscribe> subscriptions = subscribeRepository.findByUserId(user.getId());

        return subscriptions.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    public Map<String, Object> getSubscriptionSummary(String userId) {
        User user = userService.getUserByUserId(userId);
        List<Subscribe> subscriptions = subscribeRepository.findByUserId(user.getId());

        int totalSubscriptions = subscriptions.size();
        int totalSpending = subscriptions.stream().mapToInt(Subscribe::getPrice).sum();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalSubscriptions", totalSubscriptions);
        summary.put("totalSpending", totalSpending);

        return summary;
    }
    public SubscribeResponseDto updateSubscription(Long subscriptionId, SubscribeRequestDto requestDto, String userId) {
        User user = userService.getUserByUserId(userId);
        Optional<Subscribe> optionalSubscribe = subscribeRepository.findById(subscriptionId);

        if (optionalSubscribe.isEmpty() || !optionalSubscribe.get().getUser().equals(user)) {
            throw new IllegalArgumentException("구독 정보를 찾을 수 없거나 권한이 없습니다.");
        }

        Subscribe subscribe = optionalSubscribe.get();

        subscribe.setName(requestDto.getName());
        subscribe.setPrice(requestDto.getPrice());
        subscribe.setCycle(requestDto.getCycle());

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedSubscribeDate = LocalDate.parse(requestDto.getSubscribeDate(), dateFormatter);
        subscribe.setSubscribeDate(parsedSubscribeDate);

        subscribe.setUpdatedAt(LocalDateTime.now());

        Subscribe updatedSubscribe = subscribeRepository.save(subscribe);

        return convertToResponseDto(updatedSubscribe);
    }
    public void deleteSubscription(Long subscriptionId, String userId) {
        User user = userService.getUserByUserId(userId);
        Optional<Subscribe> optionalSubscribe = subscribeRepository.findById(subscriptionId);

        if (optionalSubscribe.isEmpty() || !optionalSubscribe.get().getUser().equals(user)) {
            throw new IllegalArgumentException("구독 정보를 찾을 수 없거나 권한이 없습니다.");
        }

        subscribeRepository.delete(optionalSubscribe.get());
    }
    public SubscribeResponseDto getSubscriptionDetail(Long subscriptionId, String userId) {
        User user = userService.getUserByUserId(userId);
        Optional<Subscribe> optionalSubscribe = subscribeRepository.findById(subscriptionId);

        if (optionalSubscribe.isEmpty() || !optionalSubscribe.get().getUser().equals(user)) {
            throw new IllegalArgumentException("구독 정보를 찾을 수 없거나 권한이 없습니다.");
        }

        Subscribe subscribe = optionalSubscribe.get();
        return convertToResponseDto(subscribe);
    }
    // S3에 파일 업로드
    private String uploadFileToS3(MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }

        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    private SubscribeResponseDto convertToResponseDto(Subscribe subscribe) {
        SubscribeResponseDto responseDto = new SubscribeResponseDto();
        responseDto.setId(subscribe.getId());
        responseDto.setName(subscribe.getName());
        responseDto.setPrice(subscribe.getPrice());
        responseDto.setCycle(subscribe.getCycle());
        responseDto.setLogoUrl(subscribe.getLogo());
        responseDto.setSubscribeDate(subscribe.getSubscribeDate().toString());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일");
        responseDto.setCreatedAt(subscribe.getCreatedAt().format(formatter));
        responseDto.setUpdatedAt(subscribe.getUpdatedAt().format(formatter));

        return responseDto;
    }
}
