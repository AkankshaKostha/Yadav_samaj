package com.example.yadavsajam.dto;

import lombok.Data;

@Data
public class ConfirmDonationRequest {
    private String orderId;
    private String paymentId;
    private String name;
    private Double amount;
    private String message;
    private String paymentMethod; // optional, e.g. "DUMMY"
}