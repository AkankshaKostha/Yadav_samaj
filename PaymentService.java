package com.example.yadavsajam.service;

import com.example.yadavsajam.model.PaymentRecord;
import com.example.yadavsajam.repository.PaymentRecordRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRecordRepository paymentRepository;

    /**
     * Save or update a payment record
     */
    @Transactional
    public PaymentRecord savePayment(PaymentRecord payment) {
        return paymentRepository.save(payment);
    }

    /**
     * Get all payment records
     */
    public List<PaymentRecord> getAllPayments() {
        return paymentRepository.findAll();
    }

    /**
     * Get the total sum of all payment amounts
     */
    public double getTotalPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(PaymentRecord::getAmount)
                .filter(amount -> amount != null)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    /**
     * Find payment by Razorpay Order ID
     */
    public Optional<PaymentRecord> findByOrderId(String orderId) {
        return paymentRepository.findByRazorpayOrderId(orderId);
    }

    /**
     * Convenience method to get payment or null
     */
    public PaymentRecord findByOrderIdOrNull(String orderId) {
        return findByOrderId(orderId).orElse(null);
    }
}
