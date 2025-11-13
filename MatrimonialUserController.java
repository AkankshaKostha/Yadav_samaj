package com.example.yadavsajam.controller;

import com.example.yadavsajam.model.MatrimonialUser;
import com.example.yadavsajam.repository.MatrimonialUserRepository;
import com.example.yadavsajam.service.MatrimonialUserService;
import com.example.yadavsajam.service.OtpUtil;
import com.example.yadavsajam.service.PaymentService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;        // ✅ for IOException
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;         // ✅ for HashMap
import java.util.List;            // ✅ for List
import java.util.Map;             // ✅ for Map
import java.util.Optional;        // ✅ for Optional
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/matrimonial")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class MatrimonialUserController {

    private final MatrimonialUserService matrimonialUserService;
    private final MatrimonialUserRepository repository;
    private final String UPLOAD_DIR = "E:\\Akanksha\\Yadav_samaj\\yadavsajam\\uploads\\matrimonial\\";
    @Autowired
    private PaymentService paymentService;

    private static final long OTP_EXPIRATION_MILLIS = 5 * 60 * 1000; // 5 minutes
    
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(
            @RequestPart("user") String userJson,
            @RequestPart(value = "photo", required = false) MultipartFile photoFile
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            MatrimonialUser user = mapper.readValue(userJson, MatrimonialUser.class);

            // ✅ Validate membership plan
            if (user.getMembershipPlan() == null || user.getMembershipPlan().isEmpty()) {
                throw new IllegalArgumentException("Please select a membership plan.");
            }

            // ✅ Set membership amount and expiry automatically
            switch (user.getMembershipPlan()) {
                case "1_MONTH" -> {
                    user.setMembershipAmount(99);
                    user.setMembershipExpiryDate(LocalDate.now().plusMonths(1));
                }
                case "LIFETIME" -> {
                    user.setMembershipAmount(499);
                    user.setMembershipExpiryDate(null);
                }
                default -> throw new IllegalArgumentException("Invalid membership plan selected.");
            }

            // ✅ Save user with photo
            String UPLOAD_DIR = "E:\\Akanksha\\Yadav_samaj\\yadavsajam\\uploads\\matrimonial\\";
            MatrimonialUser saved = matrimonialUserService.register(user, photoFile, UPLOAD_DIR);

            return ResponseEntity.ok(Map.of(
                    "message", "Matrimonial user registered successfully",
                    "amount", saved.getMembershipAmount(),
                    "plan", saved.getMembershipPlan(),
                    "user", saved
            ));

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Registration failed: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }



    @GetMapping("/send-otp/{phone}")
    public ResponseEntity<?> sendOtp(@PathVariable String phone) {
        Optional<MatrimonialUser> userOpt = matrimonialUserService.findByPhone(phone);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "User not found"));
        }

        try {
            String otp = matrimonialUserService.generateOtp(userOpt.get());
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "otp", otp,
                    "message", "OTP generated successfully"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> payload) {
        String phone = payload.get("phone");
        String otp = payload.get("otp");

        Optional<MatrimonialUser> optionalUser = matrimonialUserService.getUserByPhone(phone);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "verified", false,
                    "message", "User not registered"
            ));
        }

        MatrimonialUser user = optionalUser.get();

        // Check OTP expiration first
        long now = System.currentTimeMillis();
        if (user.getOtpGeneratedAtMillis() == null || (now - user.getOtpGeneratedAtMillis() > OtpUtil.OTP_EXPIRATION_MILLIS)) {
            return ResponseEntity.ok(Map.of(
                    "verified", false,
                    "message", "OTP has expired. Please request a new one."
            ));
        }

        // Check if OTP matches
        if (!otp.equals(user.getCurrentOtp())) {
            return ResponseEntity.ok(Map.of(
                    "verified", false,
                    "message", "Invalid OTP. Please try again."
            ));
        }

        // Check admin approval
        if (!Boolean.TRUE.equals(user.getApproved())) {
            return ResponseEntity.ok(Map.of(
                    "verified", false,
                    "message", "Your registration is pending admin approval."
            ));
        }

        // OTP verified and admin approved
        Map<String, Object> success = new HashMap<>();
        success.put("verified", true);
        success.put("status", user.getStatus() != null ? user.getStatus() : "ACTIVE");
        success.put("role", user.getRole() != null ? user.getRole() : "USER");
        success.put("message", "OTP verified successfully");
        success.put("paymentDone", Boolean.TRUE.equals(user.getPaymentDone()));

        return ResponseEntity.ok(success);
    }

        @GetMapping("/pending")
        public ResponseEntity<?> getPendingAndPaymentPendingUsers() {
            List<MatrimonialUser> pendingUsers = matrimonialUserService.getPendingUsers();
            List<MatrimonialUser> approvedButNotPaid = matrimonialUserService.getApprovedButNotPaidUsers();

            // Map pending users
            List<Map<String, Object>> pendingMapped = pendingUsers.stream()
                    .map(u -> {
                        Map<String, Object> m = new HashMap<>();
                        m.put("id", u.getId());
                        m.put("fullName", u.getFullName());
                        m.put("phone", u.getPhone());
                        m.put("email", u.getEmail());
                        m.put("type", "pending");
                        m.put("paymentPending", false);
                        return m;
                    })
                    .collect(Collectors.toList());

            // Map approved but not paid users
            List<Map<String, Object>> paymentPendingMapped = approvedButNotPaid.stream()
                    .map(u -> {
                        Map<String, Object> m = new HashMap<>();
                        m.put("id", u.getId());
                        m.put("fullName", u.getFullName());
                        m.put("phone", u.getPhone());
                        m.put("email", u.getEmail());
                        m.put("type", "approved");
                        m.put("paymentPending", true);
                        return m;
                    })
                    .collect(Collectors.toList());

            // Combine both lists
            List<Map<String, Object>> result = new ArrayList<>();
            result.addAll(pendingMapped);
            result.addAll(paymentPendingMapped);

            return ResponseEntity.ok(result);
        }



    @GetMapping("/all")
    public List<MatrimonialUser> getAllUsers() {
        return matrimonialUserService.getAllUsers();
    }
    @GetMapping("/details/{phone}")
    public ResponseEntity<?> getUserDetails(@PathVariable String phone) {
        Optional<MatrimonialUser> optionalUser = matrimonialUserService.findByPhone(phone);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found"));
        }
        return ResponseEntity.ok(optionalUser.get());
    }

    @GetMapping("/status/{phone}")
    public ResponseEntity<?> getStatus(@PathVariable String phone) {
        Optional<MatrimonialUser> optionalUser = matrimonialUserService.findByPhone(phone);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.ok(Map.of("found", false));
        }

        MatrimonialUser user = optionalUser.get();

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("found", true);
        response.put("status", user.getStatus() != null ? user.getStatus() : "PENDING");
        response.put("approved", user.getApproved() != null ? user.getApproved() : false);
        response.put("paymentDone", user.getPaymentDone() != null ? user.getPaymentDone() : false);
        response.put("membershipPlan", user.getMembershipPlan() != null ? user.getMembershipPlan() : "NONE");
        response.put("fullName", user.getFullName());

        return ResponseEntity.ok(response);
    }
    
 // MatrimonialUserController.java
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestPart("user") MatrimonialUser user,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {

        Optional<MatrimonialUser> optionalUser = matrimonialUserService.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found"));
        }

        MatrimonialUser existingUser = optionalUser.get();

        // Update fields
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setDob(user.getDob());
        existingUser.setGender(user.getGender());
        existingUser.setEducation(user.getEducation());
        existingUser.setProfession(user.getProfession());
        existingUser.setAbout(user.getAbout());
        existingUser.setPhone(user.getPhone());
        existingUser.setFatherName(user.getFatherName());
        existingUser.setMotherName(user.getMotherName());
        existingUser.setBrothers(user.getBrothers());
        existingUser.setSisters(user.getSisters());
        existingUser.setAddress(user.getAddress());
        existingUser.setCity(user.getCity());
        existingUser.setState(user.getState());
        existingUser.setCountry(user.getCountry());
        existingUser.setMaritalStatus(user.getMaritalStatus());
        existingUser.setMotherTongue(user.getMotherTongue());
        existingUser.setHobbies(user.getHobbies());
        existingUser.setReligion(user.getReligion());
        existingUser.setCaste(user.getCaste());
        existingUser.setDiet(user.getDiet());
        existingUser.setLifestyle(user.getLifestyle());
        existingUser.setHeight(user.getHeight());
        existingUser.setWeight(user.getWeight());
        existingUser.setBloodGroup(user.getBloodGroup());

        // Handle photo upload
        if (photo != null && !photo.isEmpty()) {
            try {
                // Unique filename
                String filename = System.currentTimeMillis() + "_" + photo.getOriginalFilename();

                // Path object
                Path uploadPath = Paths.get(UPLOAD_DIR + filename);

                // Create directories if not exist
                Files.createDirectories(uploadPath.getParent());

                // Save file
                photo.transferTo(uploadPath.toFile());

                // Update user photo path
                existingUser.setPhotoFileName("/uploads/matrimonial/" + filename);

            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Error saving photo: " + e.getMessage()));
            }
        }

        // Save updated user
        MatrimonialUser updatedUser = matrimonialUserService.save(existingUser);
        return ResponseEntity.ok(updatedUser);
    }



    @PostMapping("/approve/{id}")
    public void approve(@PathVariable Long id) {
        matrimonialUserService.approveUser(id);
    }

    @PostMapping("/reject/{id}")
    public void reject(@PathVariable Long id) {
        matrimonialUserService.rejectUser(id);
    }

 // ---------------- PAYMENT by phone ----------------
 

    // ---------------- Private OTP verification ----------------
    private boolean verifyOtpForUser(MatrimonialUser user, String otp) {
        if (user == null || user.getCurrentOtp() == null || user.getOtpGeneratedAtMillis() == null) return false;

        long now = System.currentTimeMillis();
        if (now - user.getOtpGeneratedAtMillis() > OTP_EXPIRATION_MILLIS) return false;

        return otp.equals(user.getCurrentOtp());
    }


}

