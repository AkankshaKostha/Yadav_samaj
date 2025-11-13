package com.example.yadavsajam.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "matrimonial_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatrimonialUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String phone;
    private String email;
    private LocalDate dob;

    private String gender;
    private String education;
    private String profession;
    private String about;
    private String photoFileName;

    private String status = "PENDING";   // Default
  
    private Boolean membershipPaid = false;
    private String membershipPlan; 
    private String role = "USER";
    private Integer membershipAmount;

    private String fatherName;
    private String motherName;
    @Column(columnDefinition = "BIT(1)")
    private Boolean approved = false;

    @Column(columnDefinition = "BIT(1)")
    private Boolean paymentDone = false;
    private int brothers;
    private int sisters;
    private String address;
    private String city;
    private String state;
    private String country;
    private double income;
    private String maritalStatus;
    private String motherTongue;
    private String hobbies;
    private String religion;
    private String caste;
    private String diet;
    private String lifestyle;
    private double height;
    private double weight;
    private String bloodGroup;
    private String uploadDir = "uploads/";
    private boolean paid;
    private String paymentId; // Add this below paymentDone
    private Integer paymentAmount; // relative path
  // "1_MONTH", "1_YEAR", "LIFETIME"
    private LocalDate membershipExpiryDate; 
    private String currentOtp;
    private Long otpGeneratedAtMillis;
}
