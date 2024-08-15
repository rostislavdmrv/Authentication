package com.tinqinacademy.authentication.persistence.repositories;

import com.tinqinacademy.authentication.persistence.models.entities.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, UUID> {

    Optional<VerificationCode> findFirstByConfirmationCodeOrderByCreatedAtDesc(String code);

    void deleteByCreatedAtBefore(LocalDateTime dateTime);
}
