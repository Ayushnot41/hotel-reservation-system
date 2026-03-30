package com.hotel.reservation.controller;

import com.hotel.reservation.model.User;
import com.hotel.reservation.model.enums.Role;
import com.hotel.reservation.repository.UserRepository;
import com.hotel.reservation.service.OtpService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth/api")
@RequiredArgsConstructor
@Slf4j
public class OtpController {

    private final OtpService otpService;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String phone, HttpSession session) {
        // 1. Generate real random 4-digit OTP
        String generatedOtp = otpService.generateOTP();
        
        // 2. Save it to session for this user to check later
        session.setAttribute("SERVER_OTP_" + phone, generatedOtp);
        log.info("Saved OTP {} for phone {} in session", generatedOtp, phone);

        // 3. Send the SMS
        boolean isSent = otpService.sendSmsOtp(phone, generatedOtp);
        
        if (isSent) {
            return ResponseEntity.ok().body("OTP Sent Successfully!");
        } else {
            return ResponseEntity.badRequest().body("Failed to send SMS.");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String phone, @RequestParam String otp, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String savedOtp = (String) session.getAttribute("SERVER_OTP_" + phone);

        log.info("Verifying OTP for {}: Received={}, Expected={}", phone, otp, savedOtp);

        if (savedOtp != null && savedOtp.equals(otp)) {
            // Remove the OTP to prevent reuse
            session.removeAttribute("SERVER_OTP_" + phone);

            // LOG THE USER IN AUTOMATICALLY
            String mockEmail = phone + "@mobile-auth.com";
            String mockName = "User " + phone.substring(phone.length() > 4 ? phone.length() - 4 : 0);
            
            Optional<User> existingUser = userRepository.findByEmail(mockEmail);
            if (existingUser.isEmpty()) {
                User newUser = new User();
                newUser.setName(mockName);
                newUser.setEmail(mockEmail);
                newUser.setPhone(phone);
                newUser.setPassword("$2a$10$xyz123mockpasswordhash"); 
                newUser.setRole(Role.USER);
                userRepository.save(newUser);
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(mockEmail);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

            return ResponseEntity.ok().body("Verified and Logged In");
        }

        return ResponseEntity.badRequest().body("Invalid OTP");
    }
}
