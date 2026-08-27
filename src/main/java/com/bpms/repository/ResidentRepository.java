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
     * Placed here since it inherently needs a repository lookup; a thin
     * wrapper under the diagram's name delegating to the derived query
     * above.
     */
    default Optional<Resident> queryResident(String name) {
        return findFirstByResidentName(name);
    }
}
