package com.bpms.web;

import com.bpms.config.SessionKeys;
import com.bpms.repository.ParcelmanRepository;
import com.bpms.repository.ResidentRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    private final ResidentRepository residentRepository;
    private final ParcelmanRepository parcelmanRepository;

    public GlobalModelAdvice(ResidentRepository residentRepository, ParcelmanRepository parcelmanRepository) {
        this.residentRepository = residentRepository;
        this.parcelmanRepository = parcelmanRepository;
    }

    @ModelAttribute
    public void addCurrentUser(HttpServletRequest request, Model model) {
        var session = request.getSession(false);
        if (session == null) return;

        Object residentId = session.getAttribute(SessionKeys.RESIDENT_ID);
        if (residentId != null) {
            residentRepository.findById((Long) residentId).ifPresent(r -> model.addAttribute("currentResident", r));
        }

        Object parcelmanId = session.getAttribute(SessionKeys.PARCELMAN_ID);
        if (parcelmanId != null) {
            parcelmanRepository.findById((Long) parcelmanId).ifPresent(p -> model.addAttribute("currentParcelman", p));
        }
    }
}
