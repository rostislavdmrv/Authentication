package com.tinqinacademy.authentication.core.processor;

import com.tinqinacademy.authentication.api.exceptions.*;
import com.tinqinacademy.authentication.api.exceptions.messages.Messages;
import com.tinqinacademy.authentication.api.models.errors.ErrorWrapper;
import com.tinqinacademy.authentication.api.models.usertoken.UserToken;
import com.tinqinacademy.authentication.api.operations.demote.DemoteInput;
import com.tinqinacademy.authentication.api.operations.demote.DemoteOperation;
import com.tinqinacademy.authentication.api.operations.demote.DemoteOutput;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DemoteOperationProcessor extends BaseOperationProcessor<DemoteInput,DemoteOutput> implements DemoteOperation {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    protected DemoteOperationProcessor(ConversionService conversionService, Validator validator, ErrorHandler errorHandler, UserRepository userRepository, RoleRepository roleRepository) {
        super(conversionService, validator, errorHandler);
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Either<ErrorWrapper, DemoteOutput> process(DemoteInput input) {
        return Try.of(() -> {
                    log.info("Start demote input: {}", input);

                    checkUserPermissions(input.getUserToken());

                    UUID userId = UUID.fromString(input.getUserId());
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId.toString()));

                    checkForSelfDemotion(user, input.getUserToken());

                    checkForLastAdminLeft(user);

                    demoteUser(user);

                    DemoteOutput output = createOutput();
                    log.info("End demote output: {}", output);
                    return output;
                }).toEither()
                .mapLeft(errorHandler::handleErrors);
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

    private void checkForLastAdminLeft(User user) {
        boolean userIsAdmin = false;
        for (Role role : user.getRoles()) {
            if (role.getType().equals(com.tinqinacademy.authentication.persistence.models.enums.RoleType.ADMIN)) {
                userIsAdmin = true;
                break;
            }
        }
        if (!userIsAdmin) {
            return;
        }

        long adminsCount = userRepository.countUsersWithAdminRole();
        if (adminsCount <= 1) {
            throw new LastAdminException(Messages.LAST_ADMIN);
        }
    }

    private void checkForSelfDemotion(User user, UserToken userToken) {
        if (user.getUsername().equals(userToken.getUsername())) {
            throw new SelfDemoteNotAllowedException(Messages.SELF_DEMOTE);
        }
    }

    private void demoteUser(User user) {
        RoleType adminRole = RoleType.ADMIN;
        Role admin = roleRepository.findByType(adminRole)
                .orElseThrow(() -> new UnknownRoleException(adminRole.toString()));

        List<Role> rolesToRemove = new ArrayList<>();
        for (Role role : user.getRoles()) {
            if (role.getId().equals(admin.getId())) {
                rolesToRemove.add(role);
            }
        }
        user.getRoles().removeAll(rolesToRemove);

        userRepository.save(user);
    }

    private DemoteOutput createOutput() {
        return DemoteOutput.builder().build();
    }
}
