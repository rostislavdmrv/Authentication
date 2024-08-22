package com.tinqinacademy.authentication.api.operations.logout;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.authentication.api.base.OperationInput;
import com.tinqinacademy.authentication.api.models.usertoken.UserToken;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class LogoutInput implements OperationInput {
    @JsonIgnore
    private UserToken userToken;
}
