package com.hotel.reservation.controller;

import com.hotel.reservation.model.Room;
import com.hotel.reservation.model.enums.RoomType;
import com.hotel.reservation.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final RoomService roomService;

    @GetMapping("/")
    public String home(Model model) {
        List<Room> featuredRooms = roomService.findAvailable();
        if (featuredRooms.size() > 3) {
            featuredRooms = featuredRooms.subList(0, 3);
        }
        model.addAttribute("featuredRooms", featuredRooms);
        model.addAttribute("cities", roomService.findDistinctCities());
        model.addAttribute("roomTypes", RoomType.values());
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
