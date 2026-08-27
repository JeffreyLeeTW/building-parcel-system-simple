package com.bpms.web;

import com.bpms.config.SessionKeys;
import com.bpms.entity.Parcel;
import com.bpms.entity.ParcelStatus;
import com.bpms.entity.Resident;
import com.bpms.repository.ParcelRepository;
import com.bpms.repository.PickupRecordRepository;
import com.bpms.repository.ResidentRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ResidentController {

    private final ResidentRepository residentRepository;
    private final ParcelRepository parcelRepository;
    private final PickupRecordRepository pickupRecordRepository;

    public ResidentController(ResidentRepository residentRepository, ParcelRepository parcelRepository,
                               PickupRecordRepository pickupRecordRepository) {
        this.residentRepository = residentRepository;
        this.parcelRepository = parcelRepository;
        this.pickupRecordRepository = pickupRecordRepository;
    }

    @GetMapping("/resident/dashboard")
    public String dashboard(HttpServletRequest request, Model model) {
        Resident resident = currentResident(request);
        var mine = parcelRepository.findByResidentAndParcelstatusOrderByArrivalTimeDesc(resident, ParcelStatus.AVAILABLE);
        var records = pickupRecordRepository.findByParcel_ResidentOrderByPickupTimeDesc(resident);
        model.addAttribute("parcels", mine);
        model.addAttribute("records", records);
        return "resident/dashboard";
    }

    @GetMapping("/resident/parcels/{id}/recipient-representative-form")
    public String recipientRepresentativeForm(@PathVariable Long id, HttpServletRequest request, Model model) {
        Resident resident = currentResident(request);
        var mine = parcelRepository.findByResidentAndParcelstatusOrderByArrivalTimeDesc(resident, ParcelStatus.AVAILABLE);
        var records = pickupRecordRepository.findByParcel_ResidentOrderByPickupTimeDesc(resident);
        model.addAttribute("parcels", mine);
        model.addAttribute("records", records);

        Parcel parcel = parcelRepository.findById(id).orElse(null);
        if (parcel == null || !parcel.getResident().getResidentId().equals(resident.getResidentId())) {
            return "redirect:/resident/dashboard";
        }
        model.addAttribute("recipientRepresentativeParcel", parcel);
        return "resident/dashboard";
    }

    @PostMapping("/resident/parcels/{id}/recipient-representative")
    public String setRecipientRepresentative(@PathVariable Long id, @RequestParam String recipientRepresentativeName,
                            HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Resident resident = currentResident(request);
        Parcel parcel = parcelRepository.findById(id).orElse(null);
        if (parcel == null || !parcel.getResident().getResidentId().equals(resident.getResidentId())
                || parcel.getParcelstatus() != ParcelStatus.AVAILABLE) {
            return "redirect:/resident/dashboard";
        }
        parcel.authorizeRecipientRepresentative(parcel.getParcelCode(), recipientRepresentativeName);
        parcelRepository.save(parcel);
        redirectAttributes.addFlashAttribute("toast", "代領授權已完成");
        return "redirect:/resident/dashboard";
    }

    private Resident currentResident(HttpServletRequest request) {
        Long id = (Long) request.getSession(true).getAttribute(SessionKeys.RESIDENT_ID);
        return residentRepository.findById(id).orElseThrow();
    }
}
