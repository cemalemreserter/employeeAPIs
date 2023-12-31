package com.emre.employeeAPI.exception;

public class NotAuthorizedException extends Exception{

    public NotAuthorizedException(String message) {
        super(message);
    }
}
