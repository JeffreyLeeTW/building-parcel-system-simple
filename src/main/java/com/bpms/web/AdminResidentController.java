package com.bpms.web;

import com.bpms.entity.Resident;
import com.bpms.repository.ResidentRepository;
import com.bpms.service.ResidentService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/residents")
public class AdminResidentController {

    private static final List<String> BUILDINGS = List.of("A", "B", "C");

    private final ResidentRepository residentRepository;
    private final ResidentService residentService;

    public AdminResidentController(ResidentRepository residentRepository, ResidentService residentService) {
        this.residentRepository = residentRepository;
        this.residentService = residentService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("residents", residentRepository.findAllByOrderByResidentIdAsc());
        addRoomOptions(model);
        return "admin/residents";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("residents", residentRepository.findAllByOrderByResidentIdAsc());
        addRoomOptions(model);
        model.addAttribute("showForm", true);
        model.addAttribute("editing", false);
        return "admin/residents";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("residents", residentRepository.findAllByOrderByResidentIdAsc());
        addRoomOptions(model);
        Resident resident = residentRepository.findById(id).orElse(null);
        if (resident == null) return "redirect:/admin/residents";
        model.addAttribute("showForm", true);
        model.addAttribute("editing", true);
        model.addAttribute("editResident", resident);
        return "admin/residents";
    }

    @GetMapping("/{id}/delete-confirm")
    public String deleteConfirm(@PathVariable Long id, Model model) {
        model.addAttribute("residents", residentRepository.findAllByOrderByResidentIdAsc());
        addRoomOptions(model);
        Resident resident = residentRepository.findById(id).orElse(null);
        if (resident == null) return "redirect:/admin/residents";
        model.addAttribute("deleteResident", resident);
        return "admin/residents";
    }

    @PostMapping
    public String create(@RequestParam String name, @RequestParam String buildingCode,
                          @RequestParam int floor, @RequestParam int unit,
                          @RequestParam String email, @RequestParam(required = false) String personalId,
                          @RequestParam String password,
                          RedirectAttributes redirectAttributes) {
        try {
            residentService.create(name.trim(), buildingCode, floor, unit, email.trim(),
                    trimToNull(personalId), password);
            redirectAttributes.addFlashAttribute("toast", "住戶已新增");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("toast", "此Email已被其他住戶使用");
        }
        return "redirect:/admin/residents";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @RequestParam String name, @RequestParam String buildingCode,
                          @RequestParam int floor, @RequestParam int unit,
                          @RequestParam String email, @RequestParam(required = false) String personalId,
                          @RequestParam(required = false) String password,
                          RedirectAttributes redirectAttributes) {
        try {
            residentService.update(id, name.trim(), buildingCode, floor, unit, email.trim(),
                    trimToNull(personalId), password);
            redirectAttributes.addFlashAttribute("toast", "住戶資料已修改");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("toast", "此Email已被其他住戶使用");
        }
        return "redirect:/admin/residents";
    }

    private String trimToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            residentService.delete(id);
            redirectAttributes.addFlashAttribute("toast", "住戶已刪除");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("toast", "此住戶尚有包裹紀錄，無法刪除");
        }
        return "redirect:/admin/residents";
    }

    private void addRoomOptions(Model model) {
        model.addAttribute("buildings", BUILDINGS);
        model.addAttribute("floors", java.util.stream.IntStream.rangeClosed(1, 20).boxed().toList());
        model.addAttribute("units", java.util.stream.IntStream.rangeClosed(1, 6).boxed().toList());
    }
}
