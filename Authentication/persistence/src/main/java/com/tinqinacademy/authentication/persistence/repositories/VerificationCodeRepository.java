package com.tinqinacademy.authentication.persistence.repositories;

import com.tinqinacademy.authentication.persistence.models.entities.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, UUID> {
}
