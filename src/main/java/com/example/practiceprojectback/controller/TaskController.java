package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.model.Task;
import com.example.practiceprojectback.model.User;
import com.example.practiceprojectback.repository.UserRepository;
import com.example.practiceprojectback.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    @GetMapping
    public String showTasksPage(@RequestParam(value = "category", required = false) String category,
                                Model model,
                                Authentication authentication) {

        String username = authentication.getName();
        User user = userRepository.findByName(username);

        List<Task> tasks;
        if ("ADMIN".equals(user.getRole())) {
            tasks = (category != null && !category.isEmpty())
                    ? taskService.getTasksByCategory(category)
                    : taskService.getAllTasks();
        } else {
            tasks = (category != null && !category.isEmpty())
                    ? taskService.getTasksByCategoryAndUserId(category, user.getId())
                    : taskService.getTasksByUserId(user.getId());
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("categories", List.of("Work", "Study", "Personal"));
        model.addAttribute("selectedCategory", category);
        model.addAttribute("task", new Task());
        model.addAttribute("user", user);
        model.addAttribute("role", user.getRole());
        model.addAttribute("statuses", List.of("CREATED", "IN_PROGRESS", "DONE"));

        return "tasks";
    }

    @PostMapping
    public String createTask(@ModelAttribute Task task,
                             @RequestParam Long columnId,
                             Authentication authentication) {
        User user = userRepository.findByName(authentication.getName());
        task.setUser(user);

        taskService.createTask(task, columnId);

        // после добавления вернуться на Kanban доску
        return "redirect:/projects/" + task.getColumn().getProject().getId() + "/board";
    }


    @GetMapping("/edit/{id}")
    public String showEditTaskForm(@PathVariable Long id, Model model, Authentication authentication) {
        User user = userRepository.findByName(authentication.getName());
        Task task = taskService.getTaskById(id);

        if (!"ADMIN".equals(user.getRole()) && !task.getUser().getId().equals(user.getId())) {
            return "redirect:/tasks?error=forbidden";
        }

        model.addAttribute("categories", List.of("Work", "Study", "Personal"));
        model.addAttribute("task", task);
        return "edit-task";
    }

    @PostMapping("/edit/{id}")
    public String updateTask(@PathVariable Long id,
                             @ModelAttribute Task updatedTask,
                             Authentication authentication) {
        User user = userRepository.findByName(authentication.getName());

        Task task = taskService.getTaskById(id);

        if (!"ADMIN".equals(user.getRole()) && !task.getUser().getId().equals(user.getId())) {
            return "redirect:/tasks?error=forbidden";
        }

        taskService.updateTask(id, updatedTask);
        return "redirect:/tasks";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id, Authentication authentication) {
        User user = userRepository.findByName(authentication.getName());

        Task task = taskService.getTaskById(id);

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
                                   Authentication authentication) {
        User user = userRepository.findByName(authentication.getName());

        Task task = taskService.getTaskById(id);

        if (!"ADMIN".equals(user.getRole()) && !task.getUser().getId().equals(user.getId())) {
            return "FORBIDDEN";
        }

        taskService.updateTaskStatus(id, status);
        return "OK";
    }

    @PostMapping("/{id}/move")
    @ResponseBody
    public String moveTask(@PathVariable Long id, @RequestParam Long columnId) {
        taskService.moveTaskToColumn(id, columnId);
        return "OK";
    }

}
