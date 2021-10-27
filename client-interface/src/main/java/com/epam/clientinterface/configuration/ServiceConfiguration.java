package com.epam.clientinterface.configuration;

import com.epam.clientinterface.service.AuthService;
import com.epam.clientinterface.service.impl.AuthServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public AuthService authService() {
        return new AuthServiceImpl();
    }

}
