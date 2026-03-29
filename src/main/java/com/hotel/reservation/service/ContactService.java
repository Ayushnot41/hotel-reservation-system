package com.hotel.reservation.service;

import com.hotel.reservation.model.ContactMessage;
import com.hotel.reservation.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactMessageRepository contactMessageRepository;

    public ContactMessage save(ContactMessage message) {
        return contactMessageRepository.save(message);
    }

    public List<ContactMessage> findAll() {
        return contactMessageRepository.findAllByOrderByCreatedAtDesc();
    }

    public long count() {
        return contactMessageRepository.count();
    }
}
