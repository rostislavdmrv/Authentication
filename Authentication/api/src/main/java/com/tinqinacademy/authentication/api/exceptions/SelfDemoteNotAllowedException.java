package com.tinqinacademy.authentication.api.exceptions;

import lombok.Getter;

@Getter
public class SelfDemoteNotAllowedException extends RuntimeException {
    public SelfDemoteNotAllowedException(String message) {
        super(message);
    }
}
