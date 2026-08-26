package com.bpms.service;

import com.bpms.entity.Resident;
import com.bpms.repository.ResidentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ResidentService {

    private final ResidentRepository residentRepository;
    private final PasswordEncoder passwordEncoder;

    public ResidentService(ResidentRepository residentRepository, PasswordEncoder passwordEncoder) {
        this.residentRepository = residentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Resident create(String name, String buildingCode, int floor, int unit, String email,
                            String personalId, String rawPassword) {
        Resident resident = new Resident();
        resident.setName(name);
        resident.setBuildingCode(buildingCode);
        resident.setFloor(floor);
        resident.setUnit(unit);
        resident.setEmail(email);
        resident.setPersonalId(personalId);
        resident.setPasswordHash(passwordEncoder.encode(rawPassword));
        return residentRepository.save(resident);
    }

    public Resident update(Long id, String name, String buildingCode, int floor, int unit, String email,
                            String personalId, String rawPassword) {
        Resident resident = residentRepository.findById(id).orElseThrow();
        resident.setName(name);
        resident.setBuildingCode(buildingCode);
        resident.setFloor(floor);
        resident.setUnit(unit);
        resident.setEmail(email);
        resident.setPersonalId(personalId);
        if (rawPassword != null && !rawPassword.isBlank()) {
            resident.setPasswordHash(passwordEncoder.encode(rawPassword));
        }
        return residentRepository.save(resident);
    }

    public void delete(Long id) {
        residentRepository.deleteById(id);
    }
}
