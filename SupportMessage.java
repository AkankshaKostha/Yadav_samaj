package com.example.yadavsajam.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String subject;
    
    @Column(length = 2000)
    private String message;

    private String reply;
    private boolean resolved = false;

    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();
}
