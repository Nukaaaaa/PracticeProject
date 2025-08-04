package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.model.User;
import com.example.practiceprojectback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // Показать профиль
    @GetMapping
    public String profile(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.findByEmail(userDetails.getUsername());
            model.addAttribute("user",user);
            return "profile"; // profile.html
        } else {
            return "redirect:/login?error";
        }
    }

    // Обновить профиль
    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("user") User updatedUser,
                                Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.findByEmail(userDetails.getUsername());

            // обновляем имя
            user.setName(updatedUser.getName());

            // если введён новый пароль — шифруем
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            userService.updateUser(user);
        }
        return "redirect:/profile?success";
    }
}
