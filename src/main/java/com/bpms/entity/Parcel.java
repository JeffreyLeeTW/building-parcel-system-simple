package com.bpms.entity;

import com.bpms.repository.PickupRecordRepository;
import com.bpms.repository.ResidentRepository;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "parcel")
public class Parcel {

    public enum ParcelStatus {
        AVAILABLE,
        PICKED_UP
    }

    // Not in the UML diagram (primary keys are usually omitted there), left
    // as-is - every other class/service in this codebase keys off it.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parcel_code", nullable = false, length = 4)
    private String parcelCode;

    // Diagram spells this field "parcelstatus" (all lowercase, unlike normal
    // camelCase) - kept exactly as diagrammed. Type stays the ParcelStatus
    // enum rather than String for correctness; see updateStatus() below.
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ParcelStatus parcelstatus;

    @Column(name = "cabinet_area", nullable = false, length = 1)
    private String cabinetArea;

    @Column(name = "cabinet_number", nullable = false, length = 2)
    private String cabinetNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "recipient_representative_name")
    private String recipientRepresentativeName;

    @Column(name = "recipient_representative_token", unique = true)
    private String recipientRepresentativeToken;

    // Diagram field "parcelPhoto" does not exist here - the photo is
    // genuinely captured later, at pickup time, and lives on
    // PickupRecord.pickupPhoto instead. Not duplicated onto Parcel.

    public String getDisplayCode() {
        return "PKG-" + arrivalTime.toLocalDate().toString().replace("-", "") + "-" + String.format("%04d", id);
    }

    public String getCabinetLabel() {
        return cabinetArea + "-" + cabinetNumber;
    }

    public boolean isProxy() {
        return recipientRepresentativeName != null && !recipientRepresentativeName.isBlank();
    }

    /**
     * Diagram method: authorizeAgent(ParcelCode: String, AgentName: String): Void
     * (Agent renamed to Recipient's Representative).
     * Pure field mutation, no persistence needed here - callers still call
     * save() themselves. parcelCode is accepted to match the diagram's
     * signature; callers pass this parcel's own code.
     */
    public void authorizeRecipientRepresentative(String parcelCode, String recipientRepresentativeName) {
        this.recipientRepresentativeName = recipientRepresentativeName == null ? null : recipientRepresentativeName.trim();
    }

    /**
     * Diagram method: updateStatus(newStatus: String): Void.
     * The diagram implies a String parameter, but the backing field is the
     * ParcelStatus enum - kept as ParcelStatus here for correctness rather
     * than downgrading to String.
     * Also drives the diagram's Parcel -> PickupRecord: execute() call
     * (Ref: Create pickup record) - record and repository are passed in
     * rather than held as fields, so this entity keeps no persistent
     * dependency on Spring/JPA infrastructure beyond this one call.
     */
    public PickupRecord updateStatus(ParcelStatus newStatus, PickupRecord record, PickupRecordRepository pickupRecordRepository) {
        this.parcelstatus = newStatus;
        return record.execute(pickupRecordRepository);
    }

    /**
     * Diagram method: verifyCode(code: String): Boolean.
     * A narrow instance-level check: does the given code match this parcel's
     * own code while it is still available? This is much narrower than
     * PickupService.search(), which looks a code up across the whole
     * repository and distinguishes "already picked up" from "not found" -
     * that logic inherently needs the repository and stays there.
     */
    public boolean verifyCode(String code) {
        return parcelCode != null && parcelCode.equals(code) && parcelstatus == ParcelStatus.AVAILABLE;
    }

    /**
     * Diagram method: Parcel.queryResident(name: String): Resident.
     * The repository is passed in rather than held as a field, so this
     * entity keeps no persistent dependency on Spring/JPA infrastructure -
     * only this one call needs it.
     */
    public Resident queryResident(ResidentRepository residentRepository, String name) {
        return residentRepository.queryResident(name).orElseThrow();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getParcelCode() { return parcelCode; }
    public void setParcelCode(String parcelCode) { this.parcelCode = parcelCode; }
    public ParcelStatus getParcelstatus() { return parcelstatus; }
    public void setParcelstatus(ParcelStatus parcelstatus) { this.parcelstatus = parcelstatus; }
    public String getCabinetArea() { return cabinetArea; }
    public void setCabinetArea(String cabinetArea) { this.cabinetArea = cabinetArea; }
    public String getCabinetNumber() { return cabinetNumber; }
    public void setCabinetNumber(String cabinetNumber) { this.cabinetNumber = cabinetNumber; }
    public Resident getResident() { return resident; }
    public void setResident(Resident resident) { this.resident = resident; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }
    public String getRecipientRepresentativeName() { return recipientRepresentativeName; }
    public void setRecipientRepresentativeName(String recipientRepresentativeName) { this.recipientRepresentativeName = recipientRepresentativeName; }
    public String getRecipientRepresentativeToken() { return recipientRepresentativeToken; }
    public void setRecipientRepresentativeToken(String recipientRepresentativeToken) { this.recipientRepresentativeToken = recipientRepresentativeToken; }
}
