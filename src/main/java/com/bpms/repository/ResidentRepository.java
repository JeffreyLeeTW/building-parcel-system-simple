package com.bpms.repository;

import com.bpms.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResidentRepository extends JpaRepository<Resident, Long> {
    Optional<Resident> findByResidentEmail(String residentEmail);
    List<Resident> findByBuildingCodeAndFloorAndUnit(String buildingCode, Integer floor, Integer unit);
    List<Resident> findAllByOrderByResidentIdAsc();
    Optional<Resident> findFirstByResidentName(String residentName);

    /**
     * Diagram method: Parcel.queryResident(name): Resident.
     * The actual repository lookup; Parcel.queryResident() delegates here.
     */
    default Optional<Resident> queryResident(String name) {
        return findFirstByResidentName(name);
    }
}
