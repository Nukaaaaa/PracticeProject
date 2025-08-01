package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.model.Comment;
import com.example.practiceprojectback.model.Task;
import com.example.practiceprojectback.model.User;
import com.example.practiceprojectback.service.CommentService;
import com.example.practiceprojectback.service.TaskService;
import com.example.practiceprojectback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final TaskService taskService;
    private final UserService userService;

    // 📌 Просмотр комментариев к задаче
    @GetMapping("/task/{taskId}")
    public String listComments(@PathVariable Long taskId,
                               Model model,
                               Authentication authentication) {
        Task task = taskService.getTaskById(taskId);
        List<Comment> comments = commentService.getCommentsByTask(taskId);

        model.addAttribute("task", task);
        model.addAttribute("comments", comments);
        model.addAttribute("comment", new Comment()); // форма добавления

        if (authentication != null) {
            User currentUser = userService.findByName(authentication.getName());
            model.addAttribute("user", currentUser);
            model.addAttribute("role", currentUser.getRole());
        } else {
            model.addAttribute("user", null);
            model.addAttribute("role", "GUEST");
        }

        return "comments";
    }

    // 📌 Добавить комментарий
    @PostMapping("/task/{taskId}")
    public String addComment(@PathVariable Long taskId,
                             @ModelAttribute Comment comment,
                             Authentication authentication) {
        Task task = taskService.getTaskById(taskId);
        User currentUser = userService.findByName(authentication.getName());
        comment.setTask(task);
        comment.setAuthor(currentUser); // ✅ фикс: сохраняем автора

        commentService.addComment(comment);
        return "redirect:/comments/task/" + taskId;
    }

    // 📌 Удалить комментарий
    @PostMapping("/{id}/delete")
    public String deleteComment(@PathVariable Long id,
                                @RequestParam Long taskId,
                                Authentication authentication) {
        User currentUser = userService.findByName(authentication.getName());
        Comment comment = commentService.getCommentById(id); // ✅ вместо списка берем один

        // Проверка прав
        if ("ADMIN".equals(currentUser.getRole()) ||
                (comment.getAuthor() != null && comment.getAuthor().getId().equals(currentUser.getId()))) {
            commentService.deleteComment(id);
        }

        return "redirect:/comments/task/" + taskId;
    }
}

