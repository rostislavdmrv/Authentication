package com.tinqinacademy.authentication.core.processor;

import com.tinqinacademy.authentication.api.models.errors.ErrorWrapper;
import com.tinqinacademy.authentication.api.operations.promote.PromoteInput;
import com.tinqinacademy.authentication.api.operations.promote.PromoteOperation;
import com.tinqinacademy.authentication.api.operations.promote.PromoteOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processor.base.BaseOperationProcessor;
import com.tinqinacademy.authentication.persistence.repositories.UserRepository;
import io.vavr.control.Either;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class PromoteOperationProcessor extends BaseOperationProcessor<PromoteInput,PromoteOutput> implements PromoteOperation {
    private final UserRepository userRepository;

    protected PromoteOperationProcessor(ConversionService conversionService, Validator validator, ErrorHandler errorHandler, UserRepository userRepository) {
        super(conversionService, validator, errorHandler);
        this.userRepository = userRepository;
    }

    @Override
    public Either<ErrorWrapper, PromoteOutput> process(PromoteInput input) {
        return null;
    }

}
