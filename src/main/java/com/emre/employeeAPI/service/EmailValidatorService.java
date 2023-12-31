package com.emre.employeeAPI.service;

import com.emre.employeeAPI.exception.HandledException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailValidatorService {

    // just found from the internet, we might choose something more stricter..
    public static final String emailPattern = "^(.+)@(\\S+)$";



    public boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    public void validate(String email) throws HandledException {
        if (!patternMatches(email, emailPattern))
            throw new HandledException("Invalid email address");

    }
}
