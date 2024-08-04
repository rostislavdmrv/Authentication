package com.tinqinacademy.authentication.core.errorhandler;

import com.tinqinacademy.authentication.api.exceptions.AuthValidationException;
import com.tinqinacademy.authentication.api.models.errors.ErrorWrapper;
import com.tinqinacademy.authentication.api.models.errors.ErrorsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Component
public class ErrorHandler {
    public ErrorWrapper handleErrors(Throwable throwable) {
        List<ErrorsResponse> errors = new ArrayList<>();

        HttpStatus status = determineHttpStatusAndFillErrors(throwable, errors);

        return ErrorWrapper.builder()
                .errors(errors)
                .errorHttpStatus(status)
                .build();
    }
    private HttpStatus determineHttpStatusAndFillErrors(Throwable throwable, List<ErrorsResponse> errors) {
        return Match(throwable).of(
                Case($(instanceOf(MethodArgumentNotValidException.class)), ex -> handleMethodArgumentNotValidException((MethodArgumentNotValidException) ex, errors)),
                Case($(instanceOf(AuthValidationException.class)), ex -> handleAuthValidationException((AuthValidationException) ex, errors)),
                Case($(), ex -> handleGenericException(ex, errors))
        );
    }

    private HttpStatus handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, List<ErrorsResponse> errors) {
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.add(createErrorResponse(error.getField(), error.getDefaultMessage())));
        return HttpStatus.BAD_REQUEST;
    }

    private HttpStatus handleAuthValidationException(AuthValidationException ex, List<ErrorsResponse> errors) {
        ex.getViolations().forEach(violation -> errors.add(createErrorResponse(violation.getField(), violation.getMessage())));
        return HttpStatus.BAD_REQUEST;
    }
    private HttpStatus handleGenericException(Throwable ex, List<ErrorsResponse> errors) {
        errors.add(createErrorResponse(null, ex.getMessage()));
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private ErrorsResponse createErrorResponse(String field, String message) {
        return ErrorsResponse.builder()
                .field(field)
                .message(message)
                .build();
    }
}
