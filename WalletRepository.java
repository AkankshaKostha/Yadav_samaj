package com.example.yadavsajam.repository;

import com.example.yadavsajam.model.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletTransaction, Long> {}
