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
@Table("admin")
public class Admin {

    @Id
    @Column("admin_id")
    private Integer adminId;

    @Column("username")
    private String username;

    @Column("password_hash")
    private String passwordHash;

    @ReadOnlyProperty
    @Column("created_at")
    private OffsetDateTime createdAt;

}