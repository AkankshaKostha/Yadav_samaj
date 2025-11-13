package com.example.yadavsajam.service;

import java.util.Random;

public class OtpUtil {

    public static final long OTP_EXPIRATION_MILLIS = 5 * 60 * 1000; // 5 minutes

    public static String generateOtp() {
        return String.valueOf(new Random().nextInt(9000) + 1000); // 4-digit OTP
    }

    public static boolean isOtpValid(String otpStored, Long generatedAt, String otpReceived) {
        if (otpStored == null || generatedAt == null || otpReceived == null) return false;

        long now = System.currentTimeMillis();
        if (now - generatedAt > OTP_EXPIRATION_MILLIS) return false;

        return otpStored.equals(otpReceived);
    }
}
