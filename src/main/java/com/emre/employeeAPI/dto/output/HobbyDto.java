package com.emre.employeeAPI.dto.output;

import com.emre.employeeAPI.model.Hobby;
import lombok.Getter;

import java.util.UUID;


@Getter
public class HobbyDto {
    private long id;



    private String name;

    private UUID employeeId;

    public  HobbyDto(Hobby h)
    {
        this.id = h.getId();
        this.name = h.getName();
        this.employeeId = h.getEmployee().getId();

    }
}
