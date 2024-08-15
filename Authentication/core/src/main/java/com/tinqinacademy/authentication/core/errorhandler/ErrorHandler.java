package com.tinqinacademy.authentication.core.errorhandler;
import com.tinqinacademy.authentication.api.exceptions.*;
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
                Case($(instanceOf(ResourceNotFoundException.class)), ex -> handleResourceNotFoundException((ResourceNotFoundException) ex, errors)),
                Case($(instanceOf(AlreadyExistsException.class)), ex -> handleAlreadyExistsException((AlreadyExistsException) ex, errors)),
                Case($(instanceOf(EmailNotConfirmedException.class)), ex -> handleEmailNotConfirmedException((EmailNotConfirmedException) ex, errors)),
                Case($(instanceOf(InvalidCredentialsException.class)), ex -> handleInvalidCredentialsException((InvalidCredentialsException) ex, errors)),
                Case($(instanceOf(UnknownRoleException.class)), ex -> handleUnknownRoleException((UnknownRoleException) ex, errors)),
                Case($(instanceOf(TokenExpiredException.class)), ex -> handleTokenExpiredException((TokenExpiredException) ex, errors)),
                Case($(instanceOf(InvalidRecoveryCodeException.class)), ex -> handleInvalidRecoveryCodeException((InvalidRecoveryCodeException) ex, errors)),
                Case($(instanceOf(NoRecoveryRequestFoundException.class)), ex -> handleNoRecoveryRequestFoundException((NoRecoveryRequestFoundException) ex, errors)),
                Case($(instanceOf(InvalidVerificationCodeException.class)), ex -> handleInvalidVerificationCodeException((InvalidVerificationCodeException) ex, errors)),
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

    private HttpStatus handleResourceNotFoundException(ResourceNotFoundException ex, List<ErrorsResponse> errors) {
        errors.add(createErrorResponse(null, ex.getMessage()));
        return HttpStatus.NOT_FOUND;
    }

    private HttpStatus handleGenericException(Throwable ex, List<ErrorsResponse> errors) {
        errors.add(createErrorResponse(null, ex.getMessage()));
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private HttpStatus handleAlreadyExistsException(AlreadyExistsException ex, List<ErrorsResponse> errors) {
        errors.add(createErrorResponse(null, ex.getMessage()));
        return HttpStatus.CONFLICT;
    }

    private HttpStatus handleEmailNotConfirmedException(EmailNotConfirmedException ex, List<ErrorsResponse> errors) {
        errors.add(createErrorResponse(null, ex.getMessage()));
        return HttpStatus.FORBIDDEN;
    }

    private HttpStatus handleInvalidCredentialsException(InvalidCredentialsException ex, List<ErrorsResponse> errors) {
        errors.add(createErrorResponse(null, ex.getMessage()));
        return HttpStatus.UNAUTHORIZED;
    }

    private HttpStatus handleUnknownRoleException(UnknownRoleException ex, List<ErrorsResponse> errors) {
        errors.add(createErrorResponse(null, ex.getMessage()));
        return HttpStatus.FORBIDDEN;
    }

    private HttpStatus handleTokenExpiredException(TokenExpiredException ex, List<ErrorsResponse> errors) {
        errors.add(createErrorResponse(null, ex.getMessage()));
        return HttpStatus.UNAUTHORIZED;
    }


    private HttpStatus handleInvalidRecoveryCodeException(InvalidRecoveryCodeException ex, List<ErrorsResponse> errors) {
        errors.add(createErrorResponse(null, ex.getMessage()));
        return HttpStatus.BAD_REQUEST;
    }


    private HttpStatus handleNoRecoveryRequestFoundException(NoRecoveryRequestFoundException ex, List<ErrorsResponse> errors) {
        errors.add(createErrorResponse(null, ex.getMessage()));
        return HttpStatus.NOT_FOUND;
    }


    private HttpStatus handleInvalidVerificationCodeException(InvalidVerificationCodeException ex, List<ErrorsResponse> errors) {
        errors.add(createErrorResponse(null, ex.getMessage()));
        return HttpStatus.BAD_REQUEST;
    }

    private ErrorsResponse createErrorResponse(String field, String message) {
        return ErrorsResponse.builder()
                .field(field)
                .message(message)
                .build();
    }
}
