package com.tinqinacademy.authentication.core.services.sheduled;

import com.tinqinacademy.authentication.persistence.repositories.RecoveryCodeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RecoveryCodeCleanupService {

    private final RecoveryCodeRepository recoveryCodeRepository;

    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void deleteExpiredRecoveryCodes() {
        LocalDateTime now = LocalDateTime.now();
        recoveryCodeRepository.deleteByCreatedAtBefore(now.minusMinutes(5));
    }
}
