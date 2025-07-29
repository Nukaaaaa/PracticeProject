package com.example.practiceprojectback.service;

import com.example.practiceprojectback.model.Column;
import com.example.practiceprojectback.model.Task;
import com.example.practiceprojectback.repository.ColumnRepository;
import com.example.practiceprojectback.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ColumnRepository columnRepository;

    public TaskService(TaskRepository taskRepository, ColumnRepository columnRepository) {
        this.taskRepository = taskRepository;
        this.columnRepository = columnRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    public List<Task> getTasksByCategoryAndUserId(String category, Long userId) {
        return  taskRepository.findByCategoryAndUserId(category, userId);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setCategory(updatedTask.getCategory());
        task.setStatus(updatedTask.getStatus());
        return taskRepository.save(task);
    }

    public void updateTaskStatus(Long id, String status) {
        Task task = getTaskById(id);
        task.setStatus(status);
        taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getTasksByCategory(String category) {
        return taskRepository.findByCategory(category);
    }

    // 🔥 Для отчёта:
    public long countAllTasks() {
        return taskRepository.count();
    }

    public long countTasksByStatus(String status) {
        return taskRepository.countByStatus(status);
    }

    public List<Task> findTop5Tasks() {
        return taskRepository.findTop5ByOrderByIdDesc();
    }



    public void moveTaskToColumn(Long taskId, Long columnId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));
        Column column = columnRepository.findById(columnId)
                .orElseThrow(() -> new RuntimeException("Колонка не найдена"));

        task.setColumn(column);
        taskRepository.save(task);
    }
}
