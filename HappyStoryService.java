package com.example.yadavsajam.service;

import com.example.yadavsajam.model.HappyStory;
import com.example.yadavsajam.repository.HappyStoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HappyStoryService {

    private final HappyStoryRepository repo;

    // ✅ Return all stories
    public List<HappyStory> getAllStories() {
        return repo.findAll();
    }

    // ✅ Get one story by ID
    public Optional<HappyStory> getStoryById(Long id) {
        return repo.findById(id);
    }

    // ✅ Save or update
    public HappyStory saveStory(HappyStory story) {
        return repo.save(story);
    }

    // ✅ Delete a story
    public void deleteStory(Long id) {
        repo.deleteById(id);
    }

    // ✅ Count stories (for dashboard)
    public long getStoryCount() {
        return repo.count();
    }
    
    
}
