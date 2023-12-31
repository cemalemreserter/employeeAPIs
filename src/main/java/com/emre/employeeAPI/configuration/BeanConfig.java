package com.emre.employeeAPI.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public Faker faker() {
        return new Faker();
    }

    @Bean
    public ApplicationConfig applicationConfig() {
        return new ApplicationConfig();
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
