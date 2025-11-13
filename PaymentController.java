package com.example.yadavsajam.controller;

import com.example.yadavsajam.model.MatrimonialUser;
import com.example.yadavsajam.model.MembershipUser;
import com.example.yadavsajam.service.MatrimonialUserService;
import com.example.yadavsajam.service.MembershipUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    private final MatrimonialUserService matrimonialService;
    private final MembershipUserService membershipService;

    // ---------------- GET PAYMENT AMOUNT ----------------
    @GetMapping("/amount/{phone}")
    public ResponseEntity<?> getPaymentAmount(
            @PathVariable String phone,
            @RequestParam String userType) {

        // ✅ MATRIMONIAL — unchanged
        if ("MATRIMONIAL".equalsIgnoreCase(userType)) {
            Optional<MatrimonialUser> userOpt = matrimonialService.findByPhone(phone);
            if (userOpt.isEmpty()) return ResponseEntity.status(404).body(Map.of("message", "Matrimonial user not found"));

            MatrimonialUser user = userOpt.get();
            if (!Boolean.TRUE.equals(user.getApproved()))
                return ResponseEntity.status(403).body(Map.of("message", "Admin approval pending for Matrimonial user."));
            if (Boolean.TRUE.equals(user.getPaymentDone()))
                return ResponseEntity.status(403).body(Map.of("message", "Payment already completed for Matrimonial user."));

            int amount = switch (user.getMembershipPlan()) {
                case "1_MONTH" -> 99;
                
                case "LIFETIME" -> 499;
                default -> 0;
            };

            return ResponseEntity.ok(Map.of(
                    "userType", "MATRIMONIAL",
                    "fullName", user.getFullName(),
                    "phone", user.getPhone(),
                    "membershipPlan", user.getMembershipPlan(),
                    "amount", amount
            ));
        }

        // ✅ MEMBERSHIP — fixed null-safe handling
        if ("MEMBERSHIP".equalsIgnoreCase(userType)) {
            Optional<MembershipUser> userOpt = membershipService.findByPhone(phone);
            if (userOpt.isEmpty())
                return ResponseEntity.status(404).body(Map.of("message", "Membership user not found"));

            MembershipUser user = userOpt.get();

            if (!Boolean.TRUE.equals(user.getApproved()))
                return ResponseEntity.status(403).body(Map.of("message", "Admin approval pending for Membership user."));
            if (Boolean.TRUE.equals(user.getPaymentDone()))
                return ResponseEntity.status(403).body(Map.of("message", "Payment already completed for Membership user."));

            // ✅ use amount saved at registration time
            int amount = user.getMembershipAmount() != null ? user.getMembershipAmount() : 0;

            return ResponseEntity.ok(Map.of(
                    "userType", "MEMBERSHIP",
                    "fullName", Optional.ofNullable(user.getFullName()).orElse(""),
                    "phone", user.getPhone(),
                    "membershipPlan", Optional.ofNullable(user.getMembershipPlan()).orElse(""),
                    "amount", amount
            ));
        }


        return ResponseEntity.badRequest().body(Map.of("message", "Invalid user type"));
    }

    // ---------------- MARK PAYMENT DONE ----------------
    @PostMapping("/mark-done")
    public ResponseEntity<?> markPaymentDone(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String paymentId = request.get("paymentId");
        String userType = request.get("userType");

        if (paymentId == null || paymentId.isBlank() || userType == null || userType.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid request"));
        }

        // ✅ MATRIMONIAL — unchanged
        if ("MATRIMONIAL".equalsIgnoreCase(userType)) {
            Optional<MatrimonialUser> userOpt = matrimonialService.findByPhone(phone);
            if (userOpt.isEmpty()) return ResponseEntity.status(404).body(Map.of("message", "Matrimonial user not found"));

            MatrimonialUser user = userOpt.get();
            if (!Boolean.TRUE.equals(user.getApproved()))
                return ResponseEntity.status(403).body(Map.of("message", "Admin approval pending"));
            if (Boolean.TRUE.equals(user.getPaymentDone()))
                return ResponseEntity.status(403).body(Map.of("message", "Payment already completed"));

            user.setPaymentDone(true);
            user.setPaymentId(paymentId);
            user.setStatus("ACTIVE");
            matrimonialService.save(user);

            return ResponseEntity.ok(Map.of("success", true, "message", "Payment successful for Matrimonial user: " + user.getFullName()));
        }

        // ✅ MEMBERSHIP — same logic but null-safe
        if ("MEMBERSHIP".equalsIgnoreCase(userType)) {
            Optional<MembershipUser> userOpt = membershipService.findByPhone(phone);
            if (userOpt.isEmpty()) return ResponseEntity.status(404).body(Map.of("message", "Membership user not found"));

            MembershipUser user = userOpt.get();
            if (!Boolean.TRUE.equals(user.getApproved()))
                return ResponseEntity.status(403).body(Map.of("message", "Admin approval pending"));
            if (Boolean.TRUE.equals(user.getPaymentDone()))
                return ResponseEntity.status(403).body(Map.of("message", "Payment already completed"));

            user.setPaymentDone(true);
            user.setPaymentId(paymentId);
            user.setStatus("ACTIVE");
            membershipService.save(user);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Payment successful for Membership user: " + Optional.ofNullable(user.getFullName()).orElse("User")
            ));
        }

        return ResponseEntity.badRequest().body(Map.of("message", "Invalid user type"));
    }
}
