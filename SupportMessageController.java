package com.example.yadavsajam.controller;

import com.example.yadavsajam.model.SupportMessage;
import com.example.yadavsajam.service.SupportMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/support")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SupportMessageController {

    private final SupportMessageService service;

    @GetMapping
    public List<SupportMessage> getAll() {
        List<SupportMessage> messages = service.getAllMessages();
        messages.sort(Comparator.comparing(SupportMessage::getCreatedAt).reversed());
        return messages;
    }

    @PostMapping
    public SupportMessage addMessage(@RequestBody SupportMessage msg) {
        msg.setResolved(false);
        msg.setCreatedAt(java.time.LocalDateTime.now());
        return service.save(msg);
    }

    @PutMapping("/{id}/reply")
    public ResponseEntity<?> replyToMessage(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return service.getMessageById(id)
                .map(msg -> {
                    msg.setReply(body.get("reply"));
                    msg.setResolved(true);
                    return ResponseEntity.ok(service.save(msg));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(Map.of("message", "Deleted successfully"));
    }
}
