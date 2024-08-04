package com.tinqinacademy.authentication.api.exceptions;

import com.tinqinacademy.authentication.api.models.errors.ErrorsResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthValidationException extends RuntimeException {
    private final List<ErrorsResponse> violations;

    public AuthValidationException(List<ErrorsResponse> violations) {
        this.violations = violations;
    }
}
