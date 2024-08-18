package com.tinqinacademy.authentication.api.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtException extends RuntimeException{
    public JwtException(String message) {
        super(message);
    }
}
