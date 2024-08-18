package com.tinqinacademy.authentication.rest.interseptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.authentication.api.constants.Constants;
import com.tinqinacademy.authentication.api.exceptions.JwtException;
import com.tinqinacademy.authentication.api.exceptions.messages.Messages;
import com.tinqinacademy.authentication.api.models.enums.RoleType;
import com.tinqinacademy.authentication.api.models.errors.ErrorWrapper;
import com.tinqinacademy.authentication.api.models.errors.ErrorsResponse;
import com.tinqinacademy.authentication.core.security.JwtProvider;
import com.tinqinacademy.authentication.rest.context.ContextToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;
    private final ContextToken contextToken;
    private final JwtProvider jwtProvider;


    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        final String authHeaderValue = request.getHeader(Constants.AUTH_HEADER);

        if (authHeaderValue == null || !authHeaderValue.startsWith(Constants.BEARER_PREFIX)) {
            buildErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, Messages.EMPTY_TOKEN);
            return false;
        }

        try {
            String token = extractToken(authHeaderValue);
            jwtProvider.validate(token);
            createTokenContext(token);
        } catch (JwtException e) {
            buildErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return false;
        }

        return true;
    }

    private String extractToken(String authHeaderValue) {
        return authHeaderValue.substring(Constants.BEARER_PREFIX.length());
    }

    private void createTokenContext(String token) {
        String username = jwtProvider.getUsernameFromToken(token);
        List<RoleType> roles = jwtProvider.getRolesFromToken(token);
        Date expirationTime = jwtProvider.getExpirationTimeFromToken(token);

        List<String> rolesAsStrings = roles.stream()
                .map(Enum::name)
                .toList();

        contextToken.setToken(token);
        contextToken.setUsername(username);
        contextToken.setRoles(rolesAsStrings);
        contextToken.setExpirationTime(expirationTime);
        log.info("Created token wrapper: {}", contextToken);
    }

    private void buildErrorResponse(HttpServletResponse response, int status, String message) {
        ErrorWrapper errorWrapper =  convertToErrorWrapper(status, message);

        response.resetBuffer();
        response.setStatus(status);
        response.setHeader("Content-Type", "application/json");
        try {
            response.getOutputStream().print(objectMapper.writeValueAsString(errorWrapper));
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ErrorWrapper convertToErrorWrapper(int status, String message) {
        HttpStatus statusCode = HttpStatus.valueOf(status);
        ErrorsResponse error = ErrorsResponse.builder()
                .message(message)
                .build();

        return ErrorWrapper.builder()
                .errors(List.of(error))
                .errorHttpStatus(statusCode)
                .build();
    }
}
