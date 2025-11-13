package com.example.yadavsajam.service;

import com.example.yadavsajam.model.SupportMessage;
import com.example.yadavsajam.repository.SupportMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupportMessageService {
    private final SupportMessageRepository repo;

    public SupportMessageService(SupportMessageRepository repo) {
        this.repo = repo;
    }

    public List<SupportMessage> getAllMessages() {
        return repo.findAll();
    }

    public Optional<SupportMessage> getMessageById(Long id) {
        return repo.findById(id);
    }

    public SupportMessage save(SupportMessage msg) {
        return repo.save(msg);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
