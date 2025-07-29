package com.example.practiceprojectback.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "task_tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;


    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
