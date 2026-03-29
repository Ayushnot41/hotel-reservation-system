package com.hotel.reservation.dto;

import com.hotel.reservation.model.enums.RoomType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RoomSearchDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkIn;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOut;

    private RoomType roomType;
    private Integer capacity;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String city;
}
