package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.model.Column;
import com.example.practiceprojectback.service.ColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/columns")
public class ColumnController {

    private final ColumnService columnService;

    // 📌 Добавить колонку
    @PostMapping("/project/{projectId}")
    public String createColumn(@PathVariable Long projectId, @ModelAttribute Column column) {
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
