package com.tinqinacademy.authentication.rest.controllers;

import com.tinqinacademy.authentication.api.operations.changepassword.ChangePasswordInput;
import com.tinqinacademy.authentication.api.operations.changepassword.ChangePasswordOperation;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistrationInput;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistrationOperation;
import com.tinqinacademy.authentication.api.operations.demote.DemoteInput;
import com.tinqinacademy.authentication.api.operations.demote.DemoteOperation;
import com.tinqinacademy.authentication.api.operations.login.LogInInput;
import com.tinqinacademy.authentication.api.operations.login.LogInOperation;
import com.tinqinacademy.authentication.api.operations.promote.PromoteInput;
import com.tinqinacademy.authentication.api.operations.promote.PromoteOperation;
import com.tinqinacademy.authentication.api.operations.recoverpassword.RecoverPasswordInput;
import com.tinqinacademy.authentication.api.operations.recoverpassword.RecoverPasswordOperation;
import com.tinqinacademy.authentication.api.operations.register.RegisterInput;
import com.tinqinacademy.authentication.api.operations.register.RegisterOperation;
import com.tinqinacademy.authentication.api.restapiroutes.RestApiRoutes;
import com.tinqinacademy.authentication.rest.controllers.base.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController extends BaseController {

    private final LogInOperation loginOperation;
    private final RegisterOperation registerOperation;
    private final RecoverPasswordOperation recoverPasswordOperation;
    private final ConfirmRegistrationOperation confirmRegistrationOperation;
    private final ChangePasswordOperation changePasswordOperation;
    private final PromoteOperation promoteOperation;
    private final DemoteOperation demoteOperation;


    @Operation(
            summary = "User Login",
            description = "Handles user login by processing the provided login credentials. On success, a new user session is created."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created: User successfully logged in. A session is created."),
            @ApiResponse(responseCode = "400", description = "Bad Request: The request payload is invalid or incomplete."),
            @ApiResponse(responseCode = "401", description = "Unauthorized: The credentials provided are incorrect."),
            @ApiResponse(responseCode = "403", description = "Forbidden: You don't have permission to perform this action.")
    })
    @PostMapping(RestApiRoutes.LOGIN)
    public ResponseEntity<?> logIn(@RequestBody LogInInput input) {

        return handleWithStatus(loginOperation.process(input), HttpStatus.CREATED);
    }


    @Operation(
            summary = "Register a new user",
            description = "Processes the registration request for a new user. On successful registration, a new user account is created."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created: User successfully registered. A new user account has been created."),
            @ApiResponse(responseCode = "400", description = "Bad Request: The request payload is invalid or incomplete."),
            @ApiResponse(responseCode = "409", description = "Conflict: A user with the provided details already exists.")
    })
    @PostMapping(RestApiRoutes.REGISTER)
    public ResponseEntity<?> register(@RequestBody RegisterInput input) {

        return handleWithStatus(registerOperation.process(input), HttpStatus.CREATED);
    }



    @Operation(
            summary = "Recover Password",
            description = "Processes the request to recover a user's password. Sends a password recovery link or instructions to the user's email address."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Password recovery instructions have been sent to the user's email address."),
            @ApiResponse(responseCode = "400", description = "Bad Request: The request payload is invalid or incomplete."),
            @ApiResponse(responseCode = "404", description = "Not Found: No user account associated with the provided email address.")
    })

    @PostMapping(RestApiRoutes.RECOVER_PASSWORD)
    public ResponseEntity<?> recoverPassword(@RequestBody RecoverPasswordInput input) {

        return handleWithStatus(recoverPasswordOperation.process(input), HttpStatus.OK);
    }



    @Operation(
            summary = "Confirm User Registration",
            description = "Processes the request to confirm a user's registration. Typically used when the user clicks on a confirmation link sent to their email."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created: User registration confirmed successfully."),
            @ApiResponse(responseCode = "400", description = "Bad Request: The request payload is invalid or incomplete."),
            @ApiResponse(responseCode = "404", description = "Not Found: The confirmation token or user associated with it was not found."),
            @ApiResponse(responseCode = "409", description = "Conflict: The user registration has already been confirmed.")
    })    @PostMapping(RestApiRoutes.CONFIRM_REGISTRATION)
    public ResponseEntity<?> confirmRegistration(@RequestBody ConfirmRegistrationInput input) {

        return handleWithStatus(confirmRegistrationOperation.process(input), HttpStatus.CREATED);
    }



    @Operation(
            summary = "Change Password",
            description = "Processes the request to change a user's password. The user must be authenticated, and the request must contain the current password and the new password."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created: Password successfully changed."),
            @ApiResponse(responseCode = "400", description = "Bad Request: The request payload is invalid or incomplete, or the new password does not meet the security requirements."),
            @ApiResponse(responseCode = "401", description = "Unauthorized: The provided current password is incorrect."),
            @ApiResponse(responseCode = "403", description = "Forbidden: The user does not have permission to change the password."),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity: The request was well-formed but contains semantic errors or cannot be processed.")

    })
    @PostMapping(RestApiRoutes.CHANGE_PASSWORD)
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordInput input) {

        return handleWithStatus(changePasswordOperation.process(input), HttpStatus.CREATED);
    }


    @Operation(
            summary = "Promote User",
            description = "Processes the request to promote a user to a higher role or status within the system. The request should contain necessary details to identify the user and the new role or status."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created: User successfully promoted to the new role or status."),
            @ApiResponse(responseCode = "400", description = "Bad Request: The request payload is invalid or incomplete, or the promotion details are incorrect."),
            @ApiResponse(responseCode = "404", description = "Not Found: The user to be promoted was not found."),
            @ApiResponse(responseCode = "403", description = "Forbidden: The user does not have permission to perform the promotion.")
    })
    @PostMapping(RestApiRoutes.PROMOTE)
    public ResponseEntity<?> promote(@RequestBody PromoteInput input) {

        return handleWithStatus(promoteOperation.process(input), HttpStatus.CREATED);
    }




    @Operation(
            summary = "Demote User",
            description = "Processes the request to demote a user to a lower role or status within the system. The request should include necessary details to identify the user and the new role or status."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created: User successfully demoted to the new role or status."),
            @ApiResponse(responseCode = "400", description = "Bad Request: The request payload is invalid or incomplete, or the demotion details are incorrect."),
            @ApiResponse(responseCode = "404", description = "Not Found: The user to be demoted was not found."),
            @ApiResponse(responseCode = "403", description = "Forbidden: The user does not have permission to perform the demotion.")

    })
    @PostMapping(RestApiRoutes.DEMOTE)
    public ResponseEntity<?> demote(@RequestBody DemoteInput input) {

        return handleWithStatus(demoteOperation.process(input), HttpStatus.CREATED);
    }



}