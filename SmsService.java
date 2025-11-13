package com.example.yadavsajam.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class SmsService {

    private final String SMS_URL = "http://103.233.79.217/api/mt/SendSMS";
    private final String USER = "Nishka";
    private final String PASSWORD = "123456";
    private final String SENDER = "NISKAS";

    /**
     * Sends OTP via SMS
     * @param phone Phone number (10 digits, with or without +91)
     * @param otp OTP to send
     * @return true if SMS sent successfully, false otherwise
     */
    public boolean sendOtpSms(String phone, String otp) {
        try {
            // Validate phone number
            if (phone == null || !phone.matches("\\+?\\d{10,12}")) {
                System.err.println("[SMS] Invalid phone number: " + phone);
                return false;
            }

            // Ensure country code
            if (!phone.startsWith("+91")) {
                phone = "+91" + phone;
            }

            // Compose message
            String text = "Your Login OTP is " + otp + " - NISHKA SALES";
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);

            // Build URL
            String url = SMS_URL +
                    "?user=" + USER +
                    "&password=" + PASSWORD +
                    "&senderid=" + SENDER +
                    "&channel=Trans&DCS=0&flashsms=0" +
                    "&number=" + phone +
                    "&text=" + encodedText +
                    "&route=8";

            // Log URL for debugging
            System.out.println("[SMS] Request URL: " + url);

            // Send GET request
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            // Log API response
            System.out.println("[SMS] API Response: " + response);

            // Check if SMS gateway says success
            boolean success = response != null && (response.contains("Done") || response.contains("OK"));
            if (!success) {
                System.err.println("[SMS] Failed to send OTP to " + phone);
            } else {
                System.out.println("[SMS] OTP sent successfully to " + phone);
            }

            return success;

        } catch (Exception e) {
            System.err.println("[SMS] Exception while sending OTP to " + phone);
            e.printStackTrace();
            return false;
        }
    }
}
