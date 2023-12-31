package com.emre.employeeAPI.dto.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthResponseDto {

    private com.emre.employeeAPI.dto.output.UserDto user;
}
