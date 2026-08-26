package com.bpms.web;

import com.bpms.repository.ResidentRepository;
import com.bpms.service.ParcelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminRegisterController {

    private static final List<String> BUILDINGS = List.of("A", "B", "C");
    private static final List<String> CABINET_AREAS = List.of("A", "B", "C", "D");

    private final ResidentRepository residentRepository;
    private final ParcelService parcelService;

    public AdminRegisterController(ResidentRepository residentRepository, ParcelService parcelService) {
        this.residentRepository = residentRepository;
        this.parcelService = parcelService;
    }

    @GetMapping("/admin/register")
    public String form(Model model) {
        model.addAttribute("buildings", BUILDINGS);
        model.addAttribute("floors", floorRange());
        model.addAttribute("units", unitRange());
        model.addAttribute("cabinetAreas", CABINET_AREAS);
        model.addAttribute("cabinetNumbers", cabinetNumberRange());
        model.addAttribute("residents", residentOptions());
        return "admin/register-parcel";
    }

    @PostMapping("/admin/register")
    public String submit(@RequestParam Long residentId,
                          @RequestParam String cabinetArea,
                          @RequestParam String cabinetNumber,
                          RedirectAttributes redirectAttributes) {
        var resident = residentRepository.findById(residentId).orElse(null);
        if (resident == null) {
            redirectAttributes.addFlashAttribute("toast", "請選擇此房號對應的住戶");
            return "redirect:/admin/register";
        }
        var parcel = parcelService.registerParcel(resident, cabinetArea, cabinetNumber);
        redirectAttributes.addFlashAttribute("toast", "包裹已登記，包裹碼 " + parcel.getParcelCode() + " 已寄出");
        return "redirect:/admin/register";
    }

    private List<Integer> floorRange() {
        return java.util.stream.IntStream.rangeClosed(1, 20).boxed().toList();
    }

    private List<Integer> unitRange() {
        return java.util.stream.IntStream.rangeClosed(1, 6).boxed().toList();
    }

    private List<String> cabinetNumberRange() {
        return java.util.stream.IntStream.rangeClosed(1, 20).mapToObj(i -> String.format("%02d", i)).toList();
    }

    private List<Map<String, Object>> residentOptions() {
        return residentRepository.findAllByOrderByIdAsc().stream().map(r -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", r.getId());
            m.put("name", r.getName());
            m.put("buildingCode", r.getBuildingCode());
            m.put("floor", r.getFloor());
            m.put("unit", r.getUnit());
            return m;
        }).toList();
    }
}
