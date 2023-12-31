package com.emre.employeeAPI.dto.output;

import lombok.Getter;

@Getter
public class ErrorResponse {

    public ErrorOutput error;

    public ErrorResponse(String message) {
        this.error = new ErrorOutput(message);
    }
}
