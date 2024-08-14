package com.tinqinacademy.authentication.api.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidVerificationCode extends RuntimeException{
    public InvalidVerificationCode(String message) {
        super(message);
    }
}
