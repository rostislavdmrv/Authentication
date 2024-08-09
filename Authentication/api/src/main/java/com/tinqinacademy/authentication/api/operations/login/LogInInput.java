package com.tinqinacademy.authentication.api.operations.login;

import com.tinqinacademy.authentication.api.base.OperationInput;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogInInput  implements OperationInput {
    @NotBlank(message = "Username must not be blank")
    @Schema(example = "1peter8")
    String username;

    @NotBlank(message = "Password must not be blank")
    @Schema(example = "Football1852;")
    String password;
}
