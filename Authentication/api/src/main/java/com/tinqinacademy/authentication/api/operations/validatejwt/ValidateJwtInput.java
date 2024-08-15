package com.tinqinacademy.authentication.api.operations.validatejwt;

import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ValidateJwtInput implements OperationInput {

    private String authorizationHeader;
}
