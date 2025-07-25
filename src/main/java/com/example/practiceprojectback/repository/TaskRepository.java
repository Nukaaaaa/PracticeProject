package com.example.practiceprojectback.repository;

import com.example.practiceprojectback.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCategory(String category);

    long countByStatus(String status);

    List<Task> findTop5ByOrderByIdDesc();
}
