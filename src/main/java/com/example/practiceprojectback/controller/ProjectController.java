package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.model.Project;
import com.example.practiceprojectback.model.User;
import com.example.practiceprojectback.service.ProjectService;
import com.example.practiceprojectback.service.ColumnService;
import com.example.practiceprojectback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ColumnService columnService;
    private final UserService userService;

    // 📌 Список всех проектов
    @GetMapping
    public String listProjects(Model model) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        model.addAttribute("project", new Project()); // для формы создания
        return "project/list"; // project/list.html
    }

    // 📌 Kanban-доска проекта
    @GetMapping("/{id}/board")
    public String projectBoard(@PathVariable Long id, Model model) {
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        model.addAttribute("columns", columnService.getColumnsByProject(id));
        return "project/board"; // project/board.html
    }

    // 📌 Создать проект с привязкой к текущему пользователю
    @PostMapping
    public String createProject(@ModelAttribute Project project) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // получаем логин
        User currentUser = userService.findByName(username); // ищем пользователя в базе

        project.setOwner(currentUser);
        projectService.createProject(project);

        return "redirect:/projects";
    }

    // 📌 Удалить проект
    @PostMapping("/{id}/delete")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return "redirect:/projects";
    }
}
