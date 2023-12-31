package com.emre.employeeAPI.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class JsonConverter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public String convertToString(Object o){
        return objectMapper.writeValueAsString(o);
    }

}
