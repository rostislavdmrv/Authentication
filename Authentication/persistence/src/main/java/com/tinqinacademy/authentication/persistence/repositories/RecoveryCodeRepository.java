package com.tinqinacademy.authentication.persistence.repositories;

import com.tinqinacademy.authentication.persistence.models.entities.RecoveryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecoveryCodeRepository  extends JpaRepository<RecoveryCode, UUID> {
    Optional<RecoveryCode> findFirstByUserIdOrderByCreatedAtDesc(UUID userId);
    void deleteAllByUserId(UUID userId);

    void deleteByCreatedAtBefore(LocalDateTime dateTime);

}
