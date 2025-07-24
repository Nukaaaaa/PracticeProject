package com.example.practiceprojectback.service;

import com.example.practiceprojectback.model.User;
import com.example.practiceprojectback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private  UserRepository userRepository;

    public User register(User user){
        user.setRole("USER");
        return userRepository.save(user);
    }

    public User login(String name, String password) {
        User user = userRepository.findByName(name);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
