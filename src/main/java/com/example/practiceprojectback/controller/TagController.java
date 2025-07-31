package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.model.Project;
import com.example.practiceprojectback.model.Tag;
import com.example.practiceprojectback.service.ProjectService;
import com.example.practiceprojectback.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final ProjectService projectService;

    // 📌 Список тегов проекта
    @GetMapping("/project/{projectId}")
    public String listTags(@PathVariable Long projectId, Model model) {
        List<Tag> tags = tagService.getTagsByProject(projectId);


        Project project = projectService.getProjectById(projectId);
        model.addAttribute("tags", tags);
        model.addAttribute("tag", new Tag()); // форма добавления
        model.addAttribute("projectId", projectId);
        model.addAttribute("project",project);
        return "tags"; // tag/list.html
    }

    // 📌 Добавить тег
    @PostMapping("/project/{projectId}")
    public String createTag(@PathVariable Long projectId, @ModelAttribute Tag tag) {
        Project project = projectService.getProjectById(projectId);
        tag.setProject(project); // <-- устанавливаем проект
        tagService.createTag(tag);
        return "redirect:/tags/project/" + projectId;
    }

    // 📌 Удалить тег
    @PostMapping("/{id}/delete")
    public String deleteTag(@PathVariable Long id, @RequestParam Long projectId) {
        tagService.deleteTag(id);
        return "redirect:/tags/project/" + projectId;
    }
}
