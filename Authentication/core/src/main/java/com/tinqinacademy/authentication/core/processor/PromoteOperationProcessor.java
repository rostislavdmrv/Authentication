package com.tinqinacademy.authentication.core.processor;

import com.tinqinacademy.authentication.api.exceptions.NoPermissionsException;
import com.tinqinacademy.authentication.api.exceptions.ResourceNotFoundException;
import com.tinqinacademy.authentication.api.exceptions.SelfPromoteNotAllowedException;
import com.tinqinacademy.authentication.api.exceptions.UnknownRoleException;
import com.tinqinacademy.authentication.api.exceptions.messages.Messages;
import com.tinqinacademy.authentication.api.models.errors.ErrorWrapper;
import com.tinqinacademy.authentication.api.models.usertoken.UserToken;
import com.tinqinacademy.authentication.api.operations.promote.PromoteInput;
import com.tinqinacademy.authentication.api.operations.promote.PromoteOperation;
import com.tinqinacademy.authentication.api.operations.promote.PromoteOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processor.base.BaseOperationProcessor;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class PromoteOperationProcessor extends BaseOperationProcessor<PromoteInput,PromoteOutput> implements PromoteOperation {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    protected PromoteOperationProcessor(ConversionService conversionService, Validator validator, ErrorHandler errorHandler, UserRepository userRepository, RoleRepository roleRepository) {
        super(conversionService, validator, errorHandler);
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Either<ErrorWrapper, PromoteOutput> process(PromoteInput input) {
        return Try.of(() -> {
                    validateInput(input);

                    log.info("Start promote input: {}", input);

                    checkUserPermissions(input.getUserToken());

                    UUID userId = UUID.fromString(input.getUserId());
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User","userId", userId.toString()));

                    checkForSelfPromotion(user, input.getUserToken());

                    promoteToAdmin(user);

                    PromoteOutput output = createOutput();
                    log.info("End promote output: {}", output);
                    return output;


                })
                .toEither()
                .mapLeft(errorHandler::handleErrors);
    }

    private void checkForSelfPromotion(User user, UserToken userToken) {
        if (user.getUsername().equals(userToken.getUsername())) {
            throw new SelfPromoteNotAllowedException(Messages.SELF_PROMOTE);
        }
    }

    private void checkUserPermissions(UserToken userToken) {
        boolean hasAdminRole = false;
        for (com.tinqinacademy.authentication.api.models.enums.RoleType role : userToken.getRoles()) {
            if (role.equals(com.tinqinacademy.authentication.api.models.enums.RoleType.ADMIN)) {
                hasAdminRole = true;
                break;
            }
        }
        if (!hasAdminRole) {
            throw new NoPermissionsException(Messages.NO_PERMISSION);
        }
    }

    private void promoteToAdmin(User user) {
        RoleType role = RoleType.ADMIN;
        Role adminRole = roleRepository.findByType(role)
                .orElseThrow(() -> new UnknownRoleException(role.toString()));

        List<Role> userRoles = user.getRoles();
        boolean alreadyHasAdminRole = false;

        for (Role userRole : userRoles) {
            if (userRole.getType().equals(adminRole.getType())) {
                alreadyHasAdminRole = true;
                break;
            }
        }

        if (alreadyHasAdminRole) {
            return;
        }

        userRoles.add(adminRole);
        userRepository.save(user);
    }

    private PromoteOutput createOutput() {
        return PromoteOutput.builder().build();
    }

}
