package com.example.yadavsajam.controller;

import com.example.yadavsajam.model.HappyStory;
import com.example.yadavsajam.service.HappyStoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/happy-stories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class HappyStoryController {

    private final HappyStoryService happyStoryService;

    // 📁 Folder where uploaded images are stored
    private static final String UPLOAD_DIR = "E:/Akanksha/Yadav_samaj/yadavsajam/uploads/happy-stories/";

    // ✅ GET all stories
    @GetMapping
    public List<HappyStory> getAllStories() {
        return happyStoryService.getAllStories();
    }

    // ✅ GET story by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getStoryById(@PathVariable Long id) {
        Optional<HappyStory> story = happyStoryService.getStoryById(id);
        return story.isPresent()
                ? ResponseEntity.ok(story.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Story not found"));
    }

    // ✅ ADD new story (with optional image)
    @PostMapping(value = "/add", consumes = {"multipart/form-data"})
    public ResponseEntity<?> addStory(
            @RequestPart("coupleName") String coupleName,
            @RequestPart("message") String message,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        try {
            String photoUrl = null;

            // 🖼️ Save image if uploaded
            if (photo != null && !photo.isEmpty()) {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
                File dest = new File(uploadDir, fileName);
                photo.transferTo(dest);

                photoUrl = "/uploads/happy-stories/" + fileName;
            }

            HappyStory story = new HappyStory();
            story.setCoupleName(coupleName);
            story.setMessage(message);
            story.setPhotoUrl(photoUrl);
            story.setApproved(true); // Default approved by admin
            story.setStatus("ACTIVE");

            HappyStory saved = happyStoryService.saveStory(story);
            return ResponseEntity.ok(saved);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to upload story: " + e.getMessage()));
        }
    }

    // ✅ UPDATE story
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStory(
            @PathVariable Long id,
            @RequestBody HappyStory updatedStory
    ) {
        Optional<HappyStory> existing = happyStoryService.getStoryById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Story not found"));
        }

        HappyStory story = existing.get();
        story.setCoupleName(updatedStory.getCoupleName());
        story.setMessage(updatedStory.getMessage());
        story.setApproved(updatedStory.getApproved());
        story.setStatus(updatedStory.getStatus());
        story.setPhotoUrl(updatedStory.getPhotoUrl());

        return ResponseEntity.ok(happyStoryService.saveStory(story));
    }

    // ✅ DELETE story
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStory(@PathVariable Long id) {
        Optional<HappyStory> story = happyStoryService.getStoryById(id);
        if (story.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Story not found"));
        }

        happyStoryService.deleteStory(id);
        return ResponseEntity.ok(Map.of("message", "Story deleted successfully"));
    }
}
