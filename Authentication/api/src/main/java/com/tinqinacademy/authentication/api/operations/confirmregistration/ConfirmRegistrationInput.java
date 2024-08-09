package com.tinqinacademy.authentication.api.operations.confirmregistration;

import com.tinqinacademy.authentication.api.base.OperationInput;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmRegistrationInput implements OperationInput {
    @NotBlank(message = "Confirmation code cannot be blank")
    private String confirmationCode;
}
