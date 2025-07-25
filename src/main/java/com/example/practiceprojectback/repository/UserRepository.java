package com.example.practiceprojectback.repository;

import com.example.practiceprojectback.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
     User findByName(String name);

     long countByRole(String role); // ✅ для countUsersByRole

     List<User> findTop5ByOrderByIdDesc(); // ✅ для getTop5Users
}

