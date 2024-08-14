package com.tinqinacademy.authentication.api.operations.validatejwt;

import com.tinqinacademy.authentication.api.base.OperationOutput;
import lombok.*;
import org.springframework.security.core.userdetails.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ValidateJwtOutput  implements OperationOutput {
    private User user;
}
