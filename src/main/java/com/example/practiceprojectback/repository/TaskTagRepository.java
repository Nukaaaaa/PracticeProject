package com.example.practiceprojectback.repository;

import com.example.practiceprojectback.model.TaskTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskTagRepository extends JpaRepository<TaskTag, Long> {
    List<TaskTag> findByTaskId(Long taskId);

    void deleteByTaskId(Long id);
}
