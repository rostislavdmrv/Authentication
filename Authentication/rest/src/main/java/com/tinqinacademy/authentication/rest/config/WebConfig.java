package com.tinqinacademy.authentication.rest.config;

import com.tinqinacademy.authentication.api.restapiroutes.RestApiRoutes;
import com.tinqinacademy.authentication.rest.interseptor.TokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig  implements WebMvcConfigurer {

    private final TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns(securedEndpoints());
    }

    private String[] securedEndpoints() {
        return new String[]{RestApiRoutes.PROMOTE};
    }
}
