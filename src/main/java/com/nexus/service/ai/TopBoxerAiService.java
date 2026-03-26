package com.nexus.service.ai;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.nexus.dto.ai.TopBoxerAiResponse;
import com.nexus.util.AppUtils;
import org.springframework.stereotype.Service;

@Service
public class TopBoxerAiService {

    private static final boolean USE_FULL_RUBRIC = false;

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
Core rules:
- Return exactly %d boxers ranked 1 to %d.
- Score relative to the %s division.
- Be consistent across all %d fighters.
- Avoid giving too many 10s.
- Use realistic boxing knowledge.

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

    private static final String FULL_RUBRIC = """
Core rules:
- Return exactly %d boxers.
- rankingPosition must be 1 through %d.
- Use historically defensible all-time rankings for this weight class.
- Score each boxer relative to the standards of the %s division.
- Be internally consistent across all %d fighters.
- Avoid giving too many 10.0 scores unless truly justified.
- Use decimals if needed, for example 7.5.

Measurement rules:
- heightCm and reachCm are raw physical measurements in centimeters, not rubric scores.
- Prefer commonly reported open/public sources such as BoxRec, Wikipedia, ESPN, The Ring, and BoxingScene.
- If exact measurements are uncertain, use the best-known commonly reported estimate.

Record rules:
- winRatio and knockoutRatio are raw decimal ratios from 0.0 to 1.0, not rubric scores.
- Prefer values consistent with commonly reported professional records from open/public sources.
- winRatio = wins / total fights.
- knockoutRatio = knockout wins / total wins.
- If exact record details are uncertain, use the best-known commonly reported estimate.

Scoring standard:
- 1 to 2 = very poor
- 3 to 4 = below average
- 5 = average professional level
- 6 = above average
- 7 = strong / clearly good
- 8 = elite
- 9 = exceptional / world-class
- 10 = near-ideal for the division / all-time level

Important scoring rules:
- titleFightExperience, strengthOfOpposition, recentFightActivity, and performanceConsistency are scored from 1.0 to 10.0 using the rubric below.

Physical attribute rubrics:
weightClassAlignment:
- 1 = clearly undersized or drained; body type badly mismatched
- 3 = noticeable disadvantage in size/frame or poor weight cut fit
- 5 = acceptable fit for division
- 7 = naturally strong fit; good frame and performance at the weight
- 8 = very well suited physically to the division
- 10 = ideal size/build for that division without sacrificing speed or stamina

handSpeed:
- 1 = very slow hands, easy to read
- 3 = below-average speed
- 5 = average pro hand speed
- 7 = clearly fast hands, wins many exchanges by quickness
- 8 = elite hand speed
- 10 = exceptional, all-time great hand speed for the division

footSpeed:
- 1 = very slow movement
- 3 = heavy-footed, struggles to reposition
- 5 = average mobility
- 7 = clearly quick on feet, good movement around ring
- 8 = elite movement speed
- 10 = extraordinary mobility and repositioning speed

strength:
- 1 = physically weak for division
- 3 = below average strength
- 5 = average pro strength
- 7 = clearly strong, can bully opponents at times
- 8 = elite strength for division
- 10 = exceptional functional strength, dominant physically

endurance:
- 1 = fades very early
- 3 = noticeable drop-off after a few rounds
- 5 = average stamina over full fight
- 7 = strong engine, remains effective late
- 8 = elite endurance
- 10 = near tireless; keeps pace and sharpness deep into fight

reactionTime:
- 1 = slow reactions, often late
- 3 = below average reactions
- 5 = average reaction speed
- 7 = sharp reactions, catches cues well
- 8 = elite reflex responsiveness
- 10 = extraordinary reflexes and real-time responsiveness

Technical attribute rubrics:
punchAccuracy:
- 1 = wild, rarely lands clean
- 3 = below-average accuracy
- 5 = average pro accuracy
- 7 = accurate and efficient punch placement
- 8 = elite accuracy
- 10 = exceptional precision and consistent clean landing

punchVariety:
- 1 = very limited arsenal
- 3 = small number of reliable punches
- 5 = decent mix of basic punches
- 7 = good variety used effectively
- 8 = elite versatility in punch selection
- 10 = complete arsenal used fluidly and intelligently

defensiveGuardEfficiency:
- 1 = guard often ineffective
- 3 = inconsistent guard
- 5 = average guard defense
- 7 = strong guard, blocks many shots well
- 8 = elite guard discipline and efficiency
- 10 = exceptionally difficult to penetrate through guard

headMovement:
- 1 = almost no useful head movement
- 3 = limited and inconsistent
- 5 = average defensive movement
- 7 = effective and regular head movement
- 8 = elite evasive upper-body movement
- 10 = exceptional slip/roll ability; very elusive

footworkTechnique:
- 1 = poor balance and movement mechanics
- 3 = sloppy or inefficient footwork
- 5 = average technical footwork
- 7 = sound and effective footwork
- 8 = elite footwork technique
- 10 = masterful control of angles, balance, and positioning

counterpunchingAbility:
- 1 = rarely counters effectively
- 3 = limited counter ability
- 5 = average counter timing
- 7 = strong counterpuncher
- 8 = elite counterpunching
- 10 = exceptional at reading and punishing mistakes

combinationEfficiency:
- 1 = poor combinations, disconnected attacks
- 3 = basic combinations with low efficiency
- 5 = average combination work
- 7 = good, effective combinations
- 8 = elite punch sequencing
- 10 = highly efficient, fluid, damaging combinations

Tactical attribute rubrics:
ringIq:
- 1 = poor decisions, easily outthought
- 3 = limited tactical awareness
- 5 = average boxing intelligence
- 7 = smart fighter, good decisions
- 8 = elite ring intelligence
- 10 = exceptional tactical thinker, controls fight intellectually

adaptabilityMidFight:
- 1 = cannot adjust
- 3 = slow/poor adjustments
- 5 = average ability to adjust
- 7 = makes useful adjustments during fight
- 8 = elite adaptability
- 10 = outstanding real-time adjustment ability

distanceControl:
- 1 = constantly at wrong range
- 3 = weak control of distance
- 5 = average range control
- 7 = good control of engagement distance
- 8 = elite range management
- 10 = masterful control of range throughout fight

tempoControl:
- 1 = gets forced into opponent’s pace
- 3 = struggles to control rhythm
- 5 = average pace management
- 7 = often controls tempo well
- 8 = elite pace/rhythm control
- 10 = dictates tempo almost completely

opponentPatternRecognition:
- 1 = poor at noticing patterns
- 3 = limited recognition
- 5 = average read of opponent
- 7 = detects habits and exploits them
- 8 = elite pattern recognition
- 10 = exceptional at identifying and exploiting tendencies

fightPlanningDiscipline:
- 1 = abandons plan easily
- 3 = inconsistent discipline
- 5 = average game-plan discipline
- 7 = generally follows strategy well
- 8 = elite discipline in execution
- 10 = exceptional commitment to tactical plan without losing flexibility

Psychological attribute rubrics:
composureUnderPressure:
- 1 = panics badly under pressure
- 3 = often rattled
- 5 = average composure
- 7 = stays calm in difficult moments
- 8 = elite composure
- 10 = exceptionally calm under sustained pressure

aggressionControl:
- 1 = wild, reckless aggression
- 3 = often loses control
- 5 = average control
- 7 = controlled aggression, presses smartly
- 8 = elite balance of pressure and discipline
- 10 = perfect blend of assertiveness and restraint

mentalToughness:
- 1 = breaks mentally under adversity
- 3 = weak under hardship
- 5 = average toughness
- 7 = resilient and determined
- 8 = elite toughness
- 10 = extraordinary grit and refusal to fold

focusConsistency:
- 1 = frequently loses focus
- 3 = inconsistent concentration
- 5 = average focus
- 7 = strong concentration through most of fight
- 8 = elite sustained focus
- 10 = unwavering concentration from start to finish

resilienceAfterKnockdown:
- 1 = rarely recovers well
- 3 = often badly affected after knockdown
- 5 = average recovery
- 7 = usually regains control reasonably well
- 8 = elite recovery composure
- 10 = exceptional ability to recover and re-enter fight strongly

Experience and performance rubrics:
titleFightExperience:
- 1 = none
- 3 = very limited exposure
- 5 = some title-level experience
- 7 = solid title-fight background
- 8 = extensive high-level title experience
- 10 = exceptional championship-level experience

strengthOfOpposition:
- 1 = very weak opposition
- 3 = mostly limited opponents
- 5 = average quality opposition
- 7 = regularly fought strong opponents
- 8 = elite level opposition quality
- 10 = all-time great resume strength

recentFightActivity:
- 1 = very inactive
- 3 = below ideal activity
- 5 = acceptable activity level
- 7 = active and sharp
- 8 = very active at strong level
- 10 = ideal recent activity without overuse

performanceConsistency:
- 1 = highly erratic performances
- 3 = often inconsistent
- 5 = average consistency
- 7 = usually performs to level
- 8 = elite reliability
- 10 = exceptionally consistent high-level performance

sourceNote:
- Briefly justify both selection and scoring basis in 1 to 3 sentences.
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

    private static final String SCORING_RUBRIC = USE_FULL_RUBRIC ? FULL_RUBRIC : SHORT_RUBRIC;

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
        String rubric = USE_FULL_RUBRIC
                ? FULL_RUBRIC.formatted(amount, amount, weightClassName, amount)
                : SHORT_RUBRIC.formatted(amount, amount, weightClassName, amount);

        return """
%s

Task:
Select the top %d all-time boxers in the %s division.
For each boxer, return fields that match the boxing analytics schema exactly.

Additional rules:
- Return exactly %d boxers.
- rankingPosition must be 1 through %d.

%s

JSON format:
%s
""".formatted(
                BASE_INSTRUCTIONS,
                amount,
                weightClassName,
                amount,
                amount,
                rubric,
                JSON_SCHEMA
        );
    }
}