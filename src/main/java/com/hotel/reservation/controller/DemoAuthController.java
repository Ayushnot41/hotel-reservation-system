package com.hotel.reservation.controller;

import com.hotel.reservation.model.enums.Role;
import com.hotel.reservation.model.User;
import com.hotel.reservation.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Controller
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoAuthController {

    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    @GetMapping("/google-login")
    public RedirectView googleLogin(HttpServletRequest request) {
        return mockSocialLogin(request, "Ayush (Google)", "ayushsarkarfkt@gmail.com");
    }

    @GetMapping("/apple-login")
    public RedirectView appleLogin(HttpServletRequest request) {
        return mockSocialLogin(request, "Ayush (Apple)", "ayush.apple@example.com");
    }

    private RedirectView mockSocialLogin(HttpServletRequest request, String name, String email) {
        // 1. Check if user exists, if not create them
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            // set a mock password since they authenticated via "social"
            newUser.setPassword("$2a$10$xyz123mockpasswordhash"); 
            newUser.setRole(Role.USER);
            userRepository.save(newUser);
        }

        // 2. Load the user details and log them in
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        // 3. Save security context to the session so they stay logged in
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

        // 4. Redirect to the homepage just like normal login
        return new RedirectView("/");
    }
}
