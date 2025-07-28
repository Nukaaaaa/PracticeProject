package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.model.User;
import com.example.practiceprojectback.service.TaskService;
import com.example.practiceprojectback.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final TaskService taskService;

    public AdminController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/dashboard")
    public String adminHome() {
        return "admin/dashboard";
    }

    // Показать всех пользователей
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    // Форма редактирования пользователя
    @GetMapping("/users/{id}/edit")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        return "admin/edit-user";
    }

    // Обновить роль пользователя
    @PostMapping("/users/{id}/update-role")
    public String updateUserRole(@PathVariable Long id, @RequestParam String role) {
        userService.updateUserRole(id, role);
        return "redirect:/admin/users";
    }

    // Удалить пользователя
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    // Отчет
    @GetMapping("/reports")
    public String showReports(Model model) {
        model.addAttribute("totalTasks", taskService.countAllTasks());
        model.addAttribute("completedTasks", taskService.countTasksByStatus("DONE"));
        model.addAttribute("inProgressTasks", taskService.countTasksByStatus("IN_PROGRESS"));
        model.addAttribute("openTasks", taskService.countTasksByStatus("CREATED"));


        model.addAttribute("totalUsers", userService.countUsers());
        model.addAttribute("adminCount", userService.countUsersByRole("ADMIN"));
        model.addAttribute("userCount", userService.countUsersByRole("USER"));
        model.addAttribute("recentUsers", userService.getTop5Users());



        return "admin/reports";
    }

}
