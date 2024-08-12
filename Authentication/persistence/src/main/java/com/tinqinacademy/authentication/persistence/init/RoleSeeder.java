package com.tinqinacademy.authentication.persistence.init;

import com.tinqinacademy.authentication.persistence.models.entities.Role;
import com.tinqinacademy.authentication.persistence.models.enums.RoleType;
import com.tinqinacademy.authentication.persistence.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Role> savedRoles = roleRepository.findAll();

        Set<String> savedRoleTypes = new HashSet<>();
        for (Role savedRole : savedRoles) {
            savedRoleTypes.add(savedRole.getType().name());
        }

        for (RoleType roleEnum : RoleType.values()) {
            if (!savedRoleTypes.contains(roleEnum.name())) {
                Role role = Role.builder()
                        .type(roleEnum)
                        .build();
                roleRepository.save(role);


                log.info("RoleSeeder - saved all role");
            }
        }

    }
}
