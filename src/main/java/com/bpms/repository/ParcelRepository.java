package com.bpms.repository;

import com.bpms.entity.Parcel;
import com.bpms.entity.ParcelStatus;
import com.bpms.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParcelRepository extends JpaRepository<Parcel, Long> {

    Optional<Parcel> findFirstByParcelCodeAndParcelstatus(String parcelCode, ParcelStatus parcelstatus);

    Optional<Parcel> findFirstByParcelCodeOrderByArrivalTimeDesc(String parcelCode);

    boolean existsByParcelCodeAndParcelstatus(String parcelCode, ParcelStatus parcelstatus);

    List<Parcel> findByResidentAndParcelstatusOrderByArrivalTimeDesc(Resident resident, ParcelStatus parcelstatus);

    Optional<Parcel> findByRecipientRepresentativeToken(String recipientRepresentativeToken);

    /**
     * Diagram method: Parcel.queryParcel(code: String): Parcel.
     * Spring Data derived-query method names can't literally be
     * "queryParcel" and still be understood as a query - this is a thin
     * wrapper under the diagram's name delegating to the equivalent derived
     * query (most recent parcel for a given code).
     */
    default Optional<Parcel> queryParcel(String code) {
        return findFirstByParcelCodeOrderByArrivalTimeDesc(code);
    }
}
