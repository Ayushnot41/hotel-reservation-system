package com.hotel.reservation.repository;

import com.hotel.reservation.model.Room;
import com.hotel.reservation.model.enums.BookingStatus;
import com.hotel.reservation.model.enums.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByAvailableTrue();

    List<Room> findByRoomType(RoomType roomType);

    @Query("SELECT r FROM Room r WHERE r.available = true " +
           "AND (:roomType IS NULL OR r.roomType = :roomType) " +
           "AND (:minPrice IS NULL OR r.pricePerNight >= :minPrice) " +
           "AND (:maxPrice IS NULL OR r.pricePerNight <= :maxPrice) " +
           "AND (:capacity IS NULL OR r.capacity >= :capacity) " +
           "AND (:city IS NULL OR :city = '' OR LOWER(r.city) = LOWER(:city))")
    List<Room> searchRooms(@Param("roomType") RoomType roomType,
                           @Param("minPrice") BigDecimal minPrice,
                           @Param("maxPrice") BigDecimal maxPrice,
                           @Param("capacity") Integer capacity,
                           @Param("city") String city);

    @Query("SELECT r FROM Room r WHERE r.available = true " +
           "AND (:roomType IS NULL OR r.roomType = :roomType) " +
           "AND (:capacity IS NULL OR r.capacity >= :capacity) " +
           "AND (:city IS NULL OR :city = '' OR LOWER(r.city) = LOWER(:city)) " +
           "AND r.id NOT IN (" +
           "  SELECT b.room.id FROM Booking b " +
           "  WHERE b.status <> com.hotel.reservation.model.enums.BookingStatus.CANCELLED " +
           "  AND b.checkInDate < :checkOut " +
           "  AND b.checkOutDate > :checkIn" +
           ")")
    List<Room> findAvailableRooms(@Param("checkIn") LocalDate checkIn,
                                  @Param("checkOut") LocalDate checkOut,
                                  @Param("roomType") RoomType roomType,
                                  @Param("capacity") Integer capacity,
                                  @Param("city") String city);

    @Query("SELECT DISTINCT r.city FROM Room r WHERE r.city IS NOT NULL ORDER BY r.city")
    List<String> findDistinctCities();

    boolean existsByRoomNumber(String roomNumber);
}

