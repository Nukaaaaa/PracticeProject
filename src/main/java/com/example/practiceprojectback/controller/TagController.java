package com.example.practiceprojectback.controller;

import com.example.practiceprojectback.model.Tag;
import com.example.practiceprojectback.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("/project/{projectId}")
    public List<Tag> getTagsByProject(@PathVariable Long projectId) {
        return tagService.getTagsByProject(projectId);
    }

    @PostMapping
    public Tag createTag(@RequestBody Tag tag) {
        return tagService.createTag(tag);
    }

    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
    }
}
