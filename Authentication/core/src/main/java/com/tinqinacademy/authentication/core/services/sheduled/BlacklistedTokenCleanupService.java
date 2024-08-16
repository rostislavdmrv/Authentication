package com.tinqinacademy.authentication.core.services.sheduled;

import com.tinqinacademy.authentication.persistence.models.entities.BlacklistedToken;
import com.tinqinacademy.authentication.persistence.repositories.BlacklistedTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlacklistedTokenCleanupService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;

    @Scheduled(cron = "0 0 1 1/1 * ?")
    @Transactional
    public void clearBlacklistedTokens(){

        List<BlacklistedToken> tokens = blacklistedTokenRepository.getOldTokens();
        blacklistedTokenRepository.deleteAll(tokens);
    }
}
