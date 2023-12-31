package com.emre.employeeAPI.controller;

import com.emre.employeeAPI.constants.EventType;
import com.emre.employeeAPI.dto.input.SaveEmployeeDto;
import com.emre.employeeAPI.dto.output.EmployeeDto;
import com.emre.employeeAPI.exception.ControllerExceptionHandler;
import com.emre.employeeAPI.exception.HandledException;
import com.emre.employeeAPI.exception.NotAuthorizedException;
import com.emre.employeeAPI.exception.NotFoundException;
import com.emre.employeeAPI.model.Employee;
import com.emre.employeeAPI.model.User;
import com.emre.employeeAPI.producer.KafkaEmployeeEventProducerService;
import com.emre.employeeAPI.service.EmailValidatorService;
import com.emre.employeeAPI.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Convert;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class EmployeeController {

    @Autowired
    private final EmployeeService employeeService;

    private final EmailValidatorService emailValidatorService;

    @Autowired
    private final KafkaEmployeeEventProducerService kafkaEmployeeEventProducerService;


    @SneakyThrows
    @RequestMapping(value = "v1/employees/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.GET})
    public EmployeeDto getEmployee(Authentication authentication, @PathVariable(value = "id") UUID id) {

        User user = (User) authentication.getPrincipal();
        if(!(user != null && authentication.isAuthenticated())) {
            throw new NotAuthorizedException("Not Authorized");
        }
        Optional<Employee> employee = employeeService.findByUUID(id);
        if (!employee.isPresent())
            throw new NotFoundException("Employee with id " + id + " is not found.");

        return new EmployeeDto(employee.get());
    }


    /// <summary>
    /// Retrieves employee records with offset pagination, you need to provide page number value (default pageSize : 20)
    /// For first 20 records set page to 0
    /// </summary>
    @SneakyThrows
    @RequestMapping(value = "v1/employees/page/{page}", produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.GET})
    public List<EmployeeDto> getAllEmployees(Authentication authentication,
                                       @PathVariable(value = "page") int page)  {

        User user = (User) authentication.getPrincipal();
        if(!(user != null && authentication.isAuthenticated())) {
            throw new NotAuthorizedException("Not Authorized");
        }

        List<Employee> employees = employeeService.findAll(page);


        if (employees != null && employees.size() > 0) {
            List<EmployeeDto> employeeDtos = new ArrayList<>();
            employees.stream().forEach(emp -> employeeDtos.add(new EmployeeDto(emp)));
            return employeeDtos;
        }
        else
            throw new NotFoundException("No Employee found.");
    }

    @SneakyThrows
    @RequestMapping(value = "v1/employees/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.PUT})
    public void updateEmployee(Authentication authentication,
                                             @PathVariable(value = "id") UUID id,
                                             @RequestBody SaveEmployeeDto saveEmployeeDto)  {

        User user = (User) authentication.getPrincipal();
        if(!(user != null && authentication.isAuthenticated())) {
            throw new NotAuthorizedException("Not Authorized");
        }

        if (saveEmployeeDto != null && !StringUtils.isEmpty(saveEmployeeDto.getEmail())) {
            emailValidatorService.validate(saveEmployeeDto.getEmail());


            Optional<Employee> employee = employeeService.findByUUID(id);
            if (employee.isPresent()) {
                employeeService.updateEmployee(saveEmployeeDto, employee.get());
                kafkaEmployeeEventProducerService.sendTransactionListMessage(EventType.UPDATE_EMPLOYEE.getName() +" with employee id : " + id.toString());
            }
        }
    }

    @SneakyThrows
    @RequestMapping(value = "v1/employees/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.DELETE})
    public void deleteEmployee(Authentication authentication,
                               @PathVariable(value = "id") UUID id) {

        User user = (User) authentication.getPrincipal();
        if (!(user != null && authentication.isAuthenticated())) {
            throw new NotAuthorizedException("Not Authorized");
        }

        if (id != null) {
            employeeService.deleteEmployee(id);
            kafkaEmployeeEventProducerService.sendTransactionListMessage(EventType.DELETE_EMPLOYEE.getName() +" with employee id : " + id.toString());
        }
    }


    @SneakyThrows
    @RequestMapping(value = "v1/employees", produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST})
    public EmployeeDto createEmployee(Authentication authentication,
                                      @RequestBody SaveEmployeeDto saveEmployeeDto) {

        User user = (User) authentication.getPrincipal();
        if (!(user != null && authentication.isAuthenticated())) {
            throw new NotAuthorizedException("Not Authorized");
        }

        if (saveEmployeeDto != null && !StringUtils.isEmpty(saveEmployeeDto.getEmail())) {
            emailValidatorService.validate(saveEmployeeDto.getEmail());
            if(employeeService.findByEmail(saveEmployeeDto.getEmail()).isPresent())
                throw new HandledException("An employee has been using the email : " + saveEmployeeDto.getEmail());

            EmployeeDto employeeDto = employeeService.createEmployee(saveEmployeeDto);
            kafkaEmployeeEventProducerService.sendTransactionListMessage(EventType.CREATE_EMPLOYEE.getName() +" with employee id : " + employeeDto.getId().toString());
            return  employeeDto;
        }

        throw new HandledException("Could not be created");

    }
}
