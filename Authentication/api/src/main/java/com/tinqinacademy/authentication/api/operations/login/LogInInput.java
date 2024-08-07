package com.tinqinacademy.authentication.api.operations.login;

import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogInInput  implements OperationInput {
    String username;
    String password;
}
