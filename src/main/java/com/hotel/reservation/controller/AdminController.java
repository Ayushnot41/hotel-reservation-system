package com.hotel.reservation.controller;

import com.hotel.reservation.model.Room;
import com.hotel.reservation.model.enums.BookingStatus;
import com.hotel.reservation.model.enums.RoomType;
import com.hotel.reservation.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final RoomService roomService;
    private final BookingService bookingService;
    private final UserService userService;
    private final ContactService contactService;

    // ========== Dashboard ==========
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalRooms", roomService.countRooms());
        model.addAttribute("totalBookings", bookingService.countBookings());
        model.addAttribute("totalUsers", userService.countUsers());
        model.addAttribute("totalMessages", contactService.count());
        model.addAttribute("pendingBookings", bookingService.countByStatus(BookingStatus.PENDING));
        model.addAttribute("confirmedBookings", bookingService.countByStatus(BookingStatus.CONFIRMED));
        model.addAttribute("recentBookings", bookingService.findAll().stream().limit(5).toList());
        return "admin/dashboard";
    }

    // ========== Room Management ==========
    @GetMapping("/rooms")
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.findAll());
        return "admin/rooms/list";
    }

    @GetMapping("/rooms/new")
    public String newRoomForm(Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("roomTypes", RoomType.values());
        return "admin/rooms/form";
    }

    @PostMapping("/rooms/save")
    public String saveRoom(@Valid @ModelAttribute("room") Room room,
                           BindingResult result, Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("roomTypes", RoomType.values());
            return "admin/rooms/form";
        }
        roomService.save(room);
        redirectAttributes.addFlashAttribute("success", "Room saved successfully!");
        return "redirect:/admin/rooms";
    }

    @GetMapping("/rooms/{id}/edit")
    public String editRoomForm(@PathVariable Long id, Model model) {
        model.addAttribute("room", roomService.findById(id));
        model.addAttribute("roomTypes", RoomType.values());
        return "admin/rooms/form";
    }

    @GetMapping("/rooms/{id}/delete")
    public String deleteRoom(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            roomService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Room deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete room with existing bookings");
        }
        return "redirect:/admin/rooms";
    }

    // ========== Booking Management ==========
    @GetMapping("/bookings")
    public String listBookings(Model model) {
        model.addAttribute("bookings", bookingService.findAll());
        return "admin/bookings/list";
    }

    @GetMapping("/bookings/{id}")
    public String bookingDetail(@PathVariable Long id, Model model) {
        model.addAttribute("booking", bookingService.findById(id));
        return "admin/bookings/detail";
    }

    @PostMapping("/bookings/{id}/status")
    public String updateBookingStatus(@PathVariable Long id,
                                       @RequestParam BookingStatus status,
                                       RedirectAttributes redirectAttributes) {
        bookingService.updateStatus(id, status);
        redirectAttributes.addFlashAttribute("success", "Booking status updated!");
        return "redirect:/admin/bookings";
    }

    // ========== Guest Management ==========
    @GetMapping("/guests")
    public String listGuests(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/guests/list";
    }

    // ========== Messages ==========
    @GetMapping("/messages")
    public String listMessages(Model model) {
        model.addAttribute("messages", contactService.findAll());
        return "admin/messages/list";
    }
}
