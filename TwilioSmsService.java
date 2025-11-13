package com.example.yadavsajam.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class TwilioSmsService {

    // Replace these with your Twilio credentials
    private static final String ACCOUNT_SID = "YOUR_TWILIO_ACCOUNT_SID";
    private static final String AUTH_TOKEN = "YOUR_TWILIO_AUTH_TOKEN";
    private static final String FROM_NUMBER = "+1234567890"; // Twilio phone number

    public TwilioSmsService() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public boolean sendOtp(String toPhone, String otp) {
        try {
            if (!toPhone.startsWith("+91")) {
                toPhone = "+91" + toPhone;
            }

            String messageText = "Your Login OTP is " + otp + " - NISHKA SALES";

            Message message = Message.creator(
                    new PhoneNumber(toPhone),
                    new PhoneNumber(FROM_NUMBER),
                    messageText
            ).create();

            System.out.println("[Twilio SMS] SID: " + message.getSid());
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
