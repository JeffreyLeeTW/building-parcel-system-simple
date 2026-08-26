package com.bpms.web;

import com.bpms.entity.Parcel;
import com.bpms.entity.ParcelStatus;
import com.bpms.repository.ParcelRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AgentAuthorizeController {

    private final ParcelRepository parcelRepository;

    public AgentAuthorizeController(ParcelRepository parcelRepository) {
        this.parcelRepository = parcelRepository;
    }

    @GetMapping("/agent-authorize")
    public String form(@RequestParam String token, Model model) {
        Parcel parcel = parcelRepository.findByAgentToken(token).orElse(null);
        model.addAttribute("token", token);
        if (parcel == null) {
            model.addAttribute("error", "此連結無效，請確認是否已過期或曾經使用過。");
        } else if (parcel.getStatus() != ParcelStatus.AVAILABLE) {
            model.addAttribute("error", "此包裹已完成領取，無法再設定代領人。");
        } else {
            model.addAttribute("parcel", parcel);
        }
        return "agent-authorize";
    }

    @PostMapping("/agent-authorize")
    public String submit(@RequestParam String token, @RequestParam String agentName, Model model) {
        Parcel parcel = parcelRepository.findByAgentToken(token).orElse(null);
        model.addAttribute("token", token);
        if (parcel == null) {
            model.addAttribute("error", "此連結無效，請確認是否已過期或曾經使用過。");
            return "agent-authorize";
        }
        if (parcel.getStatus() != ParcelStatus.AVAILABLE) {
            model.addAttribute("error", "此包裹已完成領取，無法再設定代領人。");
            return "agent-authorize";
        }
        parcel.setAgentName(agentName.trim());
        parcelRepository.save(parcel);
        model.addAttribute("done", true);
        model.addAttribute("parcel", parcel);
        return "agent-authorize";
    }
}
