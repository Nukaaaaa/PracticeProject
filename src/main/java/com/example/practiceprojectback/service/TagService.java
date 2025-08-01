package com.example.practiceprojectback.service;

import com.example.practiceprojectback.model.Tag;
import com.example.practiceprojectback.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public List<Tag> getTagsByProject(Long projectId) {
        return tagRepository.findByProjectId(projectId);
    }

    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    // удаление с каскадом
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Тег не найден"));
        tagRepository.delete(tag);
    }
}
