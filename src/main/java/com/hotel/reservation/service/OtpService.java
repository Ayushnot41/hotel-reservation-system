package com.hotel.reservation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
@Slf4j
public class OtpService {

    // Fast2SMS API Key (You can put your free Fast2SMS API key here)
    // Get one for free at https://www.fast2sms.com/
    @Value("${fast2sms.api.key:your_api_key_here}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateOTP() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000); // Generate 4-digit OTP
        return String.valueOf(otp);
    }

    public boolean sendSmsOtp(String phone, String otp) {
        log.info("=====================================================");
        log.info("🚨 🚨 🚨   GENERATED REAL OTP FOR {}: {}   🚨 🚨 🚨", phone, otp);
        log.info("=====================================================");

        if ("your_api_key_here".equals(apiKey)) {
            log.warn("Fast2SMS API Key is missing! OTP is printed above for local testing. To send real SMS, update application.properties with fast2sms.api.key=YOUR_KEY");
            // Still return true so the user can test the UI flow even without a key
            return true;
        }

        try {
            // Fast2SMS API Configuration (Popular Indian SMS Gateway)
            String url = "https://www.fast2sms.com/dev/bulkV2?authorization=" + apiKey + 
                         "&variables_values=" + otp + 
                         "&route=otp&numbers=" + phone;

            HttpHeaders headers = new HttpHeaders();
            headers.set("cache-control", "no-cache");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            
            log.info("SMS Gateway Response: {}", response.getBody());
            return response.getStatusCode().is2xxSuccessful();
            
        } catch (Exception e) {
            log.error("Failed to send SMS via Fast2SMS API: {}", e.getMessage());
            return false;
        }
    }
}
