package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.model.Comment;
import com.example.practiceprojectback.model.Task;
import com.example.practiceprojectback.service.CommentService;
import com.example.practiceprojectback.service.TaskService;
import lombok.RequiredArgsConstructor;
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

    // 📌 Просмотр комментариев к задаче
    @GetMapping("/task/{taskId}")
    public String listComments(@PathVariable Long taskId, Model model) {
        Task task = taskService.getTaskById(taskId);
        List<Comment> comments = commentService.getCommentsByTask(taskId);

        model.addAttribute("task", task);
        model.addAttribute("comments", comments);
        model.addAttribute("comment", new Comment()); // форма добавления
        return "comments"; // comment/list.html
    }

    // 📌 Добавить комментарий
    @PostMapping("/task/{taskId}")
    public String addComment(@PathVariable Long taskId, @ModelAttribute Comment comment) {
        Task task = taskService.getTaskById(taskId);
        comment.setTask(task);
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
