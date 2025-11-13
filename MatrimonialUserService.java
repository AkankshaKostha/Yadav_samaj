package com.example.yadavsajam.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.yadavsajam.model.MatrimonialUser;
import com.example.yadavsajam.repository.MatrimonialUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatrimonialUserService {

    private final MatrimonialUserRepository repo;
    private static final long OTP_EXPIRATION_MILLIS = 5 * 60 * 1000; 
    @Autowired
    private SmsService smsService;
    
    
    public MatrimonialUser register(MatrimonialUser user, MultipartFile photoFile, String uploadDir) throws IOException {
    	
        if (user.getDob() != null) {
            LocalDate dob = LocalDate.parse(user.getDob().toString()); // ensure DOB stored as LocalDate
            int age = Period.between(dob, LocalDate.now()).getYears();
            if (age < 18) {
                throw new IllegalArgumentException("User must be at least 18 years old.");
            }
        }

        // 2. Validate Phone Number
        if (user.getPhone() == null || !user.getPhone().matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone number must be exactly 10 digits.");
        }

        // 3. Check uniqueness of phone number
        Optional<MatrimonialUser> existingUser = repo.findByPhone(user.getPhone());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Phone number is already registered.");
        }

        if (photoFile != null && !photoFile.isEmpty()) {
            String filename = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();
            File dest = new File(uploadDir, filename);
            dest.getParentFile().mkdirs();
            photoFile.transferTo(dest);
//            user.setPhotoFileName(filename);
            // ✅ Store relative path instead of raw filename
            user.setPhotoFileName("/uploads/matrimonial/" + filename);
        }
        user.setStatus("PENDING");
        user.setApproved(false);
        user.setPaymentDone(false);
        return repo.save(user);
    }
    // ---------------- Find ----------------
    public Optional<MatrimonialUser> findById(Long id) {
        return repo.findById(id);
    }

    public Optional<MatrimonialUser> findByPhone(String phone) {
        return repo.findByPhone(phone);
    }

    public MatrimonialUser save(MatrimonialUser user) {
        return repo.save(user);
    }

    public List<MatrimonialUser> getAllUsers() {
        return repo.findAll();
    }
 


    // ---------------- OTP ----------------
 // ---------------- OTP (improved) ----------------
    public String generateOtp(MatrimonialUser user) {
        String otp = OtpUtil.generateOtp();
        
        boolean smsSent = smsService.sendOtpSms(user.getPhone(), otp);
        if (!smsSent) {
            throw new RuntimeException("Failed to send OTP to " + user.getPhone());
        }

        user.setCurrentOtp(otp);
        user.setOtpGeneratedAtMillis(System.currentTimeMillis());
        repo.save(user);

        System.out.println("[OTP] Generated for MatrimonialUser " + user.getPhone() + ": " + otp);
        return otp;
    }


    public boolean verifyOtpForUser(MatrimonialUser user, String otp) {
        return OtpUtil.isOtpValid(user.getCurrentOtp(), user.getOtpGeneratedAtMillis(), otp);
    }


 // Get approved users who have not completed payment
    public List<MatrimonialUser> getApprovedButNotPaidUsers() {
        return repo.findAll().stream()
                .filter(u -> Boolean.TRUE.equals(u.getApproved()) && !Boolean.TRUE.equals(u.getPaymentDone()))
                .collect(Collectors.toList());
    }


    // ---------------- Admin ----------------
    public void approveUser(Long id) {
        MatrimonialUser user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setApproved(true);
        user.setStatus("APPROVED"); // user still needs payment
        repo.save(user);
    }
 
    public Optional<MatrimonialUser> getUserByPhone(String phone) {
        return repo.findByPhone(phone);
    }
    
 // ---------------- Admin ----------------
    public void rejectUser(Long id) {
        MatrimonialUser user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        repo.delete(user); // DELETE user if rejected
    }

    public List<MatrimonialUser> getUsersByStatus(String status) {
        return repo.findByStatus(status);
    }
  
    
    public List<MatrimonialUser> getPendingUsers() {
        return repo.findAll().stream()
                .filter(u -> u.getApproved() == null || Boolean.FALSE.equals(u.getApproved()))
                .collect(Collectors.toList());
    }
    
    public List<MatrimonialUser> findAll() {
        return repo.findAll();
    }

    // ---------------- Payment ----------------
 // ---------------- Payment by phone ----------------
    public void markPaymentDone(String phone) {
        MatrimonialUser user = repo.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Matrimonial user not found"));

        if (!Boolean.TRUE.equals(user.getApproved())) {
            throw new RuntimeException("User must be approved before payment");
        }

        user.setPaymentDone(true);
        user.setStatus("ACTIVE");
        repo.save(user);
    }


}
