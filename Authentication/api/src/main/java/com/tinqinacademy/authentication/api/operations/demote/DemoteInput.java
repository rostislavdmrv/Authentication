package com.tinqinacademy.authentication.api.operations.demote;

import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemoteInput implements OperationInput {
    String userId;
}
