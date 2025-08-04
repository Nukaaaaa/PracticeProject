package com.example.practiceprojectback.service;

import com.example.practiceprojectback.model.Column;
import com.example.practiceprojectback.model.Tag;
import com.example.practiceprojectback.model.Task;
import com.example.practiceprojectback.model.TaskTag;
import com.example.practiceprojectback.repository.ColumnRepository;
import com.example.practiceprojectback.repository.TagRepository;
import com.example.practiceprojectback.repository.TaskRepository;
import com.example.practiceprojectback.repository.TaskTagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ColumnRepository columnRepository;
    private final TagRepository tagRepository;
    private final TaskTagRepository taskTagRepository;

    public TaskService(TaskRepository taskRepository,
                       ColumnRepository columnRepository,
                       TagRepository tagRepository,
                       TaskTagRepository taskTagRepository) {
        this.taskRepository = taskRepository;
        this.columnRepository = columnRepository;
        this.tagRepository = tagRepository;
        this.taskTagRepository = taskTagRepository;
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    // ✅ Создание задачи с тегами
    @Transactional
    public Task createTask(Task task, Long columnId, List<Long> tagIds) {
        Column column = columnRepository.findById(columnId)
                .orElseThrow(() -> new RuntimeException("Column not found with id: " + columnId));

        if (column.getWipLimit() != null) {
            long taskCount = taskRepository.countByColumnId(columnId);
            if (taskCount >= column.getWipLimit()) {
                throw new RuntimeException("Лимит задач в колонке достигнут!");
            }
        }

        task.setColumn(column);
        Task savedTask = taskRepository.save(task);

        // Привязка тегов
        if (tagIds != null && !tagIds.isEmpty()) {
            for (Long tagId : tagIds) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new RuntimeException("Tag not found with id: " + tagId));
                TaskTag taskTag = new TaskTag();
                taskTag.setTask(savedTask);
                taskTag.setTag(tag);
                taskTagRepository.save(taskTag);
            }
        }

        return savedTask;
    }

    // ✅ Обновление задачи + теги
    @Transactional
    public Task updateTask(Long id, Task updatedTask, List<Long> tagIds) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());

        if (updatedTask.getColumn() != null) {
            Column column = columnRepository.findById(updatedTask.getColumn().getId())
                    .orElseThrow(() -> new RuntimeException("Column not found"));
            task.setColumn(column);
        }

        // Удаляем старые связи тегов
        taskTagRepository.deleteByTaskId(task.getId());

        // Добавляем новые теги
        if (tagIds != null && !tagIds.isEmpty()) {
            for (Long tagId : tagIds) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new RuntimeException("Tag not found with id: " + tagId));
                TaskTag taskTag = new TaskTag();
                taskTag.setTask(task);
                taskTag.setTag(tag);
                taskTagRepository.save(taskTag);
            }
        }

        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskTagRepository.deleteByTaskId(id);
        taskRepository.deleteById(id);
    }

    // ✅ Перенос задачи в другую колонку
    @Transactional
    public void moveTaskToColumn(Long taskId, Long columnId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));
        Column column = columnRepository.findById(columnId)
                .orElseThrow(() -> new RuntimeException("Колонка не найдена"));

        if (column.getWipLimit() != null) {
            long taskCount = taskRepository.countByColumnId(columnId);
            if (taskCount >= column.getWipLimit()) {
                throw new RuntimeException("Лимит задач в колонке достигнут!");
            }
        }

        task.setColumn(column);
        taskRepository.save(task);
    }
    public Map<String, Long> countTasksByColumns(Long projectId) {
        List<Column> columns = columnRepository.findByProjectId(projectId);
        Map<String, Long> stats = new LinkedHashMap<>();
        for (Column col : columns) {
            long count = taskRepository.countByColumnId(col.getId());
            stats.put(col.getName(), count);
        }
        return stats;
    }
}
