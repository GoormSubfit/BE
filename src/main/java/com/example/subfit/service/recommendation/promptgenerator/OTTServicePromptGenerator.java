package com.example.subfit.service.recommendation.promptgenerator;

import com.example.subfit.entity.service.OTTService;
import com.example.subfit.repository.service.OTTServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OTTServicePromptGenerator implements ServicePromptGenerator {

    private final OTTServiceRepository ottServiceRepository;

    public OTTServicePromptGenerator(OTTServiceRepository ottServiceRepository) {
        this.ottServiceRepository = ottServiceRepository;
    }

    @Override
    public String generatePrompt() {
        StringBuilder prompt = new StringBuilder();
        List<OTTService> ottServices = ottServiceRepository.findAll();

        prompt.append("아래 내용이 OTT 서비스들의 내용이야:\n");
        for (OTTService service : ottServices) {
            prompt.append("Service Name: ").append(service.getName()).append("\n");
            prompt.append("Price: ").append(service.getPrice()).append("\n");
            prompt.append("Mobile Benefit: ").append(service.getMobileBenefit()).append("\n");
            prompt.append("Card Benefit: ").append(service.getCardBenefit()).append("\n");
            prompt.append("Total Content: ").append(service.getTotalContent()).append("\n");
            prompt.append("Simultaneous Devices: ").append(service.getSimultaneousDevices()).append("\n");
            prompt.append("Ads Include: ").append(service.getAdsInclude()).append("\n");
            prompt.append("Offline Storage: ").append(service.isOfflineStorage()).append("\n");
            prompt.append("Key Benefit: ").append(service.getKeyBenefit()).append("\n");
            prompt.append("Special Benefit: ").append(service.getSpecialBenefit()).append("\n\n");
        }
        prompt.append("서비스 이름은 '넷플릭스', '티빙', '유튜브 프리미엄', '왓챠', '웨이브', '디즈니+', '아마존 프라임 비디오', '애플 TV+', '쿠팡 플레이', '시리즈온' 중 하나야 ").append("\n");
        prompt.append("위 서비스들 중에서 나에게 가장 추천하는 서비스 하나를 알려줘. 가격과 혜택 3가지를 간단히 알려줘. 응답은 아래 형식으로 해줘:").append("\n");
        prompt.append("{ '추천 서비스': '서비스 이름', '가격': '가격', '혜택 1': '혜택 설명', '혜택 2': '혜택 설명', '혜택 3': '혜택 설명' }").append("\n");

        return prompt.toString();
    }

    @Override
    public String generateComparisonPrompt(String excludedService) {
        StringBuilder prompt = new StringBuilder();
        List<OTTService> ottServices = ottServiceRepository.findAll();

        prompt.append("추천된 서비스 '").append(excludedService).append("'를 제외한 OTT 서비스들의 내용이야:\n");
        for (OTTService service : ottServices) {
            if (!service.getName().equals(excludedService)) {
                prompt.append("Service Name: ").append(service.getName()).append("\n");
                prompt.append("Price: ").append(service.getPrice()).append("\n");
                prompt.append("Mobile Benefit: ").append(service.getMobileBenefit()).append("\n");
                prompt.append("Card Benefit: ").append(service.getCardBenefit()).append("\n");
                prompt.append("Total Content: ").append(service.getTotalContent()).append("\n");
                prompt.append("Simultaneous Devices: ").append(service.getSimultaneousDevices()).append("\n");
                prompt.append("Ads Include: ").append(service.getAdsInclude()).append("\n");
                prompt.append("Offline Storage: ").append(service.isOfflineStorage()).append("\n");
                prompt.append("Key Benefit: ").append(service.getKeyBenefit()).append("\n");
                prompt.append("Special Benefit: ").append(service.getSpecialBenefit()).append("\n\n");
            }
        }
        prompt.append("서비스 이름은 '넷플릭스', '티빙', '유튜브 프리미엄', '왓챠', '웨이브', '디즈니+', '아마존 프라임 비디오', '애플 TV+', '쿠팡 플레이', '시리즈온' 중 하나야 ").append("\n");
        prompt.append("추천된 서비스를 제외하고 나머지 서비스들의 가격과 혜택 2가지를 간단하게 알려줘. 응답은 반드시 **JSON 형식**으로 해줘. 아래 형식을 따르고, 절대로 코드 블록이나 추가적인 설명을 넣지 말아줘:\n");
        prompt.append("'추천 서비스 리스트' : [{ '추천 서비스': '서비스 이름', '가격': '가격', '혜택 1': '혜택 설명', '혜택 2': '혜택 설명' },");
        prompt.append("{ '추천 서비스': '서비스 이름', '가격': '가격', '혜택 1': '혜택 설명', '혜택 2': '혜택 설명' }]");

        return prompt.toString();
    }
}
