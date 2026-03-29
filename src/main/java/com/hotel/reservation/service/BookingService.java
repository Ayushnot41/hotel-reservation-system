package com.hotel.reservation.service;

import com.hotel.reservation.model.Booking;
import com.hotel.reservation.model.Room;
import com.hotel.reservation.model.User;
import com.hotel.reservation.model.enums.BookingStatus;
import com.hotel.reservation.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    @Transactional
    public Booking createBooking(User user, Room room, Booking booking) {
        // Check for overlapping bookings
        if (bookingRepository.existsOverlappingBooking(room.getId(),
                booking.getCheckInDate(), booking.getCheckOutDate())) {
            throw new RuntimeException("Room is not available for the selected dates");
        }

        // Calculate total price
        long nights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        BigDecimal totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));

        booking.setUser(user);
        booking.setRoom(room);
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.PENDING);

        return bookingRepository.save(booking);
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public List<Booking> findByUserId(Long userId) {
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Booking> findAll() {
        return bookingRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public Booking updateStatus(Long id, BookingStatus status) {
        Booking booking = findById(id);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public long countBookings() {
        return bookingRepository.count();
    }

    public long countByStatus(BookingStatus status) {
        return bookingRepository.countByStatus(status);
    }

    public boolean isRoomAvailable(Long roomId, java.time.LocalDate checkIn, java.time.LocalDate checkOut) {
        return !bookingRepository.existsOverlappingBooking(roomId, checkIn, checkOut);
    }
}
