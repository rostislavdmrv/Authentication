package com.tinqinacademy.authentication.core.processor;

import com.tinqinacademy.authentication.api.exceptions.EmailNotConfirmedException;
import com.tinqinacademy.authentication.api.exceptions.InvalidCredentialsException;
import com.tinqinacademy.authentication.api.exceptions.InvalidRecoveryCodeException;
import com.tinqinacademy.authentication.api.exceptions.NoRecoveryRequestFoundException;
import com.tinqinacademy.authentication.api.exceptions.messages.Messages;
import com.tinqinacademy.authentication.api.models.errors.ErrorWrapper;
import com.tinqinacademy.authentication.api.operations.changepasswordusingrecoverycode.ChangePasswordUsingRecoveryCodeInput;
import com.tinqinacademy.authentication.api.operations.changepasswordusingrecoverycode.ChangePasswordUsingRecoveryCodeOperation;
import com.tinqinacademy.authentication.api.operations.changepasswordusingrecoverycode.ChangePasswordUsingRecoveryCodeOutput;
import com.tinqinacademy.authentication.api.operations.login.LogInOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processor.base.BaseOperationProcessor;
import com.tinqinacademy.authentication.persistence.models.entities.RecoveryCode;
import com.tinqinacademy.authentication.persistence.models.entities.User;
import com.tinqinacademy.authentication.persistence.repositories.RecoveryCodeRepository;
import com.tinqinacademy.authentication.persistence.repositories.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ChangePasswordUsingRecoveryCodeOperationProcessor extends BaseOperationProcessor<ChangePasswordUsingRecoveryCodeInput, ChangePasswordUsingRecoveryCodeOutput> implements ChangePasswordUsingRecoveryCodeOperation {

    private final UserRepository userRepository;
    private final RecoveryCodeRepository recoveryCodeRepository;
    private final PasswordEncoder passwordEncoder;

    protected ChangePasswordUsingRecoveryCodeOperationProcessor(ConversionService conversionService, Validator validator, ErrorHandler errorHandler, UserRepository userRepository, RecoveryCodeRepository recoveryCodeRepository, PasswordEncoder passwordEncoder) {
        super(conversionService, validator, errorHandler);
        this.userRepository = userRepository;
        this.recoveryCodeRepository = recoveryCodeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Either<ErrorWrapper, ChangePasswordUsingRecoveryCodeOutput> process(ChangePasswordUsingRecoveryCodeInput input) {
        return Try.of(() -> {
                    validateInput(input);
                    log.info("Start change password using recovery code input: {}", input);
                    User user = userRepository.findByEmail(input.getEmail())
                            .orElseThrow(() -> new NoRecoveryRequestFoundException(Messages.NO_RECOVERY_REQUEST_FOUND));

                    RecoveryCode recoveryCode = recoveryCodeRepository.findFirstByUserIdOrderByCreatedAtDesc(user.getId())
                            .orElseThrow(() -> new NoRecoveryRequestFoundException(Messages.NO_RECOVERY_REQUEST_FOUND));

                    recoveryCodesCheck(input, recoveryCode);

                    recoveryCodeRepository.deleteAllByUserId(user.getId());
                    changePassword(input, user);

                    return ChangePasswordUsingRecoveryCodeOutput.builder().build();
                }).toEither()
                .mapLeft(errorHandler::handleErrors);
    }
    private void recoveryCodesCheck(ChangePasswordUsingRecoveryCodeInput input, RecoveryCode recoveryCode) {
        boolean recoveryCodesCheck = passwordEncoder.matches(input.getRecoveryCode(), recoveryCode.getOtp());
        if (!recoveryCodesCheck) {
            throw new InvalidRecoveryCodeException(Messages.INVALID_RECOVERY_CODE);
        }
    }

    private void changePassword(ChangePasswordUsingRecoveryCodeInput input, User user) {
        String newPasswordHash = passwordEncoder.encode(input.getNewPassword());
        user.setPassword(newPasswordHash);
        userRepository.save(user);
    }
}
