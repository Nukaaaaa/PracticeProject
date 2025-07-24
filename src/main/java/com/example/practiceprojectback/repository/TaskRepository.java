package com.example.practiceprojectback.repository;

import com.example.practiceprojectback.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskRepository extends JpaRepository <Task, Long> {
}
