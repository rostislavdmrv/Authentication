package com.tinqinacademy.authentication.persistence.models.entities;

import com.tinqinacademy.authentication.persistence.models.entities.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "recovery_codes")
public class RecoveryCode extends BaseEntity {

    @Column(name = "confirmation_code", nullable = false)
    private String otp;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
