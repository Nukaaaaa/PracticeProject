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
import java.util.List;

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

    // ✅ Создание задачи с проверкой WIP и тегами
    public Task createTask(Task task, Long columnId, List<Long> tagIds) {
        Column column = columnRepository.findById(columnId)
                .orElseThrow(() -> new RuntimeException("Column not found with id: " + columnId));

        // Проверка WIP лимита
        if (column.getWipLimit() != null) {
            long taskCount = taskRepository.countByColumnId(columnId);
            if (taskCount >= column.getWipLimit()) {
                throw new RuntimeException("Лимит задач в колонке достигнут!");
            }
        }

        task.setColumn(column);
        Task savedTask = taskRepository.save(task);

        // Привязка тегов
        if (tagIds != null) {
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
    public Task updateTask(Long id, Task updatedTask) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());

        if (updatedTask.getColumn() != null) {
            Column column = columnRepository.findById(updatedTask.getColumn().getId())
                    .orElseThrow(() -> new RuntimeException("Column not found"));
            task.setColumn(column);
        }

        return taskRepository.save(task);
    }


    public void deleteTask(Long id) {
        taskTagRepository.deleteByTaskId(id);
        taskRepository.deleteById(id);
    }

    // ✅ Перенос задачи в другую колонку
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
}
