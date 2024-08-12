package com.tinqinacademy.authentication.core.processor;

import com.tinqinacademy.authentication.api.models.errors.ErrorWrapper;
import com.tinqinacademy.authentication.api.operations.login.LogInInput;
import com.tinqinacademy.authentication.api.operations.login.LogInOperation;
import com.tinqinacademy.authentication.api.operations.login.LogInOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processor.base.BaseOperationProcessor;
import com.tinqinacademy.authentication.core.security.JwtProvider;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginOperationProcessor extends BaseOperationProcessor<LogInInput,LogInOutput> implements LogInOperation {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    protected LoginOperationProcessor(ConversionService conversionService, Validator validator, ErrorHandler errorHandler, AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        super(conversionService, validator, errorHandler);
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Either<ErrorWrapper, LogInOutput> process(LogInInput input) {
        return Try.of(() -> {

//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword()));
//
//            String token = jwtProvider.createToken();

            return LogInOutput.builder()
                    .token("ssssssssssssssssss")
                    .build();
        }).toEither()
                .mapLeft(errorHandler::handleErrors);


    }
}
