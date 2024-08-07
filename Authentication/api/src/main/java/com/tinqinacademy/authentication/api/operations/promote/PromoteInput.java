package com.tinqinacademy.authentication.api.operations.promote;

import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoteInput implements OperationInput {
    String userId;
}
