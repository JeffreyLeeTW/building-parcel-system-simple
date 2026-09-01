package com.bpms.web;

import com.bpms.entity.Parcel;
import com.bpms.entity.Parcel.ParcelStatus;
import com.bpms.repository.ParcelRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RecipientRepresentativeAuthorizeController {

    private final ParcelRepository parcelRepository;

    public RecipientRepresentativeAuthorizeController(ParcelRepository parcelRepository) {
        this.parcelRepository = parcelRepository;
    }

    @GetMapping("/recipient-representative-authorize")
    public String form(@RequestParam String token, Model model) {
        Parcel parcel = parcelRepository.findByRecipientRepresentativeToken(token).orElse(null);
        model.addAttribute("token", token);
        if (parcel == null) {
            model.addAttribute("error", "此連結無效，請確認是否已過期或曾經使用過。");
        } else if (parcel.getParcelstatus() != ParcelStatus.AVAILABLE) {
            model.addAttribute("error", "此包裹已完成領取，無法再設定代領人。");
        } else {
            model.addAttribute("parcel", parcel);
        }
        return "recipient-representative-authorize";
    }

    @PostMapping("/recipient-representative-authorize")
    public String submit(@RequestParam String token, @RequestParam String recipientRepresentativeName, Model model) {
        Parcel parcel = parcelRepository.findByRecipientRepresentativeToken(token).orElse(null);
        model.addAttribute("token", token);
        if (parcel == null) {
            model.addAttribute("error", "此連結無效，請確認是否已過期或曾經使用過。");
            return "recipient-representative-authorize";
        }
        if (parcel.getParcelstatus() != ParcelStatus.AVAILABLE) {
            model.addAttribute("error", "此包裹已完成領取，無法再設定代領人。");
            return "recipient-representative-authorize";
        }
        parcel.authorizeRecipientRepresentative(parcel.getParcelCode(), recipientRepresentativeName);
        parcelRepository.save(parcel);
        model.addAttribute("done", true);
        model.addAttribute("parcel", parcel);
        return "recipient-representative-authorize";
    }
}
