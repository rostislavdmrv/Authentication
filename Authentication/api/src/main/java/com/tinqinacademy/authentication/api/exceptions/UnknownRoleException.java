package com.tinqinacademy.authentication.api.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UnknownRoleException extends RuntimeException{
    public UnknownRoleException(String message) {
        super(message);
    }
}
