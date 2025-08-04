package com.example.practiceprojectback.service;

import com.example.practiceprojectback.model.User;
import com.example.practiceprojectback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(User user) {
        user.setRole("USER");


        if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email уже используется");
        }

        // хэшируем пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }
}
