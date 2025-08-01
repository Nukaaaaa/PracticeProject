package com.example.practiceprojectback.repository;

import com.example.practiceprojectback.model.TaskTag;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskTagRepository extends JpaRepository<TaskTag, Long> {
    List<TaskTag> findByTaskId(Long taskId);

    @Modifying
    @Transactional
    @Query("DELETE FROM TaskTag tt WHERE tt.task.id = :taskId")
    void deleteByTaskId(Long taskId);
}
