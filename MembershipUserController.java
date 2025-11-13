package com.example.yadavsajam.controller;

import com.example.yadavsajam.model.MembershipUser;
import com.example.yadavsajam.repository.MembershipUserRepository;
import com.example.yadavsajam.service.MembershipUserService;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/membership")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class MembershipUserController {

    private final MembershipUserService membershipUserService;
    private final MembershipUserRepository repository;

    private static final String UPLOAD_DIR = "E:\\Akanksha\\Yadav_samaj\\yadavsajam\\uploads\\membership\\";

    private static final Map<String, Integer> MEMBERSHIP_PLANS = Map.of(
            "1_MONTH", 99,
            "LIFETIME", 499
    );

    // ---------------- Registration ----------------
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(
            @RequestPart("user") String userJson,
            @RequestPart(value = "photo", required = false) MultipartFile photoFile
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            MembershipUser user = mapper.readValue(userJson, MembershipUser.class);

            // Validate membership plan
            if (user.getMembershipPlan() == null || !MEMBERSHIP_PLANS.containsKey(user.getMembershipPlan())) {
                throw new IllegalArgumentException("Invalid membership plan selected.");
            }
            user.setMembershipAmount(MEMBERSHIP_PLANS.get(user.getMembershipPlan()));

            // Save user with optional photo
            MembershipUser saved = membershipUserService.register(user, photoFile, UPLOAD_DIR);

            return ResponseEntity.ok(Map.of(
                    "message", "User registered successfully",
                    "amount", saved.getMembershipAmount(),
                    "plan", saved.getMembershipPlan(),
                    "user", saved
            ));

        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // ---------------- Send OTP ----------------
    @GetMapping("/send-otp/{phone}")
    public ResponseEntity<?> sendOtp(@PathVariable String phone) {
        Optional<MembershipUser> userOpt = membershipUserService.findByPhone(phone);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "User not found"));
        }

        String otp = membershipUserService.generateOtp(userOpt.get());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "otp", otp,
                "message", "OTP generated successfully"
        ));
    }

    // ---------------- Verify OTP ----------------
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> payload) {
        String phone = payload.get("phone");
        String otp = payload.get("otp");

        MembershipUser user = membershipUserService.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("User not registered"));

        boolean validOtp = membershipUserService.verifyOtpForUser(user, otp);

        if (!validOtp) {
            return ResponseEntity.ok(Map.of("verified", false, "message", "Invalid or expired OTP"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("verified", true);
        response.put("status", user.getStatus() != null ? user.getStatus() : "ACTIVE");
        response.put("role", user.getRole() != null ? user.getRole() : "MEMBER");
        response.put("message", "OTP verified successfully");
        response.put("paymentDone", Boolean.TRUE.equals(user.getPaymentDone()));
        response.put("fullName", user.getFullName());
        response.put("membershipPlan", user.getMembershipPlan());
        response.put("membershipAmount", user.getMembershipAmount());

        return ResponseEntity.ok(response);
    }

    // ---------------- Get all users ----------------
    @GetMapping("/all")
    public ResponseEntity<List<MembershipUser>> getAll() {
        return ResponseEntity.ok().body(membershipUserService.findAll());
    }

    // ---------------- Update membership (with optional photo) ----------------
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateMembership(
            @PathVariable Long id,
            @RequestPart("user") String userJson,
            @RequestPart(value = "photo", required = false) MultipartFile photoFile
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            MembershipUser updatedUser = mapper.readValue(userJson, MembershipUser.class);

            if (updatedUser.getMembershipPlan() != null && !MEMBERSHIP_PLANS.containsKey(updatedUser.getMembershipPlan())) {
                throw new IllegalArgumentException("Invalid membership plan selected.");
            }

            if (updatedUser.getMembershipPlan() != null) {
                updatedUser.setMembershipAmount(MEMBERSHIP_PLANS.get(updatedUser.getMembershipPlan()));
            }

            updatedUser.setId(id);

            MembershipUser savedUser = membershipUserService.updateMembership(updatedUser, photoFile, UPLOAD_DIR);
            return ResponseEntity.ok(savedUser);

        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // ---------------- Delete member ----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
        MembershipUser member = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        repository.delete(member);
        return ResponseEntity.ok(Map.of("message", "Member deleted successfully"));
    }

    // ---------------- Get membership details by phone ----------------
    @GetMapping("/details/{phone}")
    public ResponseEntity<?> getMembershipDetails(@PathVariable String phone) {
        MembershipUser user = membershipUserService.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("fullName", user.getFullName() != null ? user.getFullName() : "N/A");
        response.put("state", user.getState() != null ? user.getState() : "N/A");
        response.put("membershipNumber", user.getMembershipNumber() != null ? user.getMembershipNumber() : user.getPhone());
        response.put("phone", user.getPhone() != null ? user.getPhone() : "N/A");
        response.put("profilePhoto", user.getProfilePhoto());
        response.put("referralCode", user.getReferralCode() != null ? user.getReferralCode() : "N/A");
        response.put("membershipPlan", user.getMembershipPlan());
        response.put("membershipAmount", user.getMembershipAmount());

        return ResponseEntity.ok(response);
    }
}
