package com.tinqinacademy.authentication.api.operations.confirmregistration;

import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmationCodeInput implements OperationInput {
    String confirmationCode;
}
