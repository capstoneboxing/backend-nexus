package com.nexus.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("all_time_ranked_boxer")
public class AllTimeRankedBoxer {

    @Id
    @Column("ranked_boxer_id")
    private Integer rankedBoxerId;

    @Column("batch_id")
    private Integer batchId;

    @Column("weight_class_id")
    private Integer weightClassId;

    @Column("boxer_name")
    private String boxerName;

    @Column("ranking_position")
    private Integer rankingPosition;

    // Physical
    @Column("height_cm")
    private Double heightCm;

    @Column("reach_cm")
    private Double reachCm;

    @Column("weight_class_alignment")
    private Double weightClassAlignment;

    @Column("hand_speed")
    private Double handSpeed;

    @Column("foot_speed")
    private Double footSpeed;

    @Column("strength")
    private Double strength;

    @Column("endurance")
    private Double endurance;

    @Column("reaction_time")
    private Double reactionTime;

    // Technical
    @Column("punch_accuracy")
    private Double punchAccuracy;

    @Column("punch_variety")
    private Double punchVariety;

    @Column("defensive_guard_efficiency")
    private Double defensiveGuardEfficiency;

    @Column("head_movement")
    private Double headMovement;

    @Column("footwork_technique")
    private Double footworkTechnique;

    @Column("counterpunching_ability")
    private Double counterpunchingAbility;

    @Column("combination_efficiency")
    private Double combinationEfficiency;

    // Tactical
    @Column("ring_iq")
    private Double ringIq;

    @Column("adaptability_mid_fight")
    private Double adaptabilityMidFight;

    @Column("distance_control")
    private Double distanceControl;

    @Column("tempo_control")
    private Double tempoControl;

    @Column("opponent_pattern_recognition")
    private Double opponentPatternRecognition;

    @Column("fight_planning_discipline")
    private Double fightPlanningDiscipline;

    // Psychological
    @Column("composure_under_pressure")
    private Double composureUnderPressure;

    @Column("aggression_control")
    private Double aggressionControl;

    @Column("mental_toughness")
    private Double mentalToughness;

    @Column("focus_consistency")
    private Double focusConsistency;

    @Column("resilience_after_knockdown")
    private Double resilienceAfterKnockdown;

    // Performance
    @Column("win_ratio")
    private Double winRatio;

    @Column("knockout_ratio")
    private Double knockoutRatio;

    @Column("title_fight_experience")
    private Double titleFightExperience;

    @Column("strength_of_opposition")
    private Double strengthOfOpposition;

    @Column("recent_fight_activity")
    private Double recentFightActivity;

    @Column("performance_consistency")
    private Double performanceConsistency;

    @Column("source_note")
    private String sourceNote;

    @ReadOnlyProperty
    @Column("generated_at")
    private OffsetDateTime generatedAt;
}