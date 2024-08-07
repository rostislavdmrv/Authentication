package com.tinqinacademy.authentication.api.operations.register;

import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterInput implements OperationInput {
    String username;
    String password;
    String firstName;
    String lastName;
    String email;
}
