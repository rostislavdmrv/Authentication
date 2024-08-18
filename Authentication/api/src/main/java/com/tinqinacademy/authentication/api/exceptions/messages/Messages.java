package com.tinqinacademy.authentication.api.exceptions.messages;

public class Messages {

    public static final String USERNAME_TAKEN = "User with the username %s already exists";
    public static final String EMAIL_TAKEN = "User with email %s already exists";
    public static final String PHONE_NO_TAKEN = "User with phoneNo %s already exists";
    public static final String UNKNOWN_ROLE = "Role %s does not exist";
    public static final String INVALID_CREDENTIALS = "Invalid credentials!";
    public static final String EMAIL_NOT_CONFIRMED = "Email is not confirmed!";
    public static final String RESOURCE_NOT_FOUND = "%s not found with %s : '%s'!";
    public static final String NO_RECOVERY_REQUEST_FOUND = "No recovery request has been made for this email !";
    public static final String INVALID_RECOVERY_CODE = "Invalid recovery code!";
    public static final String INVALID_VERIFICATION_CODE = "Invalid verification code!";
    public static final String TOKEN_EXPIRED = "JWT is expired!";
    public static final String NO_PERMISSION = "You do not have the necessary permissions to perform this action!";
    public static final String SELF_PROMOTE = "Cannot promote yourself";
    public static final String EMPTY_TOKEN = "JWT token is missing";
}
