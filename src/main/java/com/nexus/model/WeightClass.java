package com.nexus.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("weight_class")
public class WeightClass {

    @Id
    @Column("weight_class_id")
    private Integer weightClassId;

    @Column("class_name")
    private String className;

    @Column("max_weight_lb")
    private Double maxWeightLb;

    @Column("min_weight_lb")
    private Double minWeightLb;

}
