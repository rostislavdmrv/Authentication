package com.tinqinacademy.authentication.api.operations.recoverpassword;

import com.tinqinacademy.authentication.api.base.OperationInput;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecoverPasswordInput implements OperationInput {

    @Email(message = "Invalid email format")
    @Schema(example = "andrey.petrov@gmail.com")
    private String email;
}
