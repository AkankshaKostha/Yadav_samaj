package com.example.yadavsajam.controller;

import com.example.yadavsajam.model.Donation;
import com.example.yadavsajam.service.DonationService;
import com.example.yadavsajam.service.MatrimonialUserService;
import com.example.yadavsajam.service.MembershipUserService;
import com.example.yadavsajam.model.MembershipUser;
import com.example.yadavsajam.model.MembershipUser;
import com.example.yadavsajam.model.MatrimonialUser;
import com.example.yadavsajam.model.MatrimonialUser;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin/search")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminSearchController {

    private final DonationService donationService;
    private final MembershipUserService membershipService;
    private final MatrimonialUserService matrimonialService;

    public AdminSearchController(DonationService donationService,
                                 MembershipUserService membershipService,
                                 MatrimonialUserService matrimonialService) {
        this.donationService = donationService;
        this.membershipService = membershipService;
        this.matrimonialService = matrimonialService;
    }

    // 🔹 Search Matrimonial Member by ID
    @GetMapping("/matrimonial/{id}")
    public ResponseEntity<?> searchMatrimonialById(@PathVariable Long id) {
        Optional<MatrimonialUser> member = matrimonialService.findById(id);
        return member.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Matrimonial member not found"));
    }

    // 🔹 Search Membership Member by ID
    @GetMapping("/membership/{id}")
    public ResponseEntity<?> searchMembershipById(@PathVariable Long id) {
        Optional<MembershipUser> member = membershipService.findById(id);
        return member.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Membership member not found"));
    }

    // 🔹 Search Donation by ID
    @GetMapping("/donation/{id}")
    public ResponseEntity<?> searchDonationById(@PathVariable Long id) {
        Optional<Donation> donation = donationService.findById(id);
        return donation.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Donation not found"));
    }
    
 // Matrimonial by phone
    @GetMapping("/matrimonial/phone/{phone}")
    public ResponseEntity<?> searchMatrimonialByPhone(@PathVariable String phone) {
        Optional<MatrimonialUser> member = matrimonialService.findByPhone(phone);
        return member.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Matrimonial member not found with phone: " + phone));
    }

    // Membership by phone
    @GetMapping("/membership/phone/{phone}")
    public ResponseEntity<?> searchMembershipByPhone(@PathVariable String phone) {
        Optional<MembershipUser> member = membershipService.findByPhone(phone);
        return member.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Membership member not found with phone: " + phone));
    }

    // Donation by paymentId
    @GetMapping("/donation/payment/{paymentId}")
    public ResponseEntity<?> searchDonationByPaymentId(@PathVariable String paymentId) {
        Optional<Donation> donation = donationService.findByPaymentId(paymentId);
        return donation.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Donation not found with paymentId: " + paymentId));
    }

}
