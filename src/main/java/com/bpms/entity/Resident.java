package com.bpms.entity;

import com.bpms.service.MailService;
import jakarta.persistence.*;

@Entity
@Table(name = "resident")
public class Resident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long residentId;

    @Column(name = "name", nullable = false)
    private String residentName;

    @Column(name = "building_code", nullable = false, length = 1)
    private String buildingCode;

    @Column(nullable = false)
    private Integer floor;

    @Column(nullable = false)
    private Integer unit;

    @Column(name = "email", nullable = false, unique = true)
    private String residentEmail;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "personal_id")
    private String residentPersonalId;

    public String getRoomLabel() {
        return buildingCode + "棟 " + floor + "F-" + unit;
    }

    /**
     * Diagram method: Resident.getEmail(): String.
     * Kept under this exact name (rather than getResidentEmail) because the
     * UML class diagram names it explicitly; delegates to residentEmail.
     */
    public String getEmail() { return residentEmail; }

    /**
     * Diagram method: Resident.sendNotification(): Void.
     * The mail service is passed in rather than held as a field, so this
     * entity keeps no persistent dependency on Spring/JPA infrastructure -
     * only this one call needs it. Overloaded with the pickup-confirmation
     * variant below, matching MailService's two sendNotification() methods.
     */
    public void sendNotification(MailService mailService, Parcel parcel) {
        mailService.sendNotification(this, parcel);
    }

    /**
     * Diagram method: Resident.sendNotification(): Void.
     * The pickup-completion notification; see the overload above for the
     * arrival notification.
     */
    public void sendNotification(MailService mailService, Parcel parcel, PickupRecord record) {
        mailService.sendNotification(this, parcel, record);
    }

    public Long getResidentId() { return residentId; }
    public void setResidentId(Long residentId) { this.residentId = residentId; }
    public String getResidentName() { return residentName; }
    public void setResidentName(String residentName) { this.residentName = residentName; }
    public String getBuildingCode() { return buildingCode; }
    public void setBuildingCode(String buildingCode) { this.buildingCode = buildingCode; }
    public Integer getFloor() { return floor; }
    public void setFloor(Integer floor) { this.floor = floor; }
    public Integer getUnit() { return unit; }
    public void setUnit(Integer unit) { this.unit = unit; }
    public String getResidentEmail() { return residentEmail; }
    public void setResidentEmail(String residentEmail) { this.residentEmail = residentEmail; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getResidentPersonalId() { return residentPersonalId; }
    public void setResidentPersonalId(String residentPersonalId) { this.residentPersonalId = residentPersonalId; }
}
