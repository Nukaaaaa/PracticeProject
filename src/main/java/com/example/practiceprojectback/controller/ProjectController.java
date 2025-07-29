package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.model.Project;
import com.example.practiceprojectback.model.Column;
import com.example.practiceprojectback.model.Task;
import com.example.practiceprojectback.service.ProjectService;
import com.example.practiceprojectback.service.ColumnService;
import com.example.practiceprojectback.service.TaskService;
import lombok.RequiredArgsConstructor;
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
    private final TaskService taskService;

    // 📌 Список всех проектов
    @GetMapping
    public String listProjects(Model model) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        model.addAttribute("project", new Project()); // для формы создания
        return "project/list"; // project/list.html
    }
    @GetMapping("/{id}/board")
    public String showBoard(@PathVariable Long id, Model model) {
        Project project = projectService.getProjectById(id);
        List<Column> columns = columnService.getColumnsByProject(id);

        model.addAttribute("project", project);
        model.addAttribute("columns", columns);

        return "board";
    }


    // 📌 Создать проект
    @PostMapping
    public String createProject(@ModelAttribute Project project) {
        projectService.createProject(project);
        return "redirect:/projects";
    }

    // 📌 Kanban-доска проекта
    @GetMapping("/{id}/board")
    public String projectBoard(@PathVariable Long id, Model model) {
        Project project = projectService.getProjectById(id);
        List<Column> columns = columnService.getColumnsByProject(id);

        model.addAttribute("project", project);
        model.addAttribute("columns", columns);
        return "project/board"; // project/board.html
    }

    // 📌 Удалить проект
    @PostMapping("/{id}/delete")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return "redirect:/projects";
    }
}
