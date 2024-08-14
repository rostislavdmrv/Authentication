package com.tinqinacademy.authentication.persistence.repositories;

import com.tinqinacademy.authentication.persistence.models.entities.RecoveryCode;
import com.tinqinacademy.authentication.persistence.models.entities.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecoveryCodeRepository  extends JpaRepository<RecoveryCode, UUID> {
    Optional<RecoveryCode> findFirstByUserIdOrderByCreatedAtDesc(UUID userId);
    void deleteAllByUserId(UUID userId);

}
