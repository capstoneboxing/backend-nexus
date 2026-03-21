package com.nexus.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopBoxerAiProfile {

    private Integer rankingPosition;
    private String boxerName;

    private Double heightCm;
    private Double reachCm;
    private Double weightClassAlignment;
    private Double handSpeed;
    private Double footSpeed;
    private Double strength;
    private Double endurance;
    private Double reactionTime;

    private Double punchAccuracy;
    private Double punchVariety;
    private Double defensiveGuardEfficiency;
    private Double headMovement;
    private Double footworkTechnique;
    private Double counterpunchingAbility;
    private Double combinationEfficiency;

    private Double ringIq;
    private Double adaptabilityMidFight;
    private Double distanceControl;
    private Double tempoControl;
    private Double opponentPatternRecognition;
    private Double fightPlanningDiscipline;

    private Double composureUnderPressure;
    private Double aggressionControl;
    private Double mentalToughness;
    private Double focusConsistency;
    private Double resilienceAfterKnockdown;

    private Double winRatio;
    private Double knockoutRatio;
    private Double titleFightExperience;
    private Double strengthOfOpposition;
    private Double recentFightActivity;
    private Double performanceConsistency;

    private String sourceNote;
}