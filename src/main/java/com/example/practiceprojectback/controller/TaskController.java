package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.model.Task;
import com.example.practiceprojectback.model.User;
import com.example.practiceprojectback.service.TaskService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public String showTasksPage(@RequestParam(value = "category", required = false) String category,
                                Model model,
                                HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }
        List<String> statuses = List.of("CREATED", "IN_PROGRESS", "DONE");

        List<Task> tasks;
        if ("ADMIN".equals(user.getRole())) {
            // Админ видит все задачи
            tasks = (category != null && !category.isEmpty())
                    ? taskService.getTasksByCategory(category)
                    : taskService.getAllTasks();
        } else {
            // Юзер видит только свои
            tasks = (category != null && !category.isEmpty())
                    ? taskService.getTasksByCategoryAndUserId(category, user.getId())
                    : taskService.getTasksByUserId(user.getId());
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("categories", List.of("Work", "Study", "Personal"));
        model.addAttribute("selectedCategory", category);
        model.addAttribute("task", new Task());
        model.addAttribute("user", user); // ✅ добавляем user в модель
        model.addAttribute("role", user.getRole());
        model.addAttribute("statuses",statuses);

        return "tasks";
    }

    @GetMapping("/edit/{id}")
    public String showEditTaskForm(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Task task = taskService.getTaskById(id);

        // Проверка: юзер может редактировать только свои задачи
        if (!"ADMIN".equals(user.getRole()) && !task.getUser().getId().equals(user.getId())) {
            return "redirect:/tasks?error=forbidden";
        }

        model.addAttribute("categories", List.of("Work", "Study", "Personal"));
        model.addAttribute("task", task);
        return "edit-task";
    }

    @PostMapping
    public String createTask(@ModelAttribute Task task, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
    }

        task.setUser(user); // Привязываем задачу к пользователю
        taskService.createTask(task);
        return "redirect:/tasks";
    }

    @PostMapping("/edit/{id}")
    public String updateTask(@PathVariable Long id,
                             @ModelAttribute Task updatedTask,
                             HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Task task = taskService.getTaskById(id);

        // Проверка прав
        if (!"ADMIN".equals(user.getRole()) && !task.getUser().getId().equals(user.getId())) {
            return "redirect:/tasks?error=forbidden";
        }

        taskService.updateTask(id, updatedTask);
        return "redirect:/tasks";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Task task = taskService.getTaskById(id);

        // Проверка прав
        if (!"ADMIN".equals(user.getRole()) && !task.getUser().getId().equals(user.getId())) {
            return "redirect:/tasks?error=forbidden";
        }

        taskService.deleteTask(id);
        return "redirect:/tasks";
    }


    @PostMapping("/status/{id}")
    @ResponseBody
    public String updateTaskStatus(@PathVariable Long id,
                                   @RequestParam("status") String status,
                                   HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Task task = taskService.getTaskById(id);

        if (!"ADMIN".equals(user.getRole()) && !task.getUser().getId().equals(user.getId())) {
            return "FORBIDDEN";
        }

        taskService.updateTaskStatus(id, status);
        return "OK";
    }
}
