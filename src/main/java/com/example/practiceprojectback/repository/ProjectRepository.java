package com.example.practiceprojectback.repository;

import com.example.practiceprojectback.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository  extends JpaRepository<Project, Long> {
    List<Project> findByOwnerId(Long owmerId);
}