package com.emre.employeeAPI.dto.output;

import com.emre.employeeAPI.model.Employee;
import com.emre.employeeAPI.model.Hobby;
import com.emre.employeeAPI.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

    private UUID id;


    private String fullName;


    private String email;

    @JsonFormat(pattern="YYYY-MM-DD")
    private Date birthDay;


    private List<HobbyDto> hobbies;

    public EmployeeDto(Employee employee) {
        this.id = employee.getId();
        this.fullName = employee.getFullName();
        this.birthDay = employee.getBirthDay();
        this.email = employee.getEmail();
        this.hobbies = new ArrayList<>();
        for (Hobby h : employee.getHobbies()) {
            this.hobbies.add(new HobbyDto(h));
        }
    }

}
