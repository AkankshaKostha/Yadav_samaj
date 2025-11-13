package com.example.yadavsajam.controller;

import com.example.yadavsajam.model.TeamMember;
import com.example.yadavsajam.repository.TeamMemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TeamMemberController {

    private final TeamMemberRepository teamRepository;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String UPLOAD_DIR = "E:\\Akanksha\\Yadav_samaj\\yadavsajam\\uploads\\team\\";

    // --------- Admin: Add new member ---------
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addMember(
            @RequestPart("member") String memberJson,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        try {
            TeamMember member = mapper.readValue(memberJson, TeamMember.class);

            if (photo != null && !photo.isEmpty()) {
                String filename = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + filename);
                Files.createDirectories(path.getParent());
                photo.transferTo(path.toFile());
                member.setPhotoFileName("/uploads/team/" + filename);
            }

            TeamMember saved = teamRepository.save(member);
            return ResponseEntity.ok(saved);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing request: " + e.getMessage());
        }
    }

    // --------- Admin: Update member ---------
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateMember(
            @PathVariable Long id,
            @RequestPart("member") String memberJson,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        Optional<TeamMember> optionalMember = teamRepository.findById(id);
        if (optionalMember.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");

        try {
            TeamMember existing = optionalMember.get();
            TeamMember updatedData = mapper.readValue(memberJson, TeamMember.class);

            existing.setFullName(updatedData.getFullName());
            existing.setRole(updatedData.getRole());
            existing.setEmail(updatedData.getEmail());
            existing.setPhone(updatedData.getPhone());
            existing.setAbout(updatedData.getAbout());
            existing.setCity(updatedData.getCity());
            existing.setDesignation(updatedData.getDesignation());
            existing.setActive(updatedData.getActive());

            if (photo != null && !photo.isEmpty()) {
                String filename = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + filename);
                Files.createDirectories(path.getParent());
                photo.transferTo(path.toFile());
                existing.setPhotoFileName("/uploads/team/" + filename);
            }

            return ResponseEntity.ok(teamRepository.save(existing));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing request: " + e.getMessage());
        }
    }

    // --------- Admin: Delete member ---------
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
        teamRepository.deleteById(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    // --------- Admin & User: Get all members ---------
    @GetMapping("/all")
    public List<Map<String, Object>> getAllMembers() {
        return teamRepository.findAll().stream()
                .map(m -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", m.getId());
                    map.put("fullName", m.getFullName());
                    map.put("role", m.getRole());
                    map.put("photoFileName", m.getPhotoFileName());
                    return map;
                })
                .collect(Collectors.toList());
    }

    // --------- Admin & User: Get member by ID ---------
    @GetMapping("/{id}")
    public ResponseEntity<?> getMember(@PathVariable Long id) {
        Optional<TeamMember> member = teamRepository.findById(id);
        if (member.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }
        return ResponseEntity.ok(member.get());
    }
}
