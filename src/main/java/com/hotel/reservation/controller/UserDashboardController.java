package com.hotel.reservation.controller;

import com.hotel.reservation.model.User;
import com.hotel.reservation.service.BookingService;
import com.hotel.reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class UserDashboardController {

    private final UserService userService;
    private final BookingService bookingService;

    @GetMapping
    public String dashboard(Authentication auth, Model model) {
        User user = userService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        model.addAttribute("bookings", bookingService.findByUserId(user.getId()));
        return "dashboard/index";
    }
}
