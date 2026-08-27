package com.bpms.web;

import com.bpms.config.SessionKeys;
import com.bpms.repository.ConciergeRepository;
import com.bpms.repository.ResidentRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    private final ResidentRepository residentRepository;
    private final ConciergeRepository conciergeRepository;

    public GlobalModelAdvice(ResidentRepository residentRepository, ConciergeRepository conciergeRepository) {
        this.residentRepository = residentRepository;
        this.conciergeRepository = conciergeRepository;
    }

    @ModelAttribute
    public void addCurrentUser(HttpServletRequest request, Model model) {
        var session = request.getSession(false);
        if (session == null) return;

        Object residentId = session.getAttribute(SessionKeys.RESIDENT_ID);
        if (residentId != null) {
            residentRepository.findById((Long) residentId).ifPresent(r -> model.addAttribute("currentResident", r));
        }

        Object conciergeId = session.getAttribute(SessionKeys.CONCIERGE_ID);
        if (conciergeId != null) {
            conciergeRepository.findById((Long) conciergeId).ifPresent(p -> model.addAttribute("currentConcierge", p));
        }
    }
}
