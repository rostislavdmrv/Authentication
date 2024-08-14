package com.tinqinacademy.authentication.restexport;
import com.tinqinacademy.authentication.api.feignclientapiroutes.FeignClientApiRoutes;
import com.tinqinacademy.authentication.api.operations.validatejwt.ValidateJwtOutput;
import feign.Headers;
import feign.Param;
import feign.RequestLine;


@Headers({"Content-Type: application/json"})
public interface AuthClient {

    @RequestLine(FeignClientApiRoutes.VALIDATE_TOKEN)
    @Headers({"Authorization: {authorizationHeader}"})
    ValidateJwtOutput validateToken(@Param("authorizationHeader") String authorizationHeader);
}
