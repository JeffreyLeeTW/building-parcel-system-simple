package com.bpms.repository;

import com.bpms.entity.Parcel;
import com.bpms.entity.PickupRecord;
import com.bpms.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PickupRecordRepository extends JpaRepository<PickupRecord, Long> {
    List<PickupRecord> findByParcel_ResidentOrderByPickupTimeDesc(Resident resident);
    Optional<PickupRecord> findFirstByParcelOrderByPickupTimeDesc(Parcel parcel);
}
