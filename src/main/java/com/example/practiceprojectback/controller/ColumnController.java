package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.model.Column;
import com.example.practiceprojectback.service.ColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/columns")
@RequiredArgsConstructor
public class ColumnController {
    private final ColumnService columnService;

    @GetMapping("/project/{projectId}")
    public List<Column> getColumns(@PathVariable Long projectId) {
        return columnService.getColumnsByProject(projectId);
    }

    @PostMapping
    public Column createColumn(@RequestBody Column column) {
        return columnService.createColumn(column);
    }

    @PutMapping("/{id}")
    public Column updateColumn(@PathVariable Long id, @RequestBody Column column) {
        return columnService.updateColumn(id, column);
    }

    @DeleteMapping("/{id}")
    public void deleteColumn(@PathVariable Long id) {
        columnService.deleteColumn(id);
    }
}
