package com.example.practiceprojectback.model;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String email;
    private String name;
    private String password;
    private String role;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
    private List<Project> projects;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Task> tasks;
}
