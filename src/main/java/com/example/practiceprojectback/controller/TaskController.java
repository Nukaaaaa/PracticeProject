package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.model.Task;
import com.example.practiceprojectback.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public String showTasksPage(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);

        // Список категорий
        List<String> categories = List.of("Work", "Study", "Personal");
        model.addAttribute("categories", categories);

        model.addAttribute("task", new Task()); // Для формы создания
        return "tasks"; // tasks.html
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
    @PostMapping("/update-status/{id}")
    @ResponseBody
    public String updateTaskStatus(@PathVariable Long id, @RequestParam("status") String status) {
        taskService.updateTaskStatus(id, status);
        return "OK";
    }
    @PostMapping("/status/{id}")
    @ResponseBody
    public String updateStatus(@PathVariable Long id, @RequestParam String status) {
        Task task = taskService.getTaskById(id);
        task.setStatus(status);
        taskService.updateTask(id, task);
        return "OK";
    }

}
