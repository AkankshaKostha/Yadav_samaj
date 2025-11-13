package com.example.yadavsajam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateOrderResponse {
    private String orderId;
    private Integer amountInPaise; // optional
    private String currency;
    private String key; // optional (kept for compatibility with frontend)
}
