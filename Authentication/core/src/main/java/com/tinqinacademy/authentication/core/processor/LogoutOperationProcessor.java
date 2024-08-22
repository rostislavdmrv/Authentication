package com.tinqinacademy.authentication.core.processor;

import com.tinqinacademy.authentication.api.exceptions.ResourceNotFoundException;
import com.tinqinacademy.authentication.api.models.errors.ErrorWrapper;
import com.tinqinacademy.authentication.api.models.usertoken.UserToken;
import com.tinqinacademy.authentication.api.operations.logout.LogoutInput;
import com.tinqinacademy.authentication.api.operations.logout.LogoutOperation;
import com.tinqinacademy.authentication.api.operations.logout.LogoutOutput;
import com.tinqinacademy.authentication.api.operations.promote.PromoteOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processor.base.BaseOperationProcessor;
import com.tinqinacademy.authentication.persistence.models.entities.BlacklistedToken;
import com.tinqinacademy.authentication.persistence.models.entities.User;
import com.tinqinacademy.authentication.persistence.repositories.BlacklistedTokenRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class LogoutOperationProcessor extends BaseOperationProcessor<LogoutInput, LogoutOutput> implements LogoutOperation {

    private final BlacklistedTokenRepository blacklistedTokenRepository;

    protected LogoutOperationProcessor(ConversionService conversionService, Validator validator, ErrorHandler errorHandler, BlacklistedTokenRepository blacklistedTokenRepository) {
        super(conversionService, validator, errorHandler);
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    @Override
    public Either<ErrorWrapper, LogoutOutput> process(LogoutInput input) {
        return Try.of(() -> {
                    validateInput(input);
                    log.info("Start logout input: {}", input);

                    BlacklistedToken invalidatedJwt = createInvalidatedJwt(input.getUserToken());
                    blacklistedTokenRepository.save(invalidatedJwt);

                    LogoutOutput output = createOutput();
                    log.info("End logout output: {}", output);
                    return output;


                })
                .toEither()
                .mapLeft(errorHandler::handleErrors);
    }

    private BlacklistedToken createInvalidatedJwt(UserToken tokenInput) {
        return BlacklistedToken.builder()
                .token(tokenInput.getToken())
                .build();
    }

    private LogoutOutput createOutput() {
        return LogoutOutput.builder().build();
    }
}
