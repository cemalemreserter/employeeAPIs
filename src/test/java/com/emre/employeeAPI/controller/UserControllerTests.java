package com.emre.employeeAPI.controller;

import com.emre.employeeAPI.dto.input.UserLoginDto;
import com.emre.employeeAPI.dto.output.AuthResponseDto;
import com.emre.employeeAPI.model.User;
import com.emre.employeeAPI.service.EmployeeService;
import com.emre.employeeAPI.service.UserService;
import com.emre.employeeAPI.util.JsonConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JsonConverter jsonConverter;

    @Test
    public void testRegisterSuccessfully() throws Exception {

        String email = "noname@gmail.com";
        String password = passwordEncoder.encode("password");
        UserLoginDto userLoginDto = new UserLoginDto(email, password);

        userService.deleteByEmail(email);

        String responseString = mvc.perform(post("/v1/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convertToString(userLoginDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AuthResponseDto authResponseDto = objectMapper.readValue(responseString, AuthResponseDto.class);
        // assert authResponseDto.getToken() != null;
        assert authResponseDto.getUser().getEmail().equals(email);

        assert userService.findByEmail(email) != null;
    }

    @Test
    public void testRegisterWithInvalidEmailAddress() throws Exception {

        String email = "cemalemre.com";
        String password = passwordEncoder.encode("password");
        UserLoginDto userLoginDto = new UserLoginDto(email, password);

        userService.deleteByEmail(email);

        mvc.perform(post("/v1/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convertToString(userLoginDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":{\"message\":\"Invalid email address\"}}"));
    }

    @Test
    public void testRegisterBySameEmail() throws Exception {

        String email = "noname@gmail.com";
        String password = passwordEncoder.encode("password");
        UserLoginDto userLoginDto = new UserLoginDto(email, password);

        userService.deleteByEmail(email);
        userService.save(new User(email, password));

        mvc.perform(post("/v1/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convertToString(userLoginDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":{\"message\":\"There's an account with this email address. Try logging in with your password\"}}"));
    }

    @Test
    public void testSuccessfulLogin() throws Exception {

        String email = "noname@gmail.com\"";
        String password = "password";
        UserLoginDto userLoginDto = new UserLoginDto(email, password);

        userService.deleteByEmail(email);
        User user = new User(email, passwordEncoder.encode(password));
        userService.save(user);



        String responseString = mvc.perform(post("/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convertToString(userLoginDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AuthResponseDto authResponseDto = objectMapper.readValue(responseString, AuthResponseDto.class);
        // assert authResponseDto.getToken() != null;
        assert authResponseDto.getUser().getEmail().equals(email);
    }

    @Test
    public void testLoginWithWrongPassword() throws Exception {

        String email = "bbb@bb.com";
        String password = "password1";
        UserLoginDto userLoginDto = new UserLoginDto(email, password + "2323");

        userService.deleteByEmail(email);
        userService.save(new User(email, passwordEncoder.encode(password)));

        mvc.perform(post("/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convertToString(userLoginDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":{\"message\":\"Bad credentials\"}}"));
    }
}
