package com.tinqinacademy.authentication.api.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidRecoveryCodeException extends RuntimeException{
    public InvalidRecoveryCodeException(String message) {
        super(message);
    }
}
