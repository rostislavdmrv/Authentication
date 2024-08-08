package com.tinqinacademy.authentication.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(
                authorizeRequests ->
                        authorizeRequests
                                .requestMatchers( "/swagger-ui/**", "/login", "/register").permitAll()
                                .anyRequest().permitAll()

        ).formLogin(formLogin ->
                        formLogin
                                // Where is our custom login form?
                                .loginPage("/users/login")
                                // what is the name of the username parameter in the Login POST request?
                                .usernameParameter("email")
                                // what is the name of the password parameter in the Login POST request?
                                .passwordParameter("password")
                                // What will happen if the login is successful
                                .defaultSuccessUrl("/", true)
                                // What will happen if the login fails
                                .failureForwardUrl("/users/login-error")
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.IF_REQUIRED))
                .logout(
                        logout ->
                                logout
                                        // what is the logout URL?
                                        .logoutUrl("/users/logout")
                                        // Where to go after successful logout?
                                        .logoutSuccessUrl("/")
                                        // invalidate the session after logout.
                                        .invalidateHttpSession(true)
                )
                .build();



    }


}
