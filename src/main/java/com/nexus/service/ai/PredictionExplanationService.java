package com.nexus.service.ai;

import com.nexus.dto.prediction.BoxerInput;
import com.nexus.dto.prediction.CategoryScores;
import com.nexus.model.CategoryWeight;
import org.springframework.stereotype.Service;

@Service
public class PredictionExplanationService {

    private final AiService aiService;

    public PredictionExplanationService(AiService aiService) {
        this.aiService = aiService;
    }

    public String generateExplanation(
            String weightClassName,
            String predictedWinner,
            BoxerInput boxerA,
            BoxerInput boxerB,
            CategoryScores scoresA,
            CategoryScores scoresB,
            double baseClosenessA,
            double baseClosenessB,
            double adjustedClosenessA,
            double adjustedClosenessB,
            double probabilityA,
            double probabilityB,
            CategoryWeight weights
    ) {
        String prompt = """
You are explaining a boxing prediction result for a backend boxing analytics system.

Weight class: %s
Predicted winner: %s

Category weights:
- Physical: %.2f
- Technical: %.2f
- Tactical: %.2f
- Psychological: %.2f
- Experience: %.2f

Boxer A: %s
Boxer A category scores:
- Physical: %.4f
- Technical: %.4f
- Tactical: %.4f
- Psychological: %.4f
- Experience: %.4f
Boxer A base closeness to perfect boxer: %.4f
Boxer A attribute confidence: %.4f
Boxer A adjusted closeness used for prediction: %.4f
Boxer A win probability: %.4f

Boxer B: %s
Boxer B category scores:
- Physical: %.4f
- Technical: %.4f
- Tactical: %.4f
- Psychological: %.4f
- Experience: %.4f
Boxer B base closeness to perfect boxer: %.4f
Boxer B attribute confidence: %.4f
Boxer B adjusted closeness used for prediction: %.4f
Boxer B win probability: %.4f

Instructions:
- Write a concise explanation in plain English.
- Explain which categories most likely gave the predicted winner the edge.
- If relevant, mention whether attribute confidence strengthened or reduced certainty.
- Mention if the result is narrow or clear.
- Do not invent statistics or facts not provided.
- Do not mention raw attribute values.
- Keep it under 120 words.
"""
                .formatted(
                        weightClassName,
                        predictedWinner,

                        weights.getPhysicalWeight(),
                        weights.getTechnicalWeight(),
                        weights.getTacticalWeight(),
                        weights.getPsychologicalWeight(),
                        weights.getExperienceWeight(),

                        boxerA.boxerName(),
                        scoresA.physical(),
                        scoresA.technical(),
                        scoresA.tactical(),
                        scoresA.psychological(),
                        scoresA.experience(),
                        baseClosenessA,
                        boxerA.attributeConfidence(),
                        adjustedClosenessA,
                        probabilityA,

                        boxerB.boxerName(),
                        scoresB.physical(),
                        scoresB.technical(),
                        scoresB.tactical(),
                        scoresB.psychological(),
                        scoresB.experience(),
                        baseClosenessB,
                        boxerB.attributeConfidence(),
                        adjustedClosenessB,
                        probabilityB
                );

        return aiService.chat(prompt);
    }
}