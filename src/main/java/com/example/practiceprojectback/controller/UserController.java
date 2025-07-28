package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.model.User;
import com.example.practiceprojectback.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Показать профиль
    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user != null) {
            model.addAttribute("user", user);
            return "profile"; // profile.html
        } else {
            return "redirect:/login?error";
        }
    }

    // Обновить данные профиля
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("user") User updatedUser, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser != null) {
            // Обновляем имя
            sessionUser.setName(updatedUser.getName());

            // Если введён новый пароль — обновляем
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                sessionUser.setPassword(updatedUser.getPassword());
            }

            userService.updateUser(sessionUser); // обновляем в базе
            session.setAttribute("user", sessionUser); // обновляем в сессии
        }

        return "redirect:/profile";
    }

    // Выход из аккаунта
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Удаляет все данные из сессии
        return "redirect:/login"; // Возвращаем пользователя на страницу логина
    }
}
