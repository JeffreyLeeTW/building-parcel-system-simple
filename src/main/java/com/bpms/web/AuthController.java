package com.bpms.web;

import com.bpms.config.SessionKeys;
import com.bpms.entity.Parcelman;
import com.bpms.entity.Resident;
import com.bpms.repository.ParcelmanRepository;
import com.bpms.repository.ResidentRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final ResidentRepository residentRepository;
    private final ParcelmanRepository parcelmanRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(ResidentRepository residentRepository, ParcelmanRepository parcelmanRepository, PasswordEncoder passwordEncoder) {
        this.residentRepository = residentRepository;
        this.parcelmanRepository = parcelmanRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String access() {
        return "access";
    }

    @GetMapping("/login/resident")
    public String residentLoginForm() {
        return "login-resident";
    }

    @PostMapping("/login/resident")
    public String residentLogin(@RequestParam String email, @RequestParam String password,
                                 HttpServletRequest request, Model model) {
        var resident = residentRepository.findByEmail(email).orElse(null);
        if (resident == null || !passwordEncoder.matches(password, resident.getPasswordHash())) {
            model.addAttribute("error", "帳號或密碼錯誤");
            return "login-resident";
        }
        request.getSession(true).setAttribute(SessionKeys.RESIDENT_ID, resident.getId());
        return "redirect:/resident/dashboard";
    }

    @PostMapping("/resident/logout")
    public String residentLogout(HttpServletRequest request) {
        var session = request.getSession(false);
        if (session != null) session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/admin/login")
    public String adminLoginForm() {
        return "login-admin";
    }

    @PostMapping("/admin/login")
    public String adminLogin(@RequestParam String account, @RequestParam String password,
                              HttpServletRequest request, Model model) {
        var parcelman = parcelmanRepository.findByAccount(account).orElse(null);
        if (parcelman == null || !passwordEncoder.matches(password, parcelman.getPasswordHash())) {
            model.addAttribute("error", "帳號或密碼錯誤");
            return "login-admin";
        }
        request.getSession(true).setAttribute(SessionKeys.PARCELMAN_ID, parcelman.getId());
        return "redirect:/admin/pickup";
    }

    @PostMapping("/admin/logout")
    public String adminLogout(HttpServletRequest request) {
        var session = request.getSession(false);
        if (session != null) session.invalidate();
        return "redirect:/";
    }
}
