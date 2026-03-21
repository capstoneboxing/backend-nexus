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
@Table("perfect_boxer_generation_batch")
public class PerfectBoxerGenerationBatch {

    @Id
    @Column("batch_id")
    private Integer batchId;

    @Column("weight_class_id")
    private Integer weightClassId;

    @ReadOnlyProperty
    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("is_active")
    private Boolean isActive;
}