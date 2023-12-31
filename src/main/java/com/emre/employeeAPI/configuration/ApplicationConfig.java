package com.emre.employeeAPI.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class ApplicationConfig implements IApplicationConfig {

    @Value("${jwt.secret}")
    protected String jwtSecret;

    @Value("${jwt.issuer}")
    protected String jwtIssuer;

    @Value("${spring.kafka.template.default-topic}")
    protected String kafkaTopicNameForEmployeeEvent;



    @Value("#{new Long('${authTokenExpireSeconds}')}")
    protected long authTokenExpireSeconds;
}
