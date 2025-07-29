package com.example.practiceprojectback.repository;

import com.example.practiceprojectback.model.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumnRepository  extends JpaRepository<Column, Long> {
    List<Column> findByProjectId(Long project_id);

}
