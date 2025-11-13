package com.example.yadavsajam.service;

import com.example.yadavsajam.model.MembershipUser;
import com.example.yadavsajam.repository.MembershipUserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MembershipUserService {

    private final MembershipUserRepository repository;
    @Autowired
    private SmsService smsService;


    // ✅ Registration with photo upload and referral code
    public MembershipUser register(MembershipUser user, MultipartFile photoFile, String uploadDir) throws IOException {
        // Upload profile photo
        if (photoFile != null && !photoFile.isEmpty()) {
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, photoFile.getBytes());

            // 🔹 Save **relative URL**, not absolute path
            user.setProfilePhoto("/uploads/membership/" + fileName);

        }

        // Generate unique membership number if not set
        if (user.getMembershipNumber() == null || user.getMembershipNumber().isEmpty()) {
            String uniquePart = String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit random
            user.setMembershipNumber("71" + uniquePart);
        }

        // 🆕 Generate unique referral code if not set
        if (user.getReferralCode() == null || user.getReferralCode().isEmpty()) {
            user.setReferralCode(generateReferralCode());
        }

        user.setApproved(true);
        user.setPaymentDone(false);
        user.setStatus("REGISTERED");
        user.setRole("MEMBER");

        return repository.save(user);
    }

    // ✅ Generate unique referral code (8-character alphanumeric)
    private String generateReferralCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
    }

 

    // ✅ Generate OTP
 // ✅ Generate OTP (improved)
    public String generateOtp(MembershipUser user) {
        String otp = OtpUtil.generateOtp();
        boolean smsSent = smsService.sendOtpSms(user.getPhone(), otp);
        
        if (!smsSent) {
            throw new RuntimeException("Failed to send OTP to " + user.getPhone());
        }

        user.setOtp(otp);
        user.setOtpGeneratedAtMillis(System.currentTimeMillis());
        repository.save(user);

        System.out.println("[OTP] Generated for MembershipUser " + user.getPhone() + ": " + otp);
        return otp;
    }

    public boolean verifyOtpForUser(MembershipUser user, String otp) {
        return OtpUtil.isOtpValid(user.getOtp(), user.getOtpGeneratedAtMillis(), otp);
    }

    // ✅ Save user
    public MembershipUser save(MembershipUser user) {
        return repository.save(user);
    }

  
    
 // ✅ Update membership (with optional profile photo)
    public MembershipUser updateMembership(MembershipUser updatedUser, MultipartFile photoFile, String uploadDir) throws IOException {
        MembershipUser existing = repository.findById(updatedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + updatedUser.getId()));

        existing.setFullName(updatedUser.getFullName());
        existing.setEmail(updatedUser.getEmail());
        existing.setPhone(updatedUser.getPhone());
        existing.setState(updatedUser.getState());
        existing.setMembershipPlan(updatedUser.getMembershipPlan());
        existing.setReferralCode(updatedUser.getReferralCode());
        existing.setStatus(updatedUser.getStatus());

        // Update profile photo if provided
        if (photoFile != null && !photoFile.isEmpty()) {
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, photoFile.getBytes());

            existing.setProfilePhoto("/uploads/membership/" + fileName);
        }

        return repository.save(existing);
    }



    // ✅ Find by ID (new)
    public Optional<MembershipUser> findById(Long id) {
        return repository.findById(id);
    }

    // ✅ Find by phone (already in your code)
    public Optional<MembershipUser> findByPhone(String phone) {
        return repository.findByPhone(phone);
    }

    // ✅ Get all members (already used in /all)
    public List<MembershipUser> findAll() {
        return repository.findAll();
    }

    // ✅ Update membership (new or updated method)
    public MembershipUser updateMembership(MembershipUser updatedUser) {
        MembershipUser existing = repository.findById(updatedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + updatedUser.getId()));

        // Update allowed fields
        existing.setFullName(updatedUser.getFullName());
        existing.setEmail(updatedUser.getEmail());
        existing.setPhone(updatedUser.getPhone());
        existing.setState(updatedUser.getState());
        existing.setMembershipPlan(updatedUser.getMembershipPlan());
        existing.setReferralCode(updatedUser.getReferralCode());
        existing.setStatus(updatedUser.getStatus());

        return repository.save(existing);
    }

    // ✅ Delete membership
    public void deleteMembership(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        repository.deleteById(id);
    }


}
