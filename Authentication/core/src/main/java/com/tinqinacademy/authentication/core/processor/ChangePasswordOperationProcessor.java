package com.tinqinacademy.authentication.core.processor;

import com.tinqinacademy.authentication.api.exceptions.InvalidCredentialsException;
import com.tinqinacademy.authentication.api.exceptions.messages.Messages;
import com.tinqinacademy.authentication.api.models.errors.ErrorWrapper;
import com.tinqinacademy.authentication.api.operations.changepassword.ChangePasswordInput;
import com.tinqinacademy.authentication.api.operations.changepassword.ChangePasswordOperation;
import com.tinqinacademy.authentication.api.operations.changepassword.ChangePasswordOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processor.base.BaseOperationProcessor;
import com.tinqinacademy.authentication.persistence.models.entities.User;
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
public class ChangePasswordOperationProcessor extends BaseOperationProcessor<ChangePasswordInput,ChangePasswordOutput> implements ChangePasswordOperation {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    protected ChangePasswordOperationProcessor(ConversionService conversionService, Validator validator, ErrorHandler errorHandler, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(conversionService, validator, errorHandler);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Either<ErrorWrapper, ChangePasswordOutput> process(ChangePasswordInput input) {
        return Try.of(() -> {
                    log.info("Start change password input: {}", input);

                    User user = userRepository.findByEmail(input.getEmail())
                            .orElseThrow(() -> new InvalidCredentialsException(Messages.INVALID_CREDENTIALS));

                    ensurePasswordsMatch(input, user);

                    changePassword(input, user);

                    ChangePasswordOutput output = createOutput();
                    log.info("End change password output: {}", output);
                    return output;
                }).toEither()
                .mapLeft(errorHandler::handleErrors);

    }

    private static ChangePasswordOutput createOutput() {
        return ChangePasswordOutput.builder().build();
    }

    private void ensurePasswordsMatch(ChangePasswordInput input, User user) {
        boolean passwordsMatch = passwordEncoder.matches(input.getOldPassword(), user.getPassword());
        if (!passwordsMatch) {
            throw new InvalidCredentialsException(Messages.INVALID_CREDENTIALS);
        }
    }

    private void changePassword(ChangePasswordInput validInput, User user) {
        String newPasswordHash = passwordEncoder.encode(validInput.getNewPassword());
        user.setPassword(newPasswordHash);
        userRepository.save(user);
    }
}
