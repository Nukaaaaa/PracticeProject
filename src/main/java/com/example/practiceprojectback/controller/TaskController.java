package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.model.Task;
import com.example.practiceprojectback.service.TaskService;
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
    public String showTasksPage(@RequestParam(value = "category", required = false) String category, Model model) {
        List<Task> tasks;

        if (category != null && !category.isEmpty()) {
            tasks = taskService.getTasksByCategory(category);
            model.addAttribute("selectedCategory", category);
        } else {
            tasks = taskService.getAllTasks();
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("categories", List.of("Work", "Study", "Personal"));
        model.addAttribute("task", new Task());

        return "tasks";
    }


    @GetMapping("/edit/{id}")
    public String showEditTaskForm(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id);

        List<String> categories = List.of("Work", "Study", "Personal");
        model.addAttribute("categories", categories);

        model.addAttribute("task", task);
        return "edit-task"; // edit-task.html
    }

    @PostMapping
    public String createTask(@ModelAttribute Task task) {
        taskService.createTask(task);
        return "redirect:/tasks";
    }

    @PostMapping("/edit/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute Task updatedTask) {
        taskService.updateTask(id, updatedTask);
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }

    // REST endpoint для изменения статуса задачи (используется при drag and drop)
    @PostMapping("/status/{id}")
    @ResponseBody
    public String updateTaskStatus(@PathVariable Long id, @RequestParam("status") String status) {
        taskService.updateTaskStatus(id, status);
        return "OK";
    }
}
