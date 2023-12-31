package com.emre.employeeAPI.controller;

import com.emre.employeeAPI.annotation.PublicAccess;
import com.emre.employeeAPI.dto.input.UserLoginDto;
import com.emre.employeeAPI.dto.output.AuthResponseDto;
import com.emre.employeeAPI.dto.output.ErrorResponse;
import com.emre.employeeAPI.dto.output.UserDto;
import com.emre.employeeAPI.exception.HandledException;
import com.emre.employeeAPI.exception.NotFoundException;
import com.emre.employeeAPI.model.User;
import com.emre.employeeAPI.service.EmailValidatorService;
import com.emre.employeeAPI.service.JWTService;
import com.emre.employeeAPI.service.EmployeeService;
import com.emre.employeeAPI.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    private final EmailValidatorService emailValidatorService;

    private final JWTService jwtService;


    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    @SneakyThrows
    @PublicAccess
    @RequestMapping(value = "v1/user/register", produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST})
    public ResponseEntity<Object> register(@RequestBody UserLoginDto userLoginDto) {

        emailValidatorService.validate(userLoginDto.getEmail());

        String encodedPassword = passwordEncoder.encode(userLoginDto.getPassword());

        User user = userService.findByEmail(userLoginDto.getEmail());
        if (user != null) {
            throw new HandledException("There's an account with this email address. Try logging in with your password");
        } else {
            user = new User(0, userLoginDto.getEmail(), encodedPassword);
        }

        userService.save(user);


        String token = jwtService.createToken(userLoginDto.getEmail());
        return ResponseEntity.ok().header(
                        HttpHeaders.AUTHORIZATION,
                        token)
                .body(new AuthResponseDto(new UserDto(user)));
    }

    @SneakyThrows
    @PublicAccess
    @RequestMapping(value = "v1/user/login", produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST})
    public ResponseEntity<Object> login(@RequestBody UserLoginDto userLoginDto) {
        try {

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword()));

            User user = (User) authentication.getPrincipal();
            if (user == null) {
                throw new NotFoundException("User not found!");
            }

            String token = jwtService.createToken(userLoginDto.getEmail());
            return ResponseEntity.ok().header(
                            HttpHeaders.AUTHORIZATION,
                            token)
                    .body(new AuthResponseDto(new UserDto(user)));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Bad credentials"));
        }
    }

    @SneakyThrows
    @RequestMapping(value = "v1/user/{email}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public UserDto get(@PathVariable(value = "email") String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("User is not found");
        } else {
            return new UserDto(user);
        }
    }
}
