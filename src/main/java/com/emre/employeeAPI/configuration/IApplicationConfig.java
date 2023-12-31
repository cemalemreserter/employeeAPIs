package com.emre.employeeAPI.configuration;

public interface IApplicationConfig {

    String getJwtIssuer();

    String getJwtSecret();

    String getKafkaTopicNameForEmployeeEvent();

    long getAuthTokenExpireSeconds();
}
