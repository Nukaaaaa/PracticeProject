package com.example.practiceprojectback.repository;

import com.example.practiceprojectback.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.LongFunction;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
//    List<Task> findByCategory(String category);


    List<Task> findByUserId(Long userId);
//    List<Task>findByCategoryAndUserId(String category, Long UserId);
//    long countByStatus(String status);

    List<Task> findTop5ByOrderByIdDesc();
    long countByColumnId(Long columnId);
}
