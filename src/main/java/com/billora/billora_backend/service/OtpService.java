package com.billora.billora_backend.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    // ⚠️ TODO: Replace with real Twilio credentials
    private static final String TWILIO_ACCOUNT_SID = "AC_replace_with_your_account_sid";
    private static final String TWILIO_AUTH_TOKEN = "replace_with_your_auth_token";
    private static final String TWILIO_PHONE_NUMBER = "+1234567890"; // Replace with your Twilio number

    // In-memory store for OTPs: Map<MobileNumber, OTP>
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    public OtpService() {
        // Initialize Twilio ONLY if credentials are provided to avoid crashing on boot
        if (!TWILIO_ACCOUNT_SID.startsWith("AC_replace")) {
            Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
        }
    }

    public String generateAndSendOtp(String mobileNumber) {
        // Generate 4-digit OTP
        String otp = String.format("%04d", new Random().nextInt(10000));
        
        otpStore.put(mobileNumber, otp);

        String msgBody = "Your Billora OTP is: " + otp + ". Do not share this with anyone.";

        try {
            if (!TWILIO_ACCOUNT_SID.startsWith("AC_replace")) {
                Message.creator(
                        new PhoneNumber(mobileNumber),
                        new PhoneNumber(TWILIO_PHONE_NUMBER),
                        msgBody
                ).create();
                System.out.println("✅ Real SMS sent to " + mobileNumber);
            } else {
                System.out.println("⚠️ TWILIO NOT CONFIGURED. MOCK OTP SMS to " + mobileNumber + ": " + otp);
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to send real SMS: " + e.getMessage());
            System.out.println("⚠️ FALLBACK MOCK OTP SMS to " + mobileNumber + ": " + otp);
        }

        return otp; // Return it so the controller can send it to the UI if needed
    }

    public boolean verifyOtp(String mobileNumber, String otp) {
        String storedOtp = otpStore.get(mobileNumber);
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStore.remove(mobileNumber); // OTP used
            return true;
        }
        return false;
    }
}
