package com.tinqinacademy.authentication.api.operations.changepassword;

import com.tinqinacademy.authentication.api.base.OperationInput;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordInput implements OperationInput {
    @NotBlank(message = "Old password must not be blank")
    @Schema(example = "rosti1111;")
    private String oldPassword;
    @NotBlank(message = "New password must not be blank")
    @Size(min = 8, message = "New password must be at least 8 characters in length")
    @Schema(example = "newRosti11;")
    private String newPassword;
    @Email(message = "Invalid email format")
    @Schema(example = "andrey.petrov@gmail.com")
    private String email;
}
