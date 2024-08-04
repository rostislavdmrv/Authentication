package com.tinqinacademy.authentication.api.models.errors;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorWrapper {
    private List<ErrorsResponse> errors;
    private HttpStatus errorHttpStatus;
}
