package com.tinqinacademy.authentication.core.processor;
import com.tinqinacademy.authentication.api.exceptions.InvalidVerificationCodeException;
import com.tinqinacademy.authentication.api.exceptions.messages.Messages;
import com.tinqinacademy.authentication.api.exceptions.ResourceNotFoundException;
import com.tinqinacademy.authentication.api.models.errors.ErrorWrapper;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistrationInput;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistrationOperation;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistrationOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processor.base.BaseOperationProcessor;
import com.tinqinacademy.authentication.persistence.models.entities.User;
import com.tinqinacademy.authentication.persistence.models.entities.VerificationCode;
import com.tinqinacademy.authentication.persistence.repositories.UserRepository;
import com.tinqinacademy.authentication.persistence.repositories.VerificationCodeRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ConfirmRegistrationOperationProcessor extends BaseOperationProcessor<ConfirmRegistrationInput,ConfirmRegistrationOutput> implements ConfirmRegistrationOperation {

    private final VerificationCodeRepository verificationCodeRepository;
    private final UserRepository userRepository;

    protected ConfirmRegistrationOperationProcessor(ConversionService conversionService, Validator validator, ErrorHandler errorHandler, VerificationCodeRepository verificationCodeRepository, UserRepository userRepository) {
        super(conversionService, validator, errorHandler);
        this.verificationCodeRepository = verificationCodeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Either<ErrorWrapper, ConfirmRegistrationOutput> process(ConfirmRegistrationInput input) {
        return Try.of(() -> {
                    validateInput(input);
                    log.info("Start confirm registration input: {}", input);

                    VerificationCode verificationCode = verificationCodeRepository.findFirstByConfirmationCodeOrderByCreatedAtDesc(input.getConfirmationCode())
                            .orElseThrow(() -> new InvalidVerificationCodeException(Messages.INVALID_VERIFICATION_CODE));

                    UUID userId = verificationCode.getUserId();
                    verificationCodeRepository.delete(verificationCode);
                    verifyUser(userId);

                    ConfirmRegistrationOutput output = createOutput();
                    log.info("End confirm registration output: {}", output);
                    return output;
                })
                .toEither()
                .mapLeft(errorHandler::handleErrors);
    }
    private void verifyUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId",userId.toString()));
        user.setIsVerified(Boolean.TRUE);
        userRepository.save(user);
    }

    private ConfirmRegistrationOutput createOutput() {
        return ConfirmRegistrationOutput.builder().build();
    }
}
