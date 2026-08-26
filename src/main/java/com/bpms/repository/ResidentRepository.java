package com.bpms.repository;

import com.bpms.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResidentRepository extends JpaRepository<Resident, Long> {
    Optional<Resident> findByEmail(String email);
    List<Resident> findByBuildingCodeAndFloorAndUnit(String buildingCode, Integer floor, Integer unit);
    List<Resident> findAllByOrderByIdAsc();
}
