package com.emre.employeeAPI.dto.output;

import com.emre.employeeAPI.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String email;


    public UserDto(User user) {
        this.email = user.getEmail();
    }

}
