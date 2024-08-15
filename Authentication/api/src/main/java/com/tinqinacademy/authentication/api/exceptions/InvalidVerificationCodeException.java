package com.tinqinacademy.authentication.api.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidVerificationCodeException extends RuntimeException{
    public InvalidVerificationCodeException(String message) {
        super(message);
    }
}
