package com.example.subfit.service.recommendation.promptgenerator;

public interface ServicePromptGenerator {
    String generatePrompt();
    String generateComparisonPrompt(String excludedService);
}
