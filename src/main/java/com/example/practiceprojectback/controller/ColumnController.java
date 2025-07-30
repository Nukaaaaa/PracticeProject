package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.model.Column;
import com.example.practiceprojectback.model.Project;
import com.example.practiceprojectback.service.ColumnService;
import com.example.practiceprojectback.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/columns")
public class ColumnController {

    private final ColumnService columnService;
    private final ProjectService projectService;

    // 📌 Добавить колонку
    @PostMapping("/project/{projectId}")
    public String createColumn(@PathVariable Long projectId,
                               @RequestParam String name,
                               @RequestParam(required = false) Integer wipLimit) {
        Project project = projectService.getProjectById(projectId);

        Column column = new Column();
        column.setName(name);
        column.setProject(project);
        column.setWipLimit(wipLimit != null ? String.valueOf(wipLimit) : null);

        columnService.createColumn(column);

        return "redirect:/projects/" + projectId + "/board";
    }


    // 📌 Удалить колонку
    @PostMapping("/{id}/delete")
    public String deleteColumn(@PathVariable Long id, @RequestParam Long projectId) {
        columnService.deleteColumn(id);
        return "redirect:/projects/" + projectId + "/board";
    }
}
