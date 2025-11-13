package com.example.yadavsajam.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "membership_users")
public class MembershipUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String fullName;
    private String gender;
    private String email;
    @Column(nullable = false, unique = true)
    private String phone;

    private String dob;
    private String address;
    private String district;
    private String state;
    private String pincode;
    private String assemblyConstituency;
    private String referralPhone;
    private String referredBy;
    private String profilePhoto; // store path on disk

    private String membershipPlan;
    private String status;
    private String role;
    private Boolean approved;
    private Boolean paymentDone;
    private String paymentId;
    private String membershipNumber;
    private Integer membershipAmount;
    private String referralCode;
    // OTP login support
    private String otp;
   
   
    private Long otpGeneratedAtMillis;
}
