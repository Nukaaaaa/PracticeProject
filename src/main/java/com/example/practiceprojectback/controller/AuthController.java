package com.example.practiceprojectback.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model; // ✅ правильный импорт
import com.example.practiceprojectback.model.User;
import com.example.practiceprojectback.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String loginForm() {
        return "login"; // login.html
    }

    @PostMapping("/login")
    public String loginForm(@RequestParam String name,
                            @RequestParam String password,
                            HttpSession session) {
        User user = authService.login(name, password);
        if (user != null) {
            session.setAttribute("user", user); // сохранить пользователя в сессии

            // Проверка роли и редирект
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                return "redirect:/admin/dashboard"; // ⚠️ путь к админ-панели
            } else {
                return "redirect:/tasks";
            }
        }
        return "login"; // снова login.html при неудаче
    }



    @GetMapping("register")
    public String registerForm() {
        return "register"; // register.html
    }

    @PostMapping("register")
    public String register(@RequestParam String name, @RequestParam String password) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setRole("User");
        authService.register(user);
        return "redirect:/login";
    }
}
