package com.tinqinacademy.authentication.api.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoPermissionsException extends RuntimeException{

    public NoPermissionsException(String message) {
        super(message);
    }
}
