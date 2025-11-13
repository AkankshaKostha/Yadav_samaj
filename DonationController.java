package com.example.yadavsajam.controller;

import com.example.yadavsajam.model.Donation;
import com.example.yadavsajam.repository.DonationRepository;
import com.example.yadavsajam.service.DonationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donations")
@CrossOrigin(origins = "http://localhost:4200")
public class DonationController {

    private final DonationService service;
    @Autowired
    private DonationRepository donationRepository;
    private static final Logger logger = LoggerFactory.getLogger(DonationController.class);

    public DonationController(DonationService service) {
        this.service = service;
    }

    // Create dummy order
    @PostMapping("/create-dummy-order")
    public ResponseEntity<Map<String, Object>> createDummyOrder(@RequestBody Map<String, Object> payload) {
        logger.info("Received create-dummy-order payload: {}", payload);

        double amount;
        try {
            amount = Double.parseDouble(payload.get("amount").toString());
        } catch (Exception e) {
            logger.error("Invalid amount in payload: {}", payload, e);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid amount"));
        }

        String orderId = service.generateOrderId();

        Map<String, Object> resp = new HashMap<>();
        resp.put("orderId", orderId);
        resp.put("amount", (int)(amount * 100)); // paise
        resp.put("currency", "INR");
        resp.put("key", "DUMMY_KEY");

        return ResponseEntity.ok(resp);
    }

    // Confirm dummy donation
    @PostMapping("/confirm-dummy")
    public ResponseEntity<Map<String, Object>> confirmDummy(@RequestBody Donation donation) {
        logger.info("Confirm donation request: {}", donation);

        // Trim strings to avoid whitespace issues
        if (donation.getPaymentId() != null) donation.setPaymentId(donation.getPaymentId().trim());
        if (donation.getOrderId() != null) donation.setOrderId(donation.getOrderId().trim());
        if (donation.getName() != null) donation.setName(donation.getName().trim());
        if (donation.getMessage() != null) donation.setMessage(donation.getMessage().trim());
        if (donation.getPaymentMethod() != null) donation.setPaymentMethod(donation.getPaymentMethod().trim());

        if (donation.getPaymentId() == null || donation.getPaymentId().isBlank()) {
            donation.setPaymentId(service.generatePaymentId());
        }
        donation.setPaid(true);

        Donation saved = service.save(donation);

        Map<String, Object> resp = new HashMap<>();
        resp.put("status", "success");
        resp.put("donationId", saved.getId());
        resp.put("paymentId", saved.getPaymentId());
        resp.put("orderId", saved.getOrderId());

        logger.info("Donation confirmed successfully: {}", saved);
        return ResponseEntity.ok(resp);
    }

    // Get all donations
    @GetMapping("/user")
    public List<Donation> getUserDonations() {
        return donationRepository.findByPaymentMethodNot("ADMIN");
    }

    // ✅ Get donations added by admin
    @GetMapping("/admin")
    public List<Donation> getAdminDonations() {
        return donationRepository.findByPaymentMethod("ADMIN");
    }

    // ✅ Add donation by admin
    @PostMapping("/admin")
    public Donation addAdminDonation(@RequestBody Donation donation) {
        donation.setPaymentMethod("ADMIN");
        donation.setPaid(true);
        return donationRepository.save(donation);
    }

    // ✅ Update admin donation
    @PutMapping("/admin/{id}")
    public Donation updateAdminDonation(@PathVariable Long id, @RequestBody Donation updatedDonation) {
        Donation existing = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        existing.setName(updatedDonation.getName());
        existing.setAmount(updatedDonation.getAmount());
        existing.setMessage(updatedDonation.getMessage());
        return donationRepository.save(existing);
    }

    // ✅ Delete admin donation
    @DeleteMapping("/admin/{id}")
    public void deleteAdminDonation(@PathVariable Long id) {
        donationRepository.deleteById(id);
    }
}
