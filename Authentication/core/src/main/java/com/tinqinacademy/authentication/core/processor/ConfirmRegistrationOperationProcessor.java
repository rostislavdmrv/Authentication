package com.tinqinacademy.authentication.core.processor;

import com.tinqinacademy.authentication.api.models.errors.ErrorWrapper;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistrationInput;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistrationOperation;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistrationOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processor.base.BaseOperationProcessor;
import io.vavr.control.Either;
import jakarta.validation.Validator;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
public class ConfirmRegistrationOperationProcessor extends BaseOperationProcessor<ConfirmRegistrationInput,ConfirmRegistrationOutput> implements ConfirmRegistrationOperation {
    protected ConfirmRegistrationOperationProcessor(ConversionService conversionService, Validator validator, ErrorHandler errorHandler) {
        super(conversionService, validator, errorHandler);
    }

    @Override
    public Either<ErrorWrapper, ConfirmRegistrationOutput> process(ConfirmRegistrationInput input) {
        return null;
    }
}
