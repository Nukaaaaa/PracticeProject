package com.example.practiceprojectback.service;

import com.example.practiceprojectback.model.Task;
import com.example.practiceprojectback.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
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
}
