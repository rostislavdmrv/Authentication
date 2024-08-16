package com.tinqinacademy.authentication.core.processor;

import com.tinqinacademy.authentication.api.exceptions.AlreadyExistsException;
import com.tinqinacademy.authentication.api.exceptions.messages.Messages;
import com.tinqinacademy.authentication.api.exceptions.UnknownRoleException;
import com.tinqinacademy.authentication.api.models.errors.ErrorWrapper;
import com.tinqinacademy.authentication.api.operations.register.RegisterInput;
import com.tinqinacademy.authentication.api.operations.register.RegisterOperation;
import com.tinqinacademy.authentication.api.operations.register.RegisterOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processor.base.BaseOperationProcessor;
import com.tinqinacademy.authentication.core.services.mails.EmailConfirmationService;
import com.tinqinacademy.authentication.persistence.models.entities.Role;
import com.tinqinacademy.authentication.persistence.models.entities.User;
import com.tinqinacademy.authentication.persistence.models.enums.RoleType;
import com.tinqinacademy.authentication.persistence.repositories.RoleRepository;
import com.tinqinacademy.authentication.persistence.repositories.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class RegisterOperationProcessor extends BaseOperationProcessor<RegisterInput,RegisterOutput> implements RegisterOperation {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailConfirmationService emailConfirmationService;


    protected RegisterOperationProcessor(ConversionService conversionService, Validator validator, ErrorHandler errorHandler, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, EmailConfirmationService emailConfirmationService) {
        super(conversionService, validator, errorHandler);
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailConfirmationService = emailConfirmationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Either<ErrorWrapper, RegisterOutput> process(RegisterInput input) {
        return Try.of(() -> {
                    validateInput(input);
                    log.info("Start register input: {}", input);

                    checkDuplicateUsername(input);
                    checkDuplicateEmail(input);
                    checkDuplicatePhoneNo(input);


                    String securePasswordHash = passwordEncoder.encode(input.getPassword());
                    log.info("Generated secure password hash: {}", securePasswordHash);

                    User userToSave = convertToUser(input, securePasswordHash);

                    User savedUser = userRepository.save(userToSave);

                    emailConfirmationService.sendConfirmationEmail(savedUser);

                    RegisterOutput output = createOutput(savedUser);
                    log.info("End register output: {}", output);
                    return output;

                })
                .toEither()
                .mapLeft(errorHandler::handleErrors);
    }

    private void checkDuplicateUsername(RegisterInput input) {
        String username = input.getUsername();

        boolean idDuplicateUsername = userRepository.existsByUsername(username);
        if (idDuplicateUsername) {
            throw new AlreadyExistsException(String.format(Messages.USERNAME_TAKEN, username));
        }
    }

    private void checkDuplicateEmail(RegisterInput input) {
        String email = input.getEmail();

        boolean idDuplicateEmail = userRepository.existsByEmail(email);
        if (idDuplicateEmail) {
            throw new AlreadyExistsException(String.format(Messages.EMAIL_TAKEN, email));
        }
    }

    private void checkDuplicatePhoneNo(RegisterInput input) {
        String phoneNo = input.getPhoneNumber();

        boolean idDuplicatePhoneNo = userRepository.existsByPhoneNumber(phoneNo);
        if (idDuplicatePhoneNo) {
            throw new AlreadyExistsException(String.format(Messages.PHONE_NO_TAKEN, phoneNo));
        }
    }
    private User convertToUser(RegisterInput input, String securePasswordHash) {
        User user = conversionService.convert(input, User.class);
        user.setPassword(securePasswordHash);
        Role userRole = roleRepository.findByType(RoleType.USER)
                .orElseThrow(() -> new UnknownRoleException(RoleType.USER.toString()));
        user.setRoles(new ArrayList<>());
        user.getRoles().add(userRole);
        user.setIsVerified(Boolean.FALSE);

        promoteIfNoAdmins(user);
        return user;
    }

    private void promoteIfNoAdmins(User user) {
        Long adminsCount = userRepository.countUsersWithAdminRole();
        if (adminsCount == 0) {
            Role adminRole = roleRepository.findByType(RoleType.ADMIN)
                    .orElseThrow(() -> new UnknownRoleException(String.format(Messages.UNKNOWN_ROLE, RoleType.ADMIN)));
            user.getRoles().add(adminRole);
        }
    }
    private RegisterOutput createOutput(User savedUser) {
        return RegisterOutput.builder()
                .id(savedUser.getId().toString())
                .build();
    }

}
