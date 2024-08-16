package com.tinqinacademy.authentication.core.converters;

import com.tinqinacademy.authentication.api.operations.register.RegisterInput;
import com.tinqinacademy.authentication.persistence.models.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RegisterInputToUserConverters implements Converter<RegisterInput, User> {
    @Override
    public User convert(RegisterInput source) {
        log.info("Start convert RegisterInput to User");
        User user = User.builder()
                .email(source.getEmail())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .username(source.getUsername())
                .phoneNumber(source.getPhoneNumber())
                .dateOfBirth(source.getDateOfBirth())
                .build();
        log.info("End convert RegisterInput to User");
        return user;
    }
}
