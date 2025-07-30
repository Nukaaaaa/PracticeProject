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
    public String listComments(@PathVariable Long taskId, Model model) {
        Task task = taskService.getTaskById(taskId);
        List<Comment> comments = commentService.getCommentsByTask(taskId);

        model.addAttribute("task", task);
        model.addAttribute("comments", comments);
        model.addAttribute("comment", new Comment()); // форма добавления
        return "comments";
    }

    // 📌 Добавить комментарий
    @PostMapping("/task/{taskId}")
    public String addComment(@PathVariable Long taskId,
                             @ModelAttribute Comment comment,
                             Authentication authentication) {
        Task task = taskService.getTaskById(taskId);
        User currentUser = userService.findByName(authentication.getName()); // ✅ находим текущего пользователя
        comment.setTask(task);
        comment.setAuthor(currentUser); // ✅ устанавливаем автора

        commentService.addComment(comment);
        return "redirect:/comments/task/" + taskId;
    }

    // 📌 Удалить комментарий
    @PostMapping("/{id}/delete")
    public String deleteComment(@PathVariable Long id, @RequestParam Long taskId) {
        commentService.deleteComment(id);
        return "redirect:/comments/task/" + taskId;
    }
}
