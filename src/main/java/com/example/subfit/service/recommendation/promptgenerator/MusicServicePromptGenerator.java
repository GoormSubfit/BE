package com.example.subfit.service.recommendation.promptgenerator;

import com.example.subfit.entity.service.MusicService;
import com.example.subfit.repository.service.MusicServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicServicePromptGenerator implements ServicePromptGenerator {

    private final MusicServiceRepository musicServiceRepository;

    public MusicServicePromptGenerator(MusicServiceRepository musicServiceRepository) {
        this.musicServiceRepository = musicServiceRepository;
    }

    @Override
    public String generatePrompt() {
        StringBuilder prompt = new StringBuilder();
        List<MusicService> musicServices = musicServiceRepository.findAll();

        prompt.append("아래 내용이 음악 앱 서비스들의 내용이야:\n");
        for (MusicService service : musicServices) {
            prompt.append("Service Name: ").append(service.getName()).append("\n");
            prompt.append("Price: ").append(service.getPrice()).append("\n");
            prompt.append("Mobile Benefit: ").append(service.getMobileBenefit()).append("\n");
            prompt.append("Card Benefit: ").append(service.getCardBenefit()).append("\n");
            prompt.append("Total Songs: ").append(service.getTotalSong()).append("\n");
            prompt.append("Account Sharing: ").append(service.isAccountSharing()).append("\n");
            prompt.append("Recommendation Algorithm: ").append(service.isRecommendationAlgorithm()).append("\n");
            prompt.append("Audio Quality: ").append(service.getAudioQuality()).append("\n");
            prompt.append("Audiobook Included: ").append(service.isAudiobookIncluded()).append("\n");
            prompt.append("Offline Storage: ").append(service.isOfflineStorage()).append("\n");
            prompt.append("Special Benefit: ").append(service.getSpecialBenefit()).append("\n\n");
        }
        prompt.append("서비스 이름은 '스포티파이', '멜론', '애플 뮤직', '유튜브 뮤직', '벅스', '플로', '지니뮤직', '네이버 Vibe', '유튜브 프리미엄' 중 하나야 ").append("\n");
        prompt.append("위 서비스들 중에서 나에게 가장 추천하는 서비스 하나를 알려줘. 가격과 혜택 3가지를 간단히 알려줘. 응답은 아래 형식으로 해줘:").append("\n");
        prompt.append("{ '추천 서비스': '서비스 이름', '가격': '가격', '혜택 1': '혜택 설명', '혜택 2': '혜택 설명', '혜택 3': '혜택 설명' }").append("\n");

        return prompt.toString();
    }

    @Override
    public String generateComparisonPrompt(String excludedService) {
        StringBuilder prompt = new StringBuilder();
        List<MusicService> musicServices = musicServiceRepository.findAll();

        prompt.append("추천된 서비스 '").append(excludedService).append("'를 제외한 음악 앱 서비스들의 내용이야:\n");
        for (MusicService service : musicServices) {
            if (!service.getName().equals(excludedService)) {
                prompt.append("Service Name: ").append(service.getName()).append("\n");
                prompt.append("Price: ").append(service.getPrice()).append("\n");
                prompt.append("Mobile Benefit: ").append(service.getMobileBenefit()).append("\n");
                prompt.append("Card Benefit: ").append(service.getCardBenefit()).append("\n");
                prompt.append("Total Songs: ").append(service.getTotalSong()).append("\n");
                prompt.append("Account Sharing: ").append(service.isAccountSharing()).append("\n");
                prompt.append("Recommendation Algorithm: ").append(service.isRecommendationAlgorithm()).append("\n");
                prompt.append("Audio Quality: ").append(service.getAudioQuality()).append("\n");
                prompt.append("Audiobook Included: ").append(service.isAudiobookIncluded()).append("\n");
                prompt.append("Offline Storage: ").append(service.isOfflineStorage()).append("\n");
                prompt.append("Special Benefit: ").append(service.getSpecialBenefit()).append("\n\n");
            }
        }
        // JSON 형식에 대한 명확한 지시
        prompt.append("서비스 이름은 '스포티파이', '멜론', '애플 뮤직', '유튜브 뮤직', '벅스', '플로', '지니뮤직', '네이버 Vibe', '유튜브 프리미엄' 중 하나야.\n");
        prompt.append("추천된 서비스를 제외하고 나머지 서비스들의 가격과 혜택 2가지를 간단하게 알려줘. 응답은 반드시 **JSON 형식**으로 해줘. 아래 형식을 따르고, 절대로 코드 블록이나 추가적인 설명을 넣지 말아줘:\n");
        prompt.append("'추천 서비스 리스트' : [{ '추천 서비스': '서비스 이름', '가격': '가격', '혜택 1': '혜택 설명', '혜택 2': '혜택 설명' },");
        prompt.append("{ '추천 서비스': '서비스 이름', '가격': '가격', '혜택 1': '혜택 설명', '혜택 2': '혜택 설명' }]");

        return prompt.toString();
    }
}
