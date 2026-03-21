package com.nexus.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
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

    @Column("match_decision")
    private String matchDecision;

    @Column("weight_class_id")
    private Integer weightClassId;

    @Column("probability_a")
    private Double probabilityA;

    @Column("probability_b")
    private Double probabilityB;

    @Column("breakdown_snapshot")
    private String breakdownSnapshot;

    @ReadOnlyProperty
    @Column("prediction_date")
    private OffsetDateTime predictionDate;

}
