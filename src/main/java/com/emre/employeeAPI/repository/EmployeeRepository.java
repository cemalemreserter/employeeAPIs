package com.emre.employeeAPI.repository;

import com.emre.employeeAPI.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends PagingAndSortingRepository<Employee, UUID> {

    Optional<Employee> findById(String UUID);
    Page<Employee> findAll(Pageable pageable);
    List<Employee> findAll();
    Optional<Employee> findByEmail(String email);
    void deleteByEmail(String email);


}
