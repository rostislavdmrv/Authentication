package com.tinqinacademy.authentication.persistence.models.entities;

import com.tinqinacademy.authentication.persistence.models.entities.base.BaseEntity;
import com.tinqinacademy.authentication.persistence.models.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "email", nullable = false, unique = true,length = 100)
    private String email;

    @Column(name ="password",nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false,length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false,length = 50)
    private String lastName;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Column(name = "phone_number", nullable = false, length = 14)
    private String phoneNumber;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
