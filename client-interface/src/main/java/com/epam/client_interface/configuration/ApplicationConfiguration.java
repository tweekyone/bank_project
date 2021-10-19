package com.epam.client_interface.configuration;

import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan("com.epam.client_interface.*")
@EnableWebMvc
public class ApplicationConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(
        Environment environment,
        ResourceLoader resourceLoader
    ) throws IOException {
        DefaultPropertySourceFactory defaultPropertySourceFactory = new DefaultPropertySourceFactory();
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer =
            new PropertySourcesPlaceholderConfigurer();

        addDefaultPropertyFile(environment, resourceLoader, defaultPropertySourceFactory);

        for (String activeProfile : environment.getActiveProfiles()) {
            String resolvedLocation = environment.resolveRequiredPlaceholders(
                String.format("classpath:application-%s.properties", activeProfile));
            Resource resource = resourceLoader.getResource(resolvedLocation);
            if (!resource.exists()) {
                continue;
            }
            String name = String.format("application-%s.properties", activeProfile);
            PropertySource<?> propertySource =
                defaultPropertySourceFactory.createPropertySource(name, new EncodedResource(resource));
            MutablePropertySources propertySources = ((ConfigurableEnvironment) environment).getPropertySources();
            propertySources.addFirst(propertySource);
        }

        propertySourcesPlaceholderConfigurer.setEnvironment(environment);
        return propertySourcesPlaceholderConfigurer;
    }

    private static void addDefaultPropertyFile(Environment environment, ResourceLoader resourceLoader,
                                  DefaultPropertySourceFactory defaultPropertySourceFactory) throws IOException {
        String resolvedLocation = environment.resolveRequiredPlaceholders("classpath:application.properties");
        Resource resource = resourceLoader.getResource(resolvedLocation);
        String name = "application.properties";
        PropertySource<?> propertySource =
            defaultPropertySourceFactory.createPropertySource(name, new EncodedResource(resource));
        MutablePropertySources propertySources = ((ConfigurableEnvironment) environment).getPropertySources();
        propertySources.addLast(propertySource);
    }
}
