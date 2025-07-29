package com.example.practiceprojectback.repository;

import com.example.practiceprojectback.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository  extends JpaRepository<Tag, Long>{
    List<Tag> findByProjectId(Long projectId);
}
