package com.emre.employeeAPI.repository;

import com.emre.employeeAPI.model.Hobby;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HobbyRepository extends CrudRepository<Hobby, Long> {
    Hobby save(Hobby Hobby) ;
}