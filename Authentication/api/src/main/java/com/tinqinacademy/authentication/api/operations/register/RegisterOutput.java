package com.tinqinacademy.authentication.api.operations.register;

import com.tinqinacademy.authentication.api.base.OperationOutput;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterOutput implements OperationOutput {
    String id;
}
