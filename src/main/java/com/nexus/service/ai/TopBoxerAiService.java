package com.nexus.service.ai;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.nexus.dto.ai.TopBoxerAiResponse;
import com.nexus.util.AppUtils;
import org.springframework.stereotype.Service;

@Service
public class TopBoxerAiService {

    private static final String BASE_INSTRUCTIONS = """
You are a boxing analytics evaluator.

Return ONLY valid JSON.
Do not return markdown.
Do not return explanations outside the JSON.
Do not wrap the JSON in code fences.
Do not invent extra fields.
Field names must match the JSON schema exactly.
""";

    private static final String RUBRIC = """
Core rules:
- Return exactly %d boxers ranked 1 to %d.
- Score relative to the %s division.
- Be consistent across all %d fighters.
- Use realistic boxing knowledge.
- If exact historical rankings are debated, use mainstream boxing consensus.

Measurement rules:
- heightCm and reachCm are raw physical measurements in centimeters, not rubric scores.
- Prefer commonly reported open/public sources such as BoxRec, Wikipedia, ESPN, The Ring, and BoxingScene.
- If exact measurements are uncertain, use the best-known commonly reported estimate.

Record rules:
- winRatio and knockoutRatio must be raw decimal values from 0.0 to 1.0.
- Prefer values consistent with commonly reported professional records from open/public sources.
- winRatio = wins / total fights.
- knockoutRatio = knockout wins / total wins.
- If exact record details are uncertain, use the best-known commonly reported estimate.

Scoring scale for all other attributes. Use decimals when needed (e.g., 7.5):
- 1 to 2 = poor
- 3 to 4 = below average
- 5 = average professional level
- 6 = above average
- 7 = strong
- 8 = elite
- 9 = exceptional / world-class
- 10 = near-ideal for the division / all-time level

Attribute groups:
- Physical = size, athletic traits, speed, strength, endurance, and reactions
- Technical = execution of punches, defense, footwork, counters, and combinations
- Tactical = decision-making, adjustment, distance control, tempo control, opponent reading, and game-plan execution
- Psychological = composure, aggression control, toughness, focus, and recovery from adversity
- Performance = record efficiency, title experience, opposition quality, activity level, and consistency

sourceNote:
- Briefly mention ranking and scoring basis.
""";

    private static final String JSON_SCHEMA = """
{
  "boxers": [
    {
      "rankingPosition": 1,
      "boxerName": "",
      "heightCm": 0,
      "reachCm": 0,
      "weightClassAlignment": 0,
      "handSpeed": 0,
      "footSpeed": 0,
      "strength": 0,
      "endurance": 0,
      "reactionTime": 0,
      "punchAccuracy": 0,
      "punchVariety": 0,
      "defensiveGuardEfficiency": 0,
      "headMovement": 0,
      "footworkTechnique": 0,
      "counterpunchingAbility": 0,
      "combinationEfficiency": 0,
      "ringIq": 0,
      "adaptabilityMidFight": 0,
      "distanceControl": 0,
      "tempoControl": 0,
      "opponentPatternRecognition": 0,
      "fightPlanningDiscipline": 0,
      "composureUnderPressure": 0,
      "aggressionControl": 0,
      "mentalToughness": 0,
      "focusConsistency": 0,
      "resilienceAfterKnockdown": 0,
      "winRatio": 0,
      "knockoutRatio": 0,
      "titleFightExperience": 0,
      "strengthOfOpposition": 0,
      "recentFightActivity": 0,
      "performanceConsistency": 0,
      "sourceNote": ""
    }
  ]
}
""";

    private final AiService aiService;
    private final JsonMapper jsonMapper;

    public TopBoxerAiService(AiService aiService, JsonMapper jsonMapper) {
        this.aiService = aiService;
        this.jsonMapper = jsonMapper;
    }

    public TopBoxerAiResponse getTopBoxersForWeightClass(String weightClassName, Integer amount) {
        String prompt = buildPrompt(weightClassName, amount);
        String response = aiService.chat(prompt);

        try {
            String json = AppUtils.extractJson(response);
            return jsonMapper.readValue(json, TopBoxerAiResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response: " + response, e);
        }
    }

    private String buildPrompt(String weightClassName, Integer amount) {
        return """
%s

Task:
Select the top %d all-time professional boxers for the %s division.

Ranking rules:
- Return exactly %d boxers.
- rankingPosition must be consecutive integers from 1 through %d.
- Do not skip, repeat, or duplicate ranking positions.
- Do not include the same boxer more than once.
- Rank based mainly on achievements in or near the %s division.
- A boxer does not need to have spent their entire career in this division.
- If a boxer competed across multiple divisions, include them if their resume is strongly relevant to %s.
- Prefer historically recognized all-time greats for this division, not just current/recent fighters.

Data rules:
- Do not reject or omit a boxer only because height, reach, or exact record details are uncertain.
- Use best-known commonly reported estimates when exact values are uncertain.
- Use conservative scoring for uncertain attributes.
- All boxers must still receive a complete analytics profile.
- sourceNote should briefly explain why the boxer belongs in the ranking.

%s

JSON format:
%s
""".formatted(
                BASE_INSTRUCTIONS,
                amount,
                weightClassName,
                amount,
                amount,
                weightClassName,
                weightClassName,
                RUBRIC.formatted(amount, amount, weightClassName, amount),
                JSON_SCHEMA
        );
    }
}