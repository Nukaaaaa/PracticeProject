package com.example.practiceprojectback.service;

import com.example.practiceprojectback.model.User;
import com.example.practiceprojectback.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByName(String name){
        return Optional.ofNullable(userRepository.findByName(name));
    }
}

