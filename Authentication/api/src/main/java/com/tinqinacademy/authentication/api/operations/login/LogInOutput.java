package com.tinqinacademy.authentication.api.operations.login;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.authentication.api.base.OperationOutput;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LogInOutput  implements OperationOutput {

    @JsonIgnore
    private String token;
}
