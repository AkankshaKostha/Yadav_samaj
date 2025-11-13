package com.example.yadavsajam.service;

import com.example.yadavsajam.model.Donation;
import com.example.yadavsajam.repository.DonationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class DonationService {

    private final DonationRepository repository;

    public DonationService(DonationRepository repository) {
        this.repository = repository;
    }

    public Donation save(Donation donation) {
        return repository.save(donation);
    }

    public List<Donation> getAll() {
        return repository.findAll();
    }

    public String generateOrderId() {
        return "ORDER_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public String generatePaymentId() {
        return "PAY_" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    public Optional<Donation> findById(Long id) {
        return repository.findById(id);
    }
    public Optional<Donation> findByPaymentId(String paymentId) {
        return repository.findByPaymentId(paymentId);
    }

}

