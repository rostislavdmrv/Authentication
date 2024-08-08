package com.tinqinacademy.authentication.persistence.models.entities;

import com.tinqinacademy.authentication.persistence.models.entities.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Entity
@Table(name = "blacklisted_tokens")
public class BlacklistedToken extends BaseEntity {
    @Column(name = "token", nullable = false, unique = true)
    private String token;
}
