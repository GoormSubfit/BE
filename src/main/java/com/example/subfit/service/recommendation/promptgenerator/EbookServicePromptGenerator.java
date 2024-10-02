package com.example.subfit.service.recommendation.promptgenerator;

import com.example.subfit.entity.service.EbookService;
import com.example.subfit.repository.service.EbookServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbookServicePromptGenerator implements ServicePromptGenerator {

    private final EbookServiceRepository ebookServiceRepository;

    public EbookServicePromptGenerator(EbookServiceRepository ebookServiceRepository) {
        this.ebookServiceRepository = ebookServiceRepository;
    }

    @Override
    public String generatePrompt() {
        StringBuilder prompt = new StringBuilder();
        List<EbookService> ebookServices = ebookServiceRepository.findAll();

        prompt.append("아래 내용이 ebook 서비스들의 내용이야:\n");
        for (EbookService service : ebookServices) {
            prompt.append("Service Name: ").append(service.getName()).append("\n");
            prompt.append("Price: ").append(service.getPrice()).append("\n");
            prompt.append("Mobile Benefit: ").append(service.getMobileBenefit()).append("\n");
            prompt.append("Card Benefit: ").append(service.getCardBenefit()).append("\n");
            prompt.append("Total Books: ").append(service.getTotalBooks()).append("\n");
            prompt.append("Most Genres: ").append(service.getMostGenres()).append("\n");
            prompt.append("Supported Devices: ").append(service.getSupportedDevices()).append("\n");
            prompt.append("Foreign Translation: ").append(service.isForeignTranslation()).append("\n");
            prompt.append("Offline Storage: ").append(service.isOfflineStorage()).append("\n");
            prompt.append("Special Benefit: ").append(service.getSpecialBenefit()).append("\n\n");
        }
        prompt.append("서비스 이름은 '밀리의 서재', '리디북스', '스크립드', '교보문고 sam', '오디언' '예스24 ebook' 중 하나야 ").append("\n");
        prompt.append("위 서비스들 중에서 나에게 가장 추천하는 서비스 하나를 알려줘. 가격과 혜택 3가지를 간단히 알려줘. 응답은 아래 형식으로 해줘:").append("\n");
        prompt.append("{ '추천 서비스': '서비스 이름', '가격': '가격', '혜택 1': '혜택 설명', '혜택 2': '혜택 설명', '혜택 3': '혜택 설명' }").append("\n");

        return prompt.toString();
    }

    @Override
    public String generateComparisonPrompt(String excludedService) {
        StringBuilder prompt = new StringBuilder();
        List<EbookService> ebookServices = ebookServiceRepository.findAll();

        prompt.append("추천된 서비스 '").append(excludedService).append("'를 제외한 ebook 서비스들의 내용이야:\n");
        for (EbookService service : ebookServices) {
            if (!service.getName().equals(excludedService)) {
                prompt.append("Service Name: ").append(service.getName()).append("\n");
                prompt.append("Price: ").append(service.getPrice()).append("\n");
                prompt.append("Mobile Benefit: ").append(service.getMobileBenefit()).append("\n");
                prompt.append("Card Benefit: ").append(service.getCardBenefit()).append("\n");
                prompt.append("Total Books: ").append(service.getTotalBooks()).append("\n");
                prompt.append("Most Genres: ").append(service.getMostGenres()).append("\n");
                prompt.append("Supported Devices: ").append(service.getSupportedDevices()).append("\n");
                prompt.append("Foreign Translation: ").append(service.isForeignTranslation()).append("\n");
                prompt.append("Offline Storage: ").append(service.isOfflineStorage()).append("\n");
                prompt.append("Special Benefit: ").append(service.getSpecialBenefit()).append("\n\n");
            }
        }
        prompt.append("서비스 이름은 '밀리의 서재', '리디북스', '스크립드', '교보문고 sam', '오디언' '예스24 ebook' 중 하나야 ").append("\n");
        prompt.append("추천된 서비스를 제외하고 나머지 서비스들의 가격과 혜택 2가지를 간단하게 알려줘. 응답은 반드시 **JSON 형식**으로 해줘. 아래 형식을 따르고, 절대로 코드 블록이나 추가적인 설명을 넣지 말아줘:\n");
        prompt.append("'추천 서비스 리스트' : [{ '추천 서비스': '서비스 이름', '가격': '가격', '혜택 1': '혜택 설명', '혜택 2': '혜택 설명' },");
        prompt.append("{ '추천 서비스': '서비스 이름', '가격': '가격', '혜택 1': '혜택 설명', '혜택 2': '혜택 설명' }]");

        return prompt.toString();
    }
}

