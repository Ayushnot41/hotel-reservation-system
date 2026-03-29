package com.hotel.reservation.repository;

import com.hotel.reservation.model.Booking;
import com.hotel.reservation.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Booking> findByStatus(BookingStatus status);

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
           "WHERE b.room.id = :roomId " +
           "AND b.status <> com.hotel.reservation.model.enums.BookingStatus.CANCELLED " +
           "AND b.checkInDate < :checkOut " +
           "AND b.checkOutDate > :checkIn")
    boolean existsOverlappingBooking(@Param("roomId") Long roomId,
                                      @Param("checkIn") LocalDate checkIn,
                                      @Param("checkOut") LocalDate checkOut);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = :status")
    long countByStatus(@Param("status") BookingStatus status);

    List<Booking> findAllByOrderByCreatedAtDesc();
}

