package com.hotel.reservation.service;

import com.hotel.reservation.model.Booking;
import com.hotel.reservation.model.Payment;
import com.hotel.reservation.model.enums.BookingStatus;
import com.hotel.reservation.model.enums.PaymentMethod;
import com.hotel.reservation.model.enums.PaymentStatus;
import com.hotel.reservation.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingService bookingService;

    @Transactional
    public Payment processPayment(Booking booking, PaymentMethod paymentMethod) {
        Payment payment = Payment.builder()
                .booking(booking)
                .amount(booking.getTotalPrice())
                .paymentMethod(paymentMethod)
                .paymentStatus(PaymentStatus.COMPLETED)
                .transactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .paidAt(LocalDateTime.now())
                .build();

        payment = paymentRepository.save(payment);

        // Update booking status to confirmed after payment
        bookingService.updateStatus(booking.getId(), BookingStatus.CONFIRMED);

        return payment;
    }

    public Optional<Payment> findByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }
}
