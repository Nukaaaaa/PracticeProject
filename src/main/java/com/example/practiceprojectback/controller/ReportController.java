package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.service.TaskService;
import com.example.practiceprojectback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

    private final TaskService taskService;
    private final UserService userService;

    // Общий отчёт (статистика по задачам, пользователям и проектам)
    @GetMapping
    public String showReports(Model model) {
        // Пример: если есть методы в taskService для подсчёта задач по статусам
//        model.addAttribute("totalTasks", taskService.countAllTasks());
//        model.addAttribute("completedTasks", taskService.countTasksByStatus("DONE"));
//        model.addAttribute("inProgressTasks", taskService.countTasksByStatus("IN_PROGRESS"));
//        model.addAttribute("openTasks", taskService.countTasksByStatus("CREATED"));

//        model.addAttribute("totalTasks", taskService.countAllTasks()); // или 0, если нет методов

        model.addAttribute("totalUsers", userService.countUsers());
        model.addAttribute("adminCount", userService.countUsersByRole("ADMIN"));
        model.addAttribute("userCount", userService.countUsersByRole("USER"));
        model.addAttribute("recentUsers", userService.getTop5Users());

//        model.addAttribute("projects", taskService.getProjectsWithTaskCounts()); // Метод, который возвращает список проектов с общей статистикой (название, id, количество задач и т.п.)

        return "admin/reports";
    }

    // Отчёт по колонкам конкретного проекта (возвращается Thymeleaf фрагмент)
    @GetMapping("/project/{projectId}")
    public String getProjectReport(@PathVariable Long projectId, Model model) {
        Map<String, Long> columnStats = taskService.countTasksByColumns(projectId);
        model.addAttribute("columnStats", columnStats);
        model.addAttribute("totalTasks", columnStats.values().stream().mapToLong(Long::longValue).sum());
        return "admin/reports :: projectReport"; // фрагмент из reports.html
    }
}
