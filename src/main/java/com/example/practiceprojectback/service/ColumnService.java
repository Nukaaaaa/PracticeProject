package com.example.practiceprojectback.service;

import com.example.practiceprojectback.model.Column;
import com.example.practiceprojectback.repository.ColumnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColumnService {
    private final ColumnRepository columnRepository;

    public List<Column> getColumnsByProject(Long projectId) {
        return columnRepository.findByProjectId(projectId);
    }

    public Column createColumn(Column column) {
        return columnRepository.save(column);
    }

    public Column updateColumn(Long id, Column updatedColumn) {
        Column column = columnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Column not found with id: " + id));
        column.setName(updatedColumn.getName());
        column.setWipLimit(updatedColumn.getWipLimit());
        return columnRepository.save(column);
    }



    public void deleteColumn(Long id) {
        columnRepository.deleteById(id);
    }
}
