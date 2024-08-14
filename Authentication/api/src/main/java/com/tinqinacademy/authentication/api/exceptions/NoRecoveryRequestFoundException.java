package com.tinqinacademy.authentication.api.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoRecoveryRequestFoundException  extends RuntimeException{
    public NoRecoveryRequestFoundException(String message) {
        super(message);
    }
}
