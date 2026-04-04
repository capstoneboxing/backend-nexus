package com.nexus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("category_weight")
public class CategoryWeight {

    @Id
    @Column("weight_class_id")
    private Integer weightClassId;

    @Column("physical_weight")
    private Double physicalWeight;

    @Column("technical_weight")
    private Double technicalWeight;

    @Column("tactical_weight")
    private Double tacticalWeight;

    @Column("psychological_weight")
    private Double psychologicalWeight;

    @Column("experience_weight")
    private Double experienceWeight;

}
