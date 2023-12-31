package com.emre.employeeAPI.controller;

import com.emre.employeeAPI.configuration.IApplicationConfig;
import com.emre.employeeAPI.dto.input.SaveEmployeeDto;
import com.emre.employeeAPI.dto.output.EmployeeDto;
import com.emre.employeeAPI.model.Employee;
import com.emre.employeeAPI.model.User;
import com.emre.employeeAPI.service.JWTService;
import com.emre.employeeAPI.service.EmployeeService;
import com.emre.employeeAPI.service.UserService;
import com.emre.employeeAPI.util.JsonConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static com.emre.employeeAPI.constants.AppConstants.DATE_FORMAT_PATTERN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IApplicationConfig applicationConfig;

    @Autowired
    private Faker faker;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JsonConverter jsonConverter;

    @Test

    public void getEmployee() throws Exception {

        String email = "noname3@gmail.com";
        String password = passwordEncoder.encode("password");

        userService.deleteByEmail(email);
        userService.save(new User(email, password));

        SaveEmployeeDto saveEmployeeDto = new SaveEmployeeDto();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);

            saveEmployeeDto.setBirthDay(sdf.parse("1990-11-11"));
        }catch (ParseException pe)
        {}
        finally {
            saveEmployeeDto.setEmail("qwerq@qeq.com");
            saveEmployeeDto.setFullName("full name");
            saveEmployeeDto.setHobbies(new ArrayList<>(){});
        }


        employeeService.deleteByEmail("qwerq@qeq.com");
        EmployeeDto employeeDto = employeeService.createEmployee(saveEmployeeDto);

        String responseString = mvc.perform(get("/api/v1/employees/" + employeeDto.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtService.createToken(email)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        EmployeeDto employeeDto1 = objectMapper.readValue(responseString, EmployeeDto.class);
        assert employeeDto1.getId().equals(employeeDto.getId());
    }

    @Test
    public void notFoundEmployee() throws Exception {

        String email = "noname3@gmail.com";
        String password = passwordEncoder.encode("password");

        userService.deleteByEmail(email);
        userService.save(new User(email, password));



        UUID employeeId = UUID.randomUUID();
        mvc.perform(get("/api/v1/employees/" + employeeId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtService.createToken(email)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":{\"message\":\"Employee with id " + employeeId + " is not found.\"}}"));
    }

    @Test
    public void updateEmployee() throws Exception {

        String email = "noname7@gmail.com";
        String password = passwordEncoder.encode("password");
        String updatedName = faker.lorem().fixedString(20);


        userService.deleteByEmail(email);
        userService.save(new User(email, password));

        User user = userService.findByEmail(email);
        SaveEmployeeDto saveEmployeeDto = new SaveEmployeeDto();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);

            saveEmployeeDto.setBirthDay(sdf.parse("1990-11-11"));
        }catch (ParseException pe)
        {}
        finally {
            saveEmployeeDto.setEmail("qwerq@qeq.com");
            saveEmployeeDto.setFullName(updatedName);
            saveEmployeeDto.setHobbies(new ArrayList<>(){});
        }
        EmployeeDto employeeDto = employeeService.createEmployee(saveEmployeeDto);

        mvc.perform(put("/api/v1/employees/" + employeeDto.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtService.createToken(email))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convertToString(saveEmployeeDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());



        assert employeeDto.getFullName().equals(updatedName);

    }



    @Test
    public void updateEmployeeEmailOnly() throws Exception {

        String email = "noname7@gmail.com";
        String password = passwordEncoder.encode("password");
        String updatedName = faker.lorem().fixedString(20);


        userService.deleteByEmail(email);
        userService.save(new User(email, password));

        User user = userService.findByEmail(email);
        SaveEmployeeDto saveEmployeeDto = new SaveEmployeeDto();
        saveEmployeeDto.setEmail("trying@qeq.com");
        saveEmployeeDto.setFullName("full name name");
        saveEmployeeDto.setHobbies(new ArrayList<>(){});
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);

            saveEmployeeDto.setBirthDay(sdf.parse("1990-11-11"));
        }catch (ParseException pe)
        {}
        EmployeeDto employeeDto = employeeService.createEmployee(saveEmployeeDto);
        Optional<Employee> employee = employeeService.findByEmail("trying@qeq.com");
        //update email  ***
        String uptodateEmail = "gmailgmail@llll.com";
        saveEmployeeDto.setEmail(uptodateEmail);


        employeeService.updateEmployee(saveEmployeeDto, employee.get());

        mvc.perform(put("/api/v1/employees/" + employeeDto.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtService.createToken(email))
                        .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.convertToString(saveEmployeeDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());


        assert saveEmployeeDto.getEmail().equals(employeeService.findByEmail(uptodateEmail).get().getEmail());
    }


    @Test
    public void createWithAlreadyExistingEmail() throws Exception {

        String email = "noname8@gmail.com";
        String password = passwordEncoder.encode("password");
        String updatedName = faker.lorem().fixedString(20);


        userService.deleteByEmail(email);
        userService.save(new User(email, password));

        User user = userService.findByEmail(email);
        SaveEmployeeDto saveEmployeeDto = new SaveEmployeeDto();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);

            saveEmployeeDto.setBirthDay(sdf.parse("1988-07-18"));
        }catch (ParseException pe)
        {}
        finally {
            saveEmployeeDto.setEmail("qwerq@qeq.com");
            saveEmployeeDto.setFullName(updatedName);
            saveEmployeeDto.setHobbies(new ArrayList<>(){});
        }
        EmployeeDto employeeDto = employeeService.createEmployee(saveEmployeeDto);

        mvc.perform(post("/api/v1/employees" )
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtService.createToken(email))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convertToString(saveEmployeeDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest()).andReturn()
                .getResponse()
                .getContentAsString()
                .contains("An employee has been using the email : " + employeeDto.getEmail());




        assert employeeDto.getEmail().equals(saveEmployeeDto.getEmail());
    }


}