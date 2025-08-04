package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.model.User;
import com.example.practiceprojectback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginForm() {
        return "login"; // login.html
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register"; // register.html
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model) {
        if(userRepository.existsByEmail(email)){
            model.addAttribute("error","Этот Email уже используется");
            return "register";
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER"); // роль по умолчанию
        userRepository.save(user);
        return "redirect:/login";
    }
}
