package com.bpms.entity;

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
