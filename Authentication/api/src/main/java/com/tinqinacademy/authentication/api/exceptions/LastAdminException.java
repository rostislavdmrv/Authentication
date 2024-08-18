package com.tinqinacademy.authentication.api.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LastAdminException extends RuntimeException{
    public LastAdminException(String message) {
        super(message);
    }
}
