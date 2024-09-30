package com.example.subfit.service.recommendation.promptgenerator;

import com.example.subfit.entity.service.CloudService;
import com.example.subfit.repository.service.CloudServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CloudServicePromptGenerator implements ServicePromptGenerator {

    private final CloudServiceRepository cloudServiceRepository;

    public CloudServicePromptGenerator(CloudServiceRepository cloudServiceRepository) {
        this.cloudServiceRepository = cloudServiceRepository;
    }

    @Override
    public String generatePrompt() {
        StringBuilder prompt = new StringBuilder();
        List<CloudService> cloudServices = cloudServiceRepository.findAll();

        prompt.append("아래 내용이 클라우드 서비스들의 내용이야:\n");
        for (CloudService service : cloudServices) {
            prompt.append("Service Name: ").append(service.getName()).append("\n");
            prompt.append("Price per Storage: ").append(service.getPricePerStorage()).append("\n");
            prompt.append("Mobile Benefit: ").append(service.getMobileBenefit()).append("\n");
            prompt.append("Card Benefit: ").append(service.getCardBenefit()).append("\n");
            prompt.append("Primary Use: ").append(service.getPrimaryUse()).append("\n");
            prompt.append("Max Storage: ").append(service.getMaxStorage()).append("\n");
            prompt.append("Auto Backup: ").append(service.isAutoBackup()).append("\n");
            prompt.append("Supported Device: ").append(service.getSupportedDevice()).append("\n");
            prompt.append("File Sharing: ").append(service.isFileSharing()).append("\n");
            prompt.append("Collaboration: ").append(service.isCollaboration()).append("\n");
            prompt.append("OS Support: ").append(service.getOsSupport()).append("\n");
            prompt.append("Special Benefit: ").append(service.getSpecialBenefit()).append("\n\n");
            prompt.append("위 서비스들 중에서 나에게 가장 추천하는 서비스 하나를 알려줘. 가격과 혜택 3가지를 간단히 알려줘. 응답은 아래 형식으로 해줘:").append("\n");
            prompt.append("서비스 이름은 '네이버 클라우드', '아이클라우드', '카카오 톡서랍 플러스', '구글 드라이브', '드롭박스', 원드라이브' 중 하나야 ").append("\n");
            prompt.append("{ '추천 서비스': '서비스 이름', '가격': '가격', '혜택 1': '혜택 설명', '혜택 2': '혜택 설명', '혜택 3': '혜택 설명' }").append("\n");
        }

        return prompt.toString();
    }
}

