package com.hotel.reservation.controller;

import com.hotel.reservation.dto.RoomSearchDto;
import com.hotel.reservation.model.Room;
import com.hotel.reservation.model.enums.RoomType;
import com.hotel.reservation.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.findAvailable());
        model.addAttribute("roomTypes", RoomType.values());
        model.addAttribute("searchDto", new RoomSearchDto());
        model.addAttribute("cities", roomService.findDistinctCities());
        return "rooms/list";
    }

    @GetMapping("/{id}")
    public String roomDetail(@PathVariable Long id, Model model) {
        Room room = roomService.findById(id);
        model.addAttribute("room", room);
        return "rooms/detail";
    }

    @GetMapping("/search")
    public String searchRooms(@ModelAttribute RoomSearchDto searchDto, Model model) {
        List<Room> rooms;
        if (searchDto.getCheckIn() != null && searchDto.getCheckOut() != null) {
            rooms = roomService.findAvailableRooms(
                    searchDto.getCheckIn(), searchDto.getCheckOut(),
                    searchDto.getRoomType(), searchDto.getCapacity(),
                    searchDto.getCity());
        } else {
            rooms = roomService.searchRooms(
                    searchDto.getRoomType(), searchDto.getMinPrice(),
                    searchDto.getMaxPrice(), searchDto.getCapacity(),
                    searchDto.getCity());
        }
        model.addAttribute("rooms", rooms);
        model.addAttribute("roomTypes", RoomType.values());
        model.addAttribute("searchDto", searchDto);
        model.addAttribute("cities", roomService.findDistinctCities());
        return "rooms/list";
    }
}
