package com.tinqinacademy.authentication.core.services;

import com.tinqinacademy.authentication.persistence.models.entities.User;
import com.tinqinacademy.authentication.persistence.models.entities.VerificationCode;
import com.tinqinacademy.authentication.persistence.repositories.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailConfirmationService {

    private final MailService mailService;
    private final VerificationCodeRepository verificationCodeRepository;

    @Async
    public void sendConfirmationEmail(User user) {

        String code = generateCode();
        mailService.sendRegistrationEmail(user.getEmail(), code);

        VerificationCode verificationCode = VerificationCode.builder()
                .confirmationCode(code)
                .userId(user.getId())
                .createdAt(LocalDateTime.now())
                .build();

        verificationCodeRepository.save(verificationCode);
    }

    private String generateCode() {
        SecureRandom random = new SecureRandom();

        return random.ints(8, 0, 10)
                .mapToObj(Integer::toString)
                .reduce((a, b) -> a + b)
                .get();
    }
}
