package com.example.yadavsajam.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "donations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double amount;
    private String message;
    private String paymentId;
    private String orderId;
    private String paymentMethod;
    private Boolean paid = false;
}
