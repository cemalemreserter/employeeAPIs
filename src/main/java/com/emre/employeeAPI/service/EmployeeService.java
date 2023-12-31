package com.emre.employeeAPI.service;

import com.emre.employeeAPI.configuration.IApplicationConfig;
import com.emre.employeeAPI.dto.input.SaveEmployeeDto;
import com.emre.employeeAPI.dto.output.EmployeeDto;
import com.emre.employeeAPI.model.Employee;
import com.emre.employeeAPI.model.Hobby;
import com.emre.employeeAPI.model.User;
import com.emre.employeeAPI.repository.EmployeeRepository;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.emre.employeeAPI.constants.AppConstants.PAGING_PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class EmployeeService {



    private final EmployeeRepository employeeRepository;


    private final HobbyService hobbyService;
    private final UserService userService;

    private final IApplicationConfig applicationConfig;


    public Optional<Employee> findByUUID(UUID id) {
        return employeeRepository.findById(id);
    }

    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    public List<Employee> findAll()
    {
        return employeeRepository.findAll();
    }

    public List<Employee> findAll(int page)
    {
        if(page != -1) {
            Pageable pageable = PageRequest.of(page, PAGING_PAGE_SIZE);
            return employeeRepository.findAll(pageable).getContent();
        }
        else { return  employeeRepository.findAll();
        }
    }

    public void deleteEmployee(UUID id)
    {
        employeeRepository.deleteById(id);
    }

    public void deleteByEmail(String email)
    {
        employeeRepository.deleteByEmail(email);
    }


    @Transactional
    public void updateEmployee(SaveEmployeeDto saveEmployeeDto, Employee employee) {

            employee.setBirthDay(saveEmployeeDto.getBirthDay());
            employee.setEmail(saveEmployeeDto.getEmail());
            employee.setFullName(saveEmployeeDto.getFullName());
            saveEmployeeDto.getHobbies().stream().forEach(h -> h.setEmployee(employee));

            List<Hobby> hobbies = saveEmployeeDto.getHobbies();

            employee.setHobbies(hobbies);

            hobbyService.saveAll(hobbies);

            employeeRepository.save(employee);

    }

    @Transactional
    public EmployeeDto createEmployee(SaveEmployeeDto saveEmployeeDto) {

        Employee employee = new Employee();
        employee.setBirthDay(saveEmployeeDto.getBirthDay());
        employee.setEmail(saveEmployeeDto.getEmail());
        employee.setFullName(saveEmployeeDto.getFullName());
        employeeRepository.save(employee);
        if (saveEmployeeDto.getHobbies() != null)
            saveEmployeeDto.getHobbies().stream().forEach(h -> h.setEmployee(employee));

        List<Hobby> hobbies = saveEmployeeDto.getHobbies();

        employee.setHobbies(hobbies);

        hobbyService.saveAll(hobbies);

        employeeRepository.save(employee);

        return new EmployeeDto(employee);
    }


}
