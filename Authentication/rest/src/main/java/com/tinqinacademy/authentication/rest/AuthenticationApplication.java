package com.tinqinacademy.authentication.rest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "com.tinqinacademy.authentication")
@EntityScan(basePackages = "com.tinqinacademy.authentication.persistence.models")
@EnableJpaRepositories(basePackages = "com.tinqinacademy.authentication.persistence.repositories")
@EnableScheduling
public class AuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }

}
