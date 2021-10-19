package com.epam.clientinterface.configuration;

import com.epam.clientinterface.configuration.property.DatasourceConnectionProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertyConfiguration {

    @Bean
    public DatasourceConnectionProperties datasourceConnectionProperties(
        @Value("${spring.datasource.url}") String url,
        @Value("${spring.datasource.username}") String username,
        @Value("${spring.datasource.password}") String password
    ) {
        return new DatasourceConnectionProperties(url, username, password);
    }
}
