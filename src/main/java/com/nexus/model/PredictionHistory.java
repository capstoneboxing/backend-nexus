package com.nexus.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("prediction_history")
public class PredictionHistory {

    @Id
    @Column("prediction_id")
    private Integer predictionId;

    @Column("boxer_a_name")
    private String boxerAName;

    @Column("boxer_b_name")
    private String boxerBName;

    @Column("predicted_winner")
    private String predictedWinner;

    @Column("match_winner")
    private String matchWinner;

    @Column("match_win_method")
    private String matchWinMethod;

    @Column("weight_class_id")
    private Integer weightClassId;

    @Column("boxer_a_closeness_score")
    private Double boxerAClosenessScore;

    @Column("boxer_b_closeness_score")
    private Double boxerBClosenessScore;

    @Column("probability_a")
    private Double probabilityA;

    @Column("probability_b")
    private Double probabilityB;

    @Column("breakdown_snapshot")
    private JsonNode breakdownSnapshot;

    @Column("prediction_date")
    private OffsetDateTime predictionDate;

}
