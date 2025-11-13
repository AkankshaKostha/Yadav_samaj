package com.example.yadavsajam.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;
    private String type; // "membership" | "matrimonial"
    private String razorpayOrderId;   
    private String razorpayPaymentId; 
    private Double amount;
    private String status; // PENDING | SUCCESS | FAILED
    private LocalDateTime createdAt = LocalDateTime.now();
}
