package com.tinqinacademy.authentication.core.services.sheduled;

import com.tinqinacademy.authentication.persistence.repositories.VerificationCodeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerificationCodeCleanupService {

    private final VerificationCodeRepository verificationCodeRepository;

    @Scheduled(cron = "0 */15 * * * *")
    @Transactional
    public void deleteExpiredVerificationCodes() {
        LocalDateTime now = LocalDateTime.now();
        verificationCodeRepository.deleteByCreatedAtBefore(now.minusMinutes(15));
    }
}
