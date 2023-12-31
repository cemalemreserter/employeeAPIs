package com.emre.employeeAPI;

import com.emre.employeeAPI.service.JWTService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerExceptionHandlerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JWTService jwtService;

    @Test
    public void testCallWithoutParameters() throws Exception {

        // send the request without the fields
        mvc.perform(post("/v1/user/register"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCallForException() throws Exception {

        String  email = "noname@gmail.com";

        mvc.perform(put("/api/v1/employees/c29b3319-f6b2-430d-8fe8-d5e4739421f2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtService.createToken(email))
                        .param("fullName", "fullName"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }
}
