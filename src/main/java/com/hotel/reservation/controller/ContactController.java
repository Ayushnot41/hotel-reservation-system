package com.hotel.reservation.controller;

import com.hotel.reservation.model.ContactMessage;
import com.hotel.reservation.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public String contactPage(Model model) {
        model.addAttribute("message", new ContactMessage());
        return "contact";
    }

    @PostMapping
    public String submitContact(@Valid @ModelAttribute("message") ContactMessage message,
                                BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "contact";
        }
        contactService.save(message);
        redirectAttributes.addFlashAttribute("success", "Message sent successfully! We'll get back to you soon.");
        return "redirect:/contact";
    }
}
