package com.emre.employeeAPI.dto.input;

import com.emre.employeeAPI.model.Hobby;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveEmployeeDto {


    private String fullName;


    private String email;

    private Date birthDay;


    private List<Hobby> hobbies;


}
