package com.example.yadavsajam.service;

import com.example.yadavsajam.model.WalletTransaction;
import com.example.yadavsajam.repository.WalletRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WalletService {
    private final WalletRepository repo;

    public WalletService(WalletRepository repo) {
        this.repo = repo;
    }

    public List<WalletTransaction> getAll() {
        return repo.findAll();
    }

    public WalletTransaction addTransaction(WalletTransaction tx) {
        return repo.save(tx);
    }

    public double getTotalBalance() {
        List<WalletTransaction> all = repo.findAll();
        return all.stream().mapToDouble(t -> 
            t.getType().equalsIgnoreCase("CREDIT") ? t.getAmount() : -t.getAmount()
        ).sum();
    }
}
