package com.tinqinacademy.authentication.core.processor;

import com.tinqinacademy.authentication.api.models.errors.ErrorWrapper;
import com.tinqinacademy.authentication.api.operations.register.RegisterOutput;
import com.tinqinacademy.authentication.api.operations.validatejwt.ValidateJwtInput;
import com.tinqinacademy.authentication.api.operations.validatejwt.ValidateJwtOperation;
import com.tinqinacademy.authentication.api.operations.validatejwt.ValidateJwtOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processor.base.BaseOperationProcessor;
import com.tinqinacademy.authentication.core.security.JwtProvider;
import org.springframework.security.core.userdetails.User;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ValidateJwtOperationProcessor extends BaseOperationProcessor<ValidateJwtInput, ValidateJwtOutput> implements ValidateJwtOperation {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    protected ValidateJwtOperationProcessor(ConversionService conversionService, Validator validator, ErrorHandler errorHandler, JwtProvider jwtProvider, UserDetailsService userDetailsService) {
        super(conversionService, validator, errorHandler);
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Either<ErrorWrapper, ValidateJwtOutput> process(ValidateJwtInput input) {
        return Try.of(() -> {
                    validateInput(input);
                    log.info("Start validate token input: {}", input);

                    String username = jwtProvider.getUsernameFromToken(input.getAuthorizationHeader());

                    User userDetails = (User) userDetailsService.loadUserByUsername(username);

                    ValidateJwtOutput output = createOutput(userDetails);
                    log.info("End validate token output: {}", output);
                    return output;

                })
                .toEither()
                .mapLeft(errorHandler::handleErrors);
    }

    private ValidateJwtOutput createOutput(User userDetails) {
        return ValidateJwtOutput.builder()
                .user(userDetails)
                .build();
    }
}
