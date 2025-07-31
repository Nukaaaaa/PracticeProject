package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.model.Task;
import com.example.practiceprojectback.model.User;
import com.example.practiceprojectback.repository.UserRepository;
import com.example.practiceprojectback.service.ColumnService;
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
    private final ColumnService columnService;
    private final UserRepository userRepository;

    // ✅ Создание задачи
    @PostMapping
    public String createTask(@ModelAttribute Task task,
                             @RequestParam Long columnId,
                             @RequestParam(required = false) List<Long> tagIds,
                             Authentication authentication) {
        User user = userRepository.findByName(authentication.getName());
        task.setUser(user);

        Task savedTask = taskService.createTask(task, columnId, tagIds);

        return "redirect:/projects/" + savedTask.getColumn().getProject().getId() + "/board";
    }

    // ✅ Форма редактирования задачи
    @GetMapping("/edit/{id}")
    public String showEditTaskForm(@PathVariable Long id, Model model, Authentication authentication) {
        User user = userRepository.findByName(authentication.getName());
        Task task = taskService.getTaskById(id);

        if (!"ADMIN".equals(user.getRole()) && !task.getUser().getId().equals(user.getId())) {
            return "redirect:/projects/" + task.getColumn().getProject().getId() + "/board?error=forbidden";
        }

        // ✅ получаем все колонки проекта задачи
        Long projectId = task.getColumn().getProject().getId();
        model.addAttribute("columns", columnService.getColumnsByProject(projectId));

        model.addAttribute("task", task);
        return "edit-task";
    }


    // ✅ Обновление задачи
    @PostMapping("/edit/{id}")
    public String updateTask(@PathVariable Long id,
                             @ModelAttribute Task updatedTask,
                             Authentication authentication) {
        User user = userRepository.findByName(authentication.getName());

        Task existingTask = taskService.getTaskById(id);

        if (!"ADMIN".equals(user.getRole()) && !existingTask.getUser().getId().equals(user.getId())) {
            return "redirect:/projects/" + existingTask.getColumn().getProject().getId() + "/board?error=forbidden";
        }

        Task savedTask = taskService.updateTask(id, updatedTask);

        // ✅ вместо того чтобы дергать у Task → Column → Project
        // берем projectId напрямую через колонку из базы
        Long projectId = savedTask.getColumn()
                .getProject()
                .getId();

        return "redirect:/projects/" + projectId + "/board";
    }


    // ✅ Удаление задачи
    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id, Authentication authentication) {
        User user = userRepository.findByName(authentication.getName());
        Task task = taskService.getTaskById(id);

        if (!"ADMIN".equals(user.getRole()) && !task.getUser().getId().equals(user.getId())) {
            return "redirect:/projects/" + task.getColumn().getProject().getId() + "/board?error=forbidden";
        }

        Long projectId = task.getColumn().getProject().getId();
        taskService.deleteTask(id);

        return "redirect:/projects/" + projectId + "/board";
    }

    // ✅ Drag & Drop
    @PostMapping("/{id}/move")
    @ResponseBody
    public String moveTask(@PathVariable Long id, @RequestParam Long columnId) {
        taskService.moveTaskToColumn(id, columnId);
        return "OK";
    }
}
