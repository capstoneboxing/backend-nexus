package com.nexus.service.ai;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.nexus.dto.ai.SingleBoxerAiResponse;
import com.nexus.util.AppUtils;
import org.springframework.stereotype.Service;

@Service
public class SingleBoxerAiService {

    private static final String BASE_INSTRUCTIONS = """
You are a boxing analytics evaluator.

Return ONLY valid JSON.
Do not return markdown.
Do not return explanations outside the JSON.
Do not wrap the JSON in code fences.
Do not invent extra fields.
Field names must match the JSON schema exactly.
""";

    private static final String SHORT_RUBRIC = """
Scoring rules:
- Return exactly one boxer profile.
- Score relative to the %s division.
- Use realistic and conservative boxing judgment when assigning scores.

Measurement rules:
- heightCm and reachCm must be raw measurements in centimeters.
- Prefer commonly reported open/public sources such as BoxRec, Wikipedia, ESPN, The Ring, and BoxingScene.
- If exact measurements are uncertain, use the best-known commonly reported estimate.

Record rules:
- winRatio and knockoutRatio must be raw decimal values from 0.0 to 1.0.
- Prefer values consistent with commonly reported professional records from open/public sources.
- winRatio = wins / total fights.
- knockoutRatio = knockout wins / total wins.
- If exact record details are uncertain, use the best-known commonly reported estimate.

Scoring scale for all other attributes:
- 1 to 2 = poor
- 3 to 4 = below average
- 5 = average pro
- 6 = above average
- 7 = strong
- 8 = elite
- 9 = world-class
- 10 = all-time great

Attribute groups:
- Physical = size, athletic traits, speed, strength, endurance, and reactions
- Technical = execution of punches, defense, footwork, counters, and combinations
- Tactical = decision-making, adjustment, distance control, tempo control, opponent reading, and game-plan execution
- Psychological = composure, aggression control, toughness, focus, and recovery from adversity
- Performance = record efficiency, title experience, opposition quality, activity level, and consistency

Confidence rules:
- confidence must be from 0.0 to 1.0.
- If unsure, lower confidence.
- If the boxer name is nonsense, ambiguous, fictional, or not credibly identifiable for this weight-class context, set boxerFound to false.
""";

    private static final String JSON_SCHEMA = """
{
  "boxerFound": true,
  "confidence": 0.0,
  "matchReason": "",
  "boxer": {
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
}
""";

    private final AiService aiService;
    private final JsonMapper jsonMapper;

    public SingleBoxerAiService(AiService aiService, JsonMapper jsonMapper) {
        this.aiService = aiService;
        this.jsonMapper = jsonMapper;
    }

    public SingleBoxerAiResponse getBoxerProfile(String boxerName, String weightClassName) {
        String prompt = buildPrompt(boxerName, weightClassName);
        String response = aiService.chat(prompt);

        try {
            String json = AppUtils.extractJson(response);
            return jsonMapper.readValue(json, SingleBoxerAiResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response: " + response, e);
        }
    }

    private String buildPrompt(String boxerName, String weightClassName) {
        return """
%s

Task:
Try to identify the boxer named "%s" in the context of the %s division and generate a boxing analytics profile.

Decision rules:
- If the boxer is not credibly identifiable, return:
  - "boxerFound": false
  - "confidence": below 0.75
  - "matchReason": short explanation
  - "boxer": null

- If the boxer is credibly identifiable, return:
  - "boxerFound": true
  - "confidence": from 0.75 to 1.0
  - "matchReason": short explanation of who the boxer is and why the match is credible
  - "boxer": full profile

Profile rules:
- boxerName must match the identified boxer.
- Use realistic boxing knowledge for subjective scoring.
- sourceNote should briefly mention the basis of the profile, including the kind of public/open sources relied on where relevant.

%s

JSON format:
%s
""".formatted(
                BASE_INSTRUCTIONS,
                boxerName,
                weightClassName,
                SHORT_RUBRIC.formatted(weightClassName),
                JSON_SCHEMA
        );
    }
}