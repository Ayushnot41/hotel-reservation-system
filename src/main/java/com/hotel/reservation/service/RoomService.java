package com.hotel.reservation.service;

import com.hotel.reservation.model.Room;
import com.hotel.reservation.model.enums.RoomType;
import com.hotel.reservation.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public List<Room> findAvailable() {
        return roomRepository.findByAvailableTrue();
    }

    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    public Room save(Room room) {
        return roomRepository.save(room);
    }

    public void deleteById(Long id) {
        roomRepository.deleteById(id);
    }

    public List<Room> searchRooms(RoomType roomType, BigDecimal minPrice,
                                   BigDecimal maxPrice, Integer capacity, String city) {
        return roomRepository.searchRooms(roomType, minPrice, maxPrice, capacity, city);
    }

    public List<Room> findAvailableRooms(LocalDate checkIn, LocalDate checkOut,
                                          RoomType roomType, Integer capacity, String city) {
        return roomRepository.findAvailableRooms(checkIn, checkOut, roomType, capacity, city);
    }

    public List<String> findDistinctCities() {
        return roomRepository.findDistinctCities();
    }

    public long countRooms() {
        return roomRepository.count();
    }

    public boolean existsByRoomNumber(String roomNumber) {
        return roomRepository.existsByRoomNumber(roomNumber);
    }
}
