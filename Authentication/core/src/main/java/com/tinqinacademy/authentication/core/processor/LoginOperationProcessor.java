package com.tinqinacademy.authentication.core.processor;

import com.tinqinacademy.authentication.api.exceptions.EmailNotConfirmedException;
import com.tinqinacademy.authentication.api.exceptions.InvalidCredentialsException;
import com.tinqinacademy.authentication.api.exceptions.messages.Messages;
import com.tinqinacademy.authentication.api.models.errors.ErrorWrapper;
import com.tinqinacademy.authentication.api.operations.login.LogInInput;
import com.tinqinacademy.authentication.api.operations.login.LogInOperation;
import com.tinqinacademy.authentication.api.operations.login.LogInOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processor.base.BaseOperationProcessor;
import com.tinqinacademy.authentication.core.security.JwtProvider;
import com.tinqinacademy.authentication.persistence.models.entities.Role;
import com.tinqinacademy.authentication.persistence.models.entities.User;
import com.tinqinacademy.authentication.persistence.models.enums.RoleType;
import com.tinqinacademy.authentication.persistence.repositories.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LoginOperationProcessor extends BaseOperationProcessor<LogInInput,LogInOutput> implements LogInOperation {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    protected LoginOperationProcessor(ConversionService conversionService, Validator validator, ErrorHandler errorHandler, JwtProvider jwtProvider, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(conversionService, validator, errorHandler);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Either<ErrorWrapper, LogInOutput> process(LogInInput input) {
        return Try.of(() -> {
                    log.info("Start login input: {}", input);

                    User user = userRepository.findByUsername(input.getUsername())
                            .orElseThrow(() -> new InvalidCredentialsException(Messages.INVALID_CREDENTIALS));

                    if (!user.getIsVerified()) {
                        throw new EmailNotConfirmedException(Messages.EMAIL_NOT_CONFIRMED);
                    }

                    checkPasswordsMatch(input, user);

                    String token = generateJwt(user);

                    LogInOutput output = createOutput(token);
                    log.info("End login output: {}", output);
                    return output;
        }).toEither()
                .mapLeft(errorHandler::handleErrors);


    }
    private void checkPasswordsMatch(LogInInput input, User user) {
        boolean passwordMatches = passwordEncoder.matches(input.getPassword(), user.getPassword());
        if (!passwordMatches) {
            throw new InvalidCredentialsException(Messages.INVALID_CREDENTIALS);
        }
    }

    private String generateJwt(User user) {
        List<RoleType> roles = new ArrayList<>();

        for (Role role : user.getRoles()) {
            roles.add(RoleType.valueOf(role.getType().name()));
        }
        return jwtProvider.createToken(user.getUsername(), roles);
    }

    private static LogInOutput createOutput(String token) {
        return LogInOutput.builder()
                .token(token)
                .build();
    }
}
