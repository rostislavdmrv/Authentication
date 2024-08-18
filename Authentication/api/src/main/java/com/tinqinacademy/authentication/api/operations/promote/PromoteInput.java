package com.tinqinacademy.authentication.api.operations.promote;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.authentication.api.base.OperationInput;
import com.tinqinacademy.authentication.api.models.usertoken.UserToken;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoteInput implements OperationInput {
    @NotBlank(message = "User id  must not be blank!")
    @UUID
    private String userId;

    @JsonIgnore
    private UserToken userToken;
}
