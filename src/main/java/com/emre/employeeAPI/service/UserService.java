package com.emre.employeeAPI.service;

import com.emre.employeeAPI.model.User;
import com.emre.employeeAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public long deleteByEmail(String email) {
        return userRepository.deleteByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

}
