package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.model.Task;
import com.example.practiceprojectback.model.User;
import com.example.practiceprojectback.repository.TagRepository;
import com.example.practiceprojectback.repository.UserRepository;
import com.example.practiceprojectback.service.ColumnService;
import com.example.practiceprojectback.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final ColumnService columnService;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    // ✅ Создание задачи
    @PostMapping
    public String createTask(@ModelAttribute Task task,
                             @RequestParam Long columnId,
                             @RequestParam(required = false) List<Long> tagIds,
                             @RequestParam(required = false) Long userId,
                             Authentication authentication) {

        User user = null;

        // ✅ Если админ выбрал пользователя
        if (userId != null) {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        }
        // ✅ Иначе берем текущего
        else if (authentication != null) {
            user = userRepository.findByEmail(authentication.getName());
        }

        // ✅ Если и тут user == null → выкидываем ошибку
        if (user == null) {
            throw new RuntimeException("Не удалось определить пользователя для задачи!");
        }

        task.setUser(user);

        Task savedTask = taskService.createTask(task, columnId, tagIds);

        return "redirect:/projects/" + savedTask.getColumn().getProject().getId() + "/board";
    }


    // ✅ Форма редактирования задачи
    @GetMapping("/edit/{id}")
    public String showEditTaskForm(@PathVariable Long id, Model model, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        Task task = taskService.getTaskById(id);

        if (!"ADMIN".equals(user.getRole()) && !task.getUser().getId().equals(user.getId())) {
            return "redirect:/projects/" + task.getColumn().getProject().getId() + "/board?error=forbidden";
        }

        Long projectId = task.getColumn().getProject().getId();
        model.addAttribute("columns", columnService.getColumnsByProject(projectId));
        model.addAttribute("tags", tagRepository.findByProjectId(projectId));
        model.addAttribute("task", task);
        return "edit-task";
    }

    // ✅ Обновление задачи
    @PostMapping("/edit/{id}")
    public String updateTask(@PathVariable Long id,
                             @ModelAttribute Task updatedTask,
                             @RequestParam(required = false) List<Long> tagIds,
                             Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        Task existingTask = taskService.getTaskById(id);

        if (!"ADMIN".equals(user.getRole()) && !existingTask.getUser().getId().equals(user.getId())) {
            return "redirect:/projects/" + existingTask.getColumn().getProject().getId() + "/board?error=forbidden";
        }

        Task savedTask = taskService.updateTask(id, updatedTask, tagIds);
        Long projectId = savedTask.getColumn().getProject().getId();

        return "redirect:/projects/" + projectId + "/board";
    }

    // ✅ Удаление задачи
    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
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

    @GetMapping("/reports/project/{projectId}")
    public String getProjectReport(@PathVariable Long projectId, Model model) {
        Map<String, Long> columnStats = taskService.countTasksByColumns(projectId);
        model.addAttribute("columnStats", columnStats);
        model.addAttribute("totalTasks", columnStats.values().stream().mapToLong(Long::longValue).sum());
        return "admin/reports :: projectReport"; // Возвращаем фрагмент из Thymeleaf шаблона
    }


}
