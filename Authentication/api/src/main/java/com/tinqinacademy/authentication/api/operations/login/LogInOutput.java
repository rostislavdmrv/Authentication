package com.tinqinacademy.authentication.api.operations.login;

import com.tinqinacademy.authentication.api.base.OperationOutput;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LogInOutput  implements OperationOutput {
    private String token;
}
