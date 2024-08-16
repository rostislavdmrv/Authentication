package com.tinqinacademy.authentication.api.exceptions;

public class SelfPromoteNotAllowedException extends RuntimeException{
    public SelfPromoteNotAllowedException(String message) {
        super(message);
    }
}
