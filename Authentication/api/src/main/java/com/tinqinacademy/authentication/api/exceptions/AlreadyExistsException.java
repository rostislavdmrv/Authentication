package com.tinqinacademy.authentication.api.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
