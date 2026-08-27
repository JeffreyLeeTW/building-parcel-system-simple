package com.bpms.web;

import com.bpms.config.SessionKeys;
import com.bpms.entity.Parcel;
import com.bpms.entity.Parcelman;
import com.bpms.entity.PickupRecord;
import com.bpms.repository.ParcelRepository;
import com.bpms.repository.ParcelmanRepository;
import com.bpms.repository.PickupRecordRepository;
import com.bpms.service.PickupService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeFormatter;

@Controller
public class AdminPickupController {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    private final ParcelRepository parcelRepository;
    private final ParcelmanRepository parcelmanRepository;
    private final PickupRecordRepository pickupRecordRepository;
    private final PickupService pickupService;

    public AdminPickupController(ParcelRepository parcelRepository, ParcelmanRepository parcelmanRepository,
                                  PickupRecordRepository pickupRecordRepository, PickupService pickupService) {
        this.parcelRepository = parcelRepository;
        this.parcelmanRepository = parcelmanRepository;
        this.pickupRecordRepository = pickupRecordRepository;
        this.pickupService = pickupService;
    }

    @GetMapping("/admin/pickup")
    public String show(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(true);
        int step = step(session);
        model.addAttribute("step", step);

        if (step == 2 || step == 3) {
            Long parcelId = (Long) session.getAttribute(SessionKeys.PICKUP_PARCEL_ID);
            Parcel parcel = parcelId == null ? null : parcelRepository.findById(parcelId).orElse(null);
            if (parcel == null) {
                resetSession(session);
                model.addAttribute("step", 1);
            } else {
                model.addAttribute("parcel", parcel);
                model.addAttribute("verified", Boolean.TRUE.equals(session.getAttribute(SessionKeys.PICKUP_VERIFIED)));
            }
        }
        if (step == 4) {
            Long recordId = (Long) session.getAttribute(SessionKeys.PICKUP_LAST_RECORD_ID);
            PickupRecord record = recordId == null ? null : pickupRecordRepository.findById(recordId).orElse(null);
            if (record == null) {
                resetSession(session);
                model.addAttribute("step", 1);
            } else {
                model.addAttribute("record", record);
            }
        }
        return "admin/pickup";
    }

    private int step(HttpSession session) {
        Object v = session.getAttribute(SessionKeys.PICKUP_STEP);
        return v == null ? 1 : (int) v;
    }

    @PostMapping("/admin/pickup/search")
    public String search(@RequestParam String code, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(true);
        var outcome = pickupService.search(code.trim());

        if (outcome instanceof PickupService.Found found) {
            session.setAttribute(SessionKeys.PICKUP_PARCEL_ID, found.parcel().getId());
            session.setAttribute(SessionKeys.PICKUP_VERIFIED, false);
            session.setAttribute(SessionKeys.PICKUP_STEP, 2);
            return "redirect:/admin/pickup";
        }

        model.addAttribute("step", 1);
        model.addAttribute("submittedCode", code);
        if (outcome instanceof PickupService.AlreadyPicked already) {
            model.addAttribute("error", "此包裹已於 " + already.pickupTime().format(FORMAT) + " 完成簽收，不能再次領取。");
        } else {
            model.addAttribute("error", "找不到此包裹，請確認輸入的 4 位數包裹碼。");
        }
        return "admin/pickup";
    }

    @PostMapping("/admin/pickup/verify")
    public String verify(@RequestParam(required = false) String verified, HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        if (session.getAttribute(SessionKeys.PICKUP_PARCEL_ID) == null) {
            return "redirect:/admin/pickup";
        }
        session.setAttribute(SessionKeys.PICKUP_VERIFIED, "on".equals(verified));
        session.setAttribute(SessionKeys.PICKUP_STEP, 3);
        return "redirect:/admin/pickup";
    }

    @GetMapping("/admin/pickup/back-to-search")
    public String backToSearch(HttpServletRequest request) {
        resetSession(request.getSession(true));
        return "redirect:/admin/pickup";
    }

    @GetMapping("/admin/pickup/back-to-verify")
    public String backToVerify(HttpServletRequest request) {
        request.getSession(true).setAttribute(SessionKeys.PICKUP_STEP, 2);
        return "redirect:/admin/pickup";
    }

    @PostMapping("/admin/pickup/confirm")
    public String confirm(@RequestParam String photoData, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(true);
        Long parcelId = (Long) session.getAttribute(SessionKeys.PICKUP_PARCEL_ID);
        boolean verified = Boolean.TRUE.equals(session.getAttribute(SessionKeys.PICKUP_VERIFIED));
        Long parcelmanId = (Long) session.getAttribute(SessionKeys.PARCELMAN_ID);

        if (parcelId == null || !verified || photoData == null || photoData.isBlank()) {
            return "redirect:/admin/pickup";
        }
        Parcel parcel = parcelRepository.findById(parcelId).orElse(null);
        Parcelman admin = parcelmanRepository.findById(parcelmanId).orElse(null);
        if (parcel == null || admin == null || parcel.getParcelstatus() != com.bpms.entity.ParcelStatus.AVAILABLE) {
            resetSession(session);
            return "redirect:/admin/pickup";
        }

        PickupRecord record = pickupService.createPickupRecord(parcel, admin, photoData);

        session.setAttribute(SessionKeys.PICKUP_LAST_RECORD_ID, record.getPickupRecordId());
        session.removeAttribute(SessionKeys.PICKUP_PARCEL_ID);
        session.removeAttribute(SessionKeys.PICKUP_VERIFIED);
        session.setAttribute(SessionKeys.PICKUP_STEP, 4);
        return "redirect:/admin/pickup";
    }

    @GetMapping("/admin/pickup/next")
    public String next(HttpServletRequest request) {
        resetSession(request.getSession(true));
        return "redirect:/admin/pickup";
    }

    private void resetSession(HttpSession session) {
        session.removeAttribute(SessionKeys.PICKUP_PARCEL_ID);
        session.removeAttribute(SessionKeys.PICKUP_VERIFIED);
        session.removeAttribute(SessionKeys.PICKUP_LAST_RECORD_ID);
        session.setAttribute(SessionKeys.PICKUP_STEP, 1);
    }
}
