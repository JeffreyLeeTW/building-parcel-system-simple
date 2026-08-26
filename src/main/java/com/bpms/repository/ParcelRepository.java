package com.bpms.repository;

import com.bpms.entity.Parcel;
import com.bpms.entity.ParcelStatus;
import com.bpms.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParcelRepository extends JpaRepository<Parcel, Long> {

    Optional<Parcel> findFirstByParcelCodeAndStatus(String parcelCode, ParcelStatus status);

    Optional<Parcel> findFirstByParcelCodeOrderByArrivalTimeDesc(String parcelCode);

    boolean existsByParcelCodeAndStatus(String parcelCode, ParcelStatus status);

    List<Parcel> findByResidentAndStatusOrderByArrivalTimeDesc(Resident resident, ParcelStatus status);

    Optional<Parcel> findByAgentToken(String agentToken);
}
