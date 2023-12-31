package com.emre.employeeAPI.service;

import com.emre.employeeAPI.model.Hobby;
import com.emre.employeeAPI.repository.HobbyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HobbyService {

    private final HobbyRepository hobbyRepository;

    public Optional<Hobby> findById(long Id) {
        return hobbyRepository.findById(Id);
    }

    public void save(Hobby h){
        hobbyRepository.save(h);
    }

    public void saveAll(List<Hobby> hobbies) {
        hobbyRepository.saveAll(hobbies);
    }
}
