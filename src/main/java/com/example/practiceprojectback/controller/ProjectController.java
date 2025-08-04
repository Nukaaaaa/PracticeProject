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
            // 🔍 Проверим, что вернёт Spring Security
            System.out.println("⚡ Authentication name: " + authentication.getName());

            User currentUser = userService.findByEmail(authentication.getName());
            if (currentUser != null) {
                model.addAttribute("role", currentUser.getRole());
                model.addAttribute("user", currentUser);
            } else {
                System.out.println("⚠️ Пользователь не найден по email: " + authentication.getName());
                model.addAttribute("role", "USER");
            }
        } else {
            model.addAttribute("role", "USER");
        }

        return "project/list";
    }



    // 📌 Kanban-доска проекта
    @GetMapping("/{id}/board")
    public String projectBoard(@PathVariable Long id, Model model, Authentication authentication) {
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        model.addAttribute("columns", columnService.getColumnsByProject(id));
        model.addAttribute("tags", tagService.getTagsByProject(id));

        if (authentication != null) {
            System.out.println("⚡ Authentication name: " + authentication.getName());

            User currentUser = userService.findByEmail(authentication.getName());
            if (currentUser != null) {
                model.addAttribute("role", currentUser.getRole());
                model.addAttribute("user", currentUser);
            } else {
                System.out.println("⚠️ Пользователь не найден по email: " + authentication.getName());
                model.addAttribute("role", "USER");
                model.addAttribute("user", null); // 👈 обязательно добавь
            }
        } else {
            model.addAttribute("role", "USER");
            model.addAttribute("user", null); // 👈 добавляем user в любом случае
        }

        return "project/board";
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
