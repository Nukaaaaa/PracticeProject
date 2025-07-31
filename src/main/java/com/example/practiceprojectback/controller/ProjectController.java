package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.model.Project;
import com.example.practiceprojectback.model.User;
import com.example.practiceprojectback.service.ProjectService;
import com.example.practiceprojectback.service.ColumnService;
import com.example.practiceprojectback.service.TagService;
import com.example.practiceprojectback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
    private final TagService tagService;

    // 📌 Список всех проектов
    @GetMapping
    public String listProjects(Model model, Authentication authentication) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        model.addAttribute("project", new Project()); // для формы создания

        if (authentication != null) {
            User currentUser = userService.findByName(authentication.getName());
            model.addAttribute("role", currentUser.getRole()); // ✅ передаем роль
            model.addAttribute("user", currentUser);
        } else {
            model.addAttribute("role", "USER"); // по умолчанию
        }

        return "project/list"; // project/list.html
    }

    // 📌 Kanban-доска проекта
    @GetMapping("/{id}/board")
    public String projectBoard(@PathVariable Long id, Model model, Authentication authentication) {
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        model.addAttribute("columns", columnService.getColumnsByProject(id));
        model.addAttribute("tags",tagService.getTagsByProject(id));

        if (authentication != null) {
            User currentUser = userService.findByName(authentication.getName());
            model.addAttribute("role", currentUser.getRole()); // ✅ роль в модель
            model.addAttribute("user", currentUser);
        } else {
            model.addAttribute("role", "USER");
        }

        return "project/board"; // project/board.html
    }

    // 📌 Создать проект с привязкой к текущему пользователю
    @PostMapping
    public String createProject(@ModelAttribute Project project, Authentication authentication) {
        if (authentication != null) {
            User currentUser = userService.findByName(authentication.getName());
            project.setOwner(currentUser); // ✅ привязываем владельца
        }
        projectService.createProject(project);
        return "redirect:/projects";
    }

    // 📌 Удалить проект (только для ADMIN)
    @PostMapping("/{id}/delete")
    public String deleteProject(@PathVariable Long id, Authentication authentication) {
        if (authentication != null) {
            User currentUser = userService.findByName(authentication.getName());
            if ("ADMIN".equals(currentUser.getRole())) { // проверка роли
                projectService.deleteProject(id);
            }
        }
        return "redirect:/projects";
    }
}
