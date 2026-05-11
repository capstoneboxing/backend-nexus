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

    private static final String RUBRIC = """
Scoring rules:
- Return exactly one boxer profile.
- Score relative to the %s division.
- Use realistic and conservative boxing judgment when assigning scores.
- If some scoring details are uncertain, still estimate conservatively.

Identification rules:
- The boxer does NOT need to be primarily associated with the requested division.
- If the boxer has fought in, near, or can reasonably be evaluated for this division, still return boxerFound = true.
- Do not reject a real boxer just because they moved divisions.
- Do not reject a real boxer just because some information is incomplete.
- Only return boxerFound = false when the name cannot be credibly matched to a real professional boxer.

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
- 1 to 2 = very poor
- 3 to 4 = below average
- 5 = average professional level
- 6 = above average
- 7 = strong / clearly good
- 8 = elite
- 9 = exceptional / world-class
- 10 = near-ideal for the division / all-time level

Attribute groups:
- Physical = size, athletic traits, speed, strength, endurance, and reactions
- Technical = execution of punches, defense, footwork, counters, and combinations
- Tactical = decision-making, adjustment, distance control, tempo control, opponent reading, and game-plan execution
- Psychological = composure, aggression control, toughness, focus, and recovery from adversity
- Performance = record efficiency, title experience, opposition quality, activity level, and consistency

Confidence rules:
- confidence must be from 0.0 to 1.0.
- confidence represents how certain you are that the identified boxer is correct.
- Lower confidence if there is ambiguity or uncertainty.
- boxerFound can still be true with moderate confidence, such as 0.5 to 0.7.
- boxerFound should only be false if the boxer cannot be credibly identified at all.
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
Identify the boxer named "%s" and generate a boxing analytics profile for the %s division.

Decision rules:
- First determine whether the name can be credibly matched to a real professional boxer.
- Do not make the requested weight class the main reason for rejecting the boxer.
- A real boxer can still be evaluated even if they are not primarily known for this division.
- If the boxer has fought in, near, or can reasonably be compared within the %s division, generate the profile.
- If the boxer is real but some details are incomplete, still return boxerFound = true and estimate conservatively.

- If the boxer is clearly real and identifiable:
  - "boxerFound": true
  - "confidence": a value from 0.0 to 1.0 representing how confident you are in the identification
  - "matchReason": short explanation of who the boxer is and why the match is credible
  - "boxer": full profile

- If the boxer cannot be identified at all, such as a nonsense name, fictional person, or no credible professional boxer match:
  - "boxerFound": false
  - "confidence": a value from 0.0 to 1.0 representing uncertainty
  - "matchReason": short explanation
  - "boxer": null

Important:
- Do NOT force confidence into any range based on boxerFound.
- A boxer can be found with moderate confidence.
- Moderate confidence should still return boxerFound = true.
- Only return boxerFound = false if the boxer truly cannot be identified.
- Do not reject a boxer only because BoxRec/Wikipedia-style details may be incomplete.
- Do not reject a boxer only because exact height, reach, or record data is uncertain.

Profile rules:
- boxerName must match the identified boxer.
- Use realistic boxing knowledge for subjective scoring.
- Use conservative estimates when exact values are uncertain.
- sourceNote should briefly mention the basis of the profile, including public/open-source boxing information where relevant.

%s

JSON format:
%s
""".formatted(
                BASE_INSTRUCTIONS,
                boxerName,
                weightClassName,
                weightClassName,
                RUBRIC.formatted(weightClassName),
                JSON_SCHEMA
        );
    }
}