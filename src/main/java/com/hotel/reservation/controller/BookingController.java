package com.hotel.reservation.controller;

import com.hotel.reservation.model.*;
import com.hotel.reservation.model.enums.PaymentMethod;
import com.hotel.reservation.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

@Controller
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final RoomService roomService;
    private final UserService userService;
    private final PaymentService paymentService;

    @GetMapping("/new")
    public String bookingForm(@RequestParam Long roomId,
                              @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                              @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut,
                              Model model) {
        Room room = roomService.findById(roomId);
        model.addAttribute("room", room);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        return "bookings/form";
    }

    @PostMapping("/confirm")
    public String confirmBooking(@RequestParam Long roomId,
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut,
                                 @RequestParam Integer guests,
                                 @RequestParam(required = false) String specialRequests,
                                 Authentication auth, Model model, RedirectAttributes redirectAttributes) {
        Room room = roomService.findById(roomId);
        User user = userService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate dates
        if (checkIn.isBefore(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("error", "Check-in date cannot be in the past");
            return "redirect:/bookings/new?roomId=" + roomId;
        }
        if (checkOut.isBefore(checkIn.plusDays(1))) {
            redirectAttributes.addFlashAttribute("error", "Check-out must be after check-in");
            return "redirect:/bookings/new?roomId=" + roomId;
        }
        if (guests > room.getCapacity()) {
            redirectAttributes.addFlashAttribute("error", "Guests exceed room capacity of " + room.getCapacity());
            return "redirect:/bookings/new?roomId=" + roomId;
        }

        // Check availability
        if (!bookingService.isRoomAvailable(roomId, checkIn, checkOut)) {
            redirectAttributes.addFlashAttribute("error", "Room is not available for selected dates");
            return "redirect:/rooms/" + roomId;
        }

        // Build temporary booking for summary
        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        java.math.BigDecimal totalPrice = room.getPricePerNight()
                .multiply(java.math.BigDecimal.valueOf(nights));

        model.addAttribute("room", room);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        model.addAttribute("guests", guests);
        model.addAttribute("nights", nights);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("specialRequests", specialRequests);
        model.addAttribute("paymentMethods", PaymentMethod.values());
        return "bookings/summary";
    }

    @PostMapping("/create")
    public String createBooking(@RequestParam Long roomId,
                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut,
                                @RequestParam Integer guests,
                                @RequestParam(required = false) String specialRequests,
                                @RequestParam PaymentMethod paymentMethod,
                                Authentication auth, RedirectAttributes redirectAttributes) {
        try {
            Room room = roomService.findById(roomId);
            User user = userService.findByEmail(auth.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Booking booking = Booking.builder()
                    .checkInDate(checkIn)
                    .checkOutDate(checkOut)
                    .guests(guests)
                    .specialRequests(specialRequests)
                    .build();

            booking = bookingService.createBooking(user, room, booking);

            // Process simulated payment
            paymentService.processPayment(booking, paymentMethod);

            redirectAttributes.addFlashAttribute("success", "Booking confirmed successfully!");
            return "redirect:/bookings/" + booking.getId() + "/confirmed";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/rooms/" + roomId;
        }
    }

    @GetMapping("/{id}/confirmed")
    public String bookingConfirmation(@PathVariable Long id, Authentication auth, Model model) {
        Booking booking = bookingService.findById(id);
        Payment payment = paymentService.findByBookingId(id).orElse(null);
        model.addAttribute("booking", booking);
        model.addAttribute("payment", payment);
        return "bookings/confirmation";
    }

    @GetMapping("/history")
    public String bookingHistory(Authentication auth, Model model) {
        User user = userService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("bookings", bookingService.findByUserId(user.getId()));
        return "bookings/history";
    }

    @PostMapping("/{id}/cancel")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.updateStatus(id, com.hotel.reservation.model.enums.BookingStatus.CANCELLED);
            redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to cancel booking");
        }
        return "redirect:/bookings/history";
    }
}
