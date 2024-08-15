package com.tinqinacademy.authentication.api.operations.register;

import com.tinqinacademy.authentication.api.base.OperationInput;
import com.tinqinacademy.authentication.api.validations.dateofbirth.DateOfBirthValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterInput implements OperationInput {

    @NotBlank(message = "Username must not be blank")
    @Size(min = 2, max = 30, message = "Username must be at least 2 characters in length")
    @Schema(example = "burzia11")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters in length")
    @Schema(example = "car14BMW5;")
    private String password;

    @NotBlank(message = "First name must not be blank")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters long")
    @Schema(example = "Atanas")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Size(min = 2, max = 30, message = "Last name must be between 2 and 30 characters long")
    @Schema(example = "Hristov")
    private String lastName;


    @Email(message = "Invalid email format")
    @Schema(example = "atanas.hristov2005@outlook.com")
    private String email;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern( regexp = "^\\+[1-9]{1}[0-9]{3,14}$",
            message = "Phone number must start with a '+' followed by the country code and subscriber number digits")
    @Schema(example = "+359895713426")
    private String phoneNumber;


    @DateOfBirthValidation
    @Schema(example = "1999-08-01")
    private LocalDate dateOfBirth;
}
