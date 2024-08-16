package com.tinqinacademy.authentication.core.processor;
import com.tinqinacademy.authentication.api.models.errors.ErrorWrapper;
import com.tinqinacademy.authentication.api.operations.recoverpassword.RecoverPasswordInput;
import com.tinqinacademy.authentication.api.operations.recoverpassword.RecoverPasswordOperation;
import com.tinqinacademy.authentication.api.operations.recoverpassword.RecoverPasswordOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processor.base.BaseOperationProcessor;
import com.tinqinacademy.authentication.core.services.mails.MailService;
import com.tinqinacademy.authentication.persistence.models.entities.RecoveryCode;
import com.tinqinacademy.authentication.persistence.models.entities.User;
import com.tinqinacademy.authentication.persistence.repositories.RecoveryCodeRepository;
import com.tinqinacademy.authentication.persistence.repositories.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class RecoverPasswordOperationProcessor extends BaseOperationProcessor<RecoverPasswordInput,RecoverPasswordOutput> implements RecoverPasswordOperation {
    private final RecoveryCodeRepository recoveryCodeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    protected RecoverPasswordOperationProcessor(ConversionService conversionService, Validator validator, ErrorHandler errorHandler, RecoveryCodeRepository recoveryCodeRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, MailService mailService) {
        super(conversionService, validator, errorHandler);
        this.recoveryCodeRepository = recoveryCodeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Override
    @Transactional
    public Either<ErrorWrapper, RecoverPasswordOutput> process(RecoverPasswordInput input) {
        return Try.of(() -> {
                    log.info("Start recover password input: {}", input);
                    validateInput(input);
                    Optional<User> userFromEmail = userRepository.findByEmail(input.getEmail());

                    if (userFromEmail.isEmpty()) {
                        return createOutput();
                    }

                    User user = userFromEmail.get();
                    recoveryCodeRepository.deleteAllByUserId(user.getId());

                    String otp = generateRandomOtp();
                    String hashedOtp = passwordEncoder.encode(otp);
                    mailService.sendNewPasswordEmail(user.getEmail(), otp);

                    RecoveryCode code = getRecoveryCode(user, hashedOtp);
                    recoveryCodeRepository.save(code);

                    return createOutput();

                }).toEither()
                .mapLeft(errorHandler::handleErrors);

    }

    private String generateRandomOtp() {
        int length =32;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*=+?";
        SecureRandom random = new SecureRandom();


        StringBuilder otp = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            otp.append(characters.charAt(index));
        }
        return otp.toString();
    }

    private static RecoveryCode getRecoveryCode(User user, String otp) {
        return RecoveryCode.builder()
                .userId(user.getId())
                .createdAt(LocalDateTime.now())
                .otp(otp)
                .build();
    }


    private static RecoverPasswordOutput createOutput() {
        return RecoverPasswordOutput.builder().build();
    }
}
