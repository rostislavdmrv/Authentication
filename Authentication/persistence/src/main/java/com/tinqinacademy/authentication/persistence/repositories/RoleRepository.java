package com.tinqinacademy.authentication.persistence.repositories;

import com.tinqinacademy.authentication.persistence.models.entities.Role;
import com.tinqinacademy.authentication.persistence.models.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByType(RoleType type);
}
