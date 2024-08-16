package com.tinqinacademy.authentication.api.exceptions;

import com.tinqinacademy.authentication.api.exceptions.messages.Messages;
import lombok.Getter;

@Getter
public class UnknownRoleException extends RuntimeException{

    private String roleType;

    public UnknownRoleException(String roleType) {
        super(String.format(Messages.UNKNOWN_ROLE, roleType));
        this.roleType = roleType;
    }
}
