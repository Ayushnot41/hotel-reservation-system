package com.hotel.reservation.config;

import com.hotel.reservation.model.Room;
import com.hotel.reservation.model.User;
import com.hotel.reservation.model.enums.Role;
import com.hotel.reservation.model.enums.RoomType;
import com.hotel.reservation.repository.RoomRepository;
import com.hotel.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin@hotel.com")) {
            User admin = User.builder()
                    .name("Admin")
                    .email("admin@hotel.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("9999999999")
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
        }

        if (roomRepository.count() == 0) {
            roomRepository.save(Room.builder()
                    .roomNumber("101").roomType(RoomType.SINGLE)
                    .pricePerNight(new BigDecimal("2500"))
                    .capacity(1).description("Cozy single room with a city view, work desk, and complimentary Wi-Fi. Perfect for solo business travellers.")
                    .amenities("Wi-Fi,TV,AC,Mini Bar,Work Desk").available(true)
                    .city("Mumbai").location("Colaba, South Mumbai")
                    .imageUrl("https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=800").build());

            roomRepository.save(Room.builder()
                    .roomNumber("201").roomType(RoomType.DOUBLE)
                    .pricePerNight(new BigDecimal("4500"))
                    .capacity(2).description("Spacious double room with queen-size bed, balcony, and stunning garden view. Ideal for couples.")
                    .amenities("Wi-Fi,TV,AC,Mini Bar,Balcony,Room Service").available(true)
                    .city("Delhi").location("Connaught Place, New Delhi")
                    .imageUrl("https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=800").build());

            roomRepository.save(Room.builder()
                    .roomNumber("301").roomType(RoomType.DELUXE)
                    .pricePerNight(new BigDecimal("7500"))
                    .capacity(2).description("Luxurious deluxe room with king-size bed, premium bath amenities, and breathtaking pool view.")
                    .amenities("Wi-Fi,TV,AC,Mini Bar,Balcony,Room Service,Jacuzzi,Lounge Access").available(true)
                    .city("Goa").location("Candolim Beach, North Goa")
                    .imageUrl("https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=800").build());

            roomRepository.save(Room.builder()
                    .roomNumber("401").roomType(RoomType.SUITE)
                    .pricePerNight(new BigDecimal("12000"))
                    .capacity(3).description("Elegant presidential suite with separate living area, premium bath, panoramic sea views, and 24/7 butler service.")
                    .amenities("Wi-Fi,TV,AC,Mini Bar,Balcony,Room Service,Jacuzzi,Lounge Access,Butler Service").available(true)
                    .city("Jaipur").location("Amer Road, Jaipur")
                    .imageUrl("https://images.unsplash.com/photo-1590490360182-c33d57733427?w=800").build());

            roomRepository.save(Room.builder()
                    .roomNumber("501").roomType(RoomType.FAMILY)
                    .pricePerNight(new BigDecimal("9000"))
                    .capacity(4).description("Spacious family room with two double beds, kids' corner, and family-friendly amenities for a memorable stay.")
                    .amenities("Wi-Fi,TV,AC,Mini Bar,Room Service,Extra Beds,Kids Corner").available(true)
                    .city("Mumbai").location("Juhu Beach, Mumbai")
                    .imageUrl("https://images.unsplash.com/photo-1566665797739-1674de7a421a?w=800").build());

            roomRepository.save(Room.builder()
                    .roomNumber("601").roomType(RoomType.DELUXE)
                    .pricePerNight(new BigDecimal("8500"))
                    .capacity(2).description("Premium ocean-facing deluxe room with private balcony, rain shower, and complimentary breakfast included.")
                    .amenities("Wi-Fi,TV,AC,Mini Bar,Balcony,Breakfast,Rain Shower,Ocean View").available(true)
                    .city("Goa").location("Baga Beach, North Goa")
                    .imageUrl("https://images.unsplash.com/photo-1618773928121-c32242e63f39?w=800").build());

            roomRepository.save(Room.builder()
                    .roomNumber("701").roomType(RoomType.SUITE)
                    .pricePerNight(new BigDecimal("15000"))
                    .capacity(2).description("Maharaja Suite with royal interiors, private pool access, heritage decor, and exclusive dining experience.")
                    .amenities("Wi-Fi,TV,AC,Mini Bar,Private Pool,Butler Service,Heritage Decor,Fine Dining").available(true)
                    .city("Udaipur").location("Lake Pichola, Udaipur")
                    .imageUrl("https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=800").build());

            roomRepository.save(Room.builder()
                    .roomNumber("801").roomType(RoomType.DOUBLE)
                    .pricePerNight(new BigDecimal("5500"))
                    .capacity(2).description("Modern double room with city skyline views, smart TV, high-speed internet, and contemporary design.")
                    .amenities("Wi-Fi,Smart TV,AC,Mini Bar,City View,Coffee Machine").available(true)
                    .city("Bangalore").location("MG Road, Bangalore")
                    .imageUrl("https://images.unsplash.com/photo-1595576508898-0ad5c879a061?w=800").build());
        }
    }
}
