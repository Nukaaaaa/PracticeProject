package com.example.practiceprojectback.service;

import com.example.practiceprojectback.model.User;
import com.example.practiceprojectback.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }


    public User updateAdminUser(Long id, User updatedUser){
        User user = getUserById(id);
        user.setName(updatedUser.getName());
        user.setRole(updatedUser.getRole());
        return userRepository.save(user);
    }
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    // ✅ Подсчет всех пользователей
    public long countUsers() {
        return userRepository.count();
    }

    // ✅ Подсчет по роли
    public long countUsersByRole(String role){
        return userRepository.countByRole(role);
    }

    // ✅ Последние 5 пользователей
    public List<User> getTop5Users() {
        return userRepository.findTop5ByOrderByIdDesc();
    }

    // ✅ Обновление только роли пользователя
    public User updateUserRole(Long id, String newRole) {
        User user = getUserById(id);
        user.setRole(newRole);
        return userRepository.save(user);
    }
}
