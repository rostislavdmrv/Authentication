package com.tinqinacademy.authentication.api.operations.changepassword;

import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordInput implements OperationInput {
    String oldPassword;
    String newPassword;
    String email;
}
