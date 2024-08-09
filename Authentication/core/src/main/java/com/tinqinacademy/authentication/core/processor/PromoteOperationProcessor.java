package com.tinqinacademy.authentication.core.processor;

import com.tinqinacademy.authentication.api.models.errors.ErrorWrapper;
import com.tinqinacademy.authentication.api.operations.promote.PromoteInput;
import com.tinqinacademy.authentication.api.operations.promote.PromoteOperation;
import com.tinqinacademy.authentication.api.operations.promote.PromoteOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processor.base.BaseOperationProcessor;
import io.vavr.control.Either;
import jakarta.validation.Validator;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
public class PromoteOperationProcessor extends BaseOperationProcessor implements PromoteOperation {
    protected PromoteOperationProcessor(ConversionService conversionService, Validator validator, ErrorHandler errorHandler) {
        super(conversionService, validator, errorHandler);
    }

    @Override
    public Either<ErrorWrapper, PromoteOutput> process(PromoteInput input) {
        return null;
    }
}
