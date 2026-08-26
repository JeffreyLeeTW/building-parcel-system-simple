package com.bpms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "parcel")
public class Parcel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parcel_code", nullable = false, length = 4)
    private String parcelCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ParcelStatus status;

    @Column(name = "cabinet_area", nullable = false, length = 1)
    private String cabinetArea;

    @Column(name = "cabinet_number", nullable = false, length = 2)
    private String cabinetNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "agent_name")
    private String agentName;

    @Column(name = "agent_token", unique = true)
    private String agentToken;

    public String getDisplayCode() {
        return "PKG-" + arrivalTime.toLocalDate().toString().replace("-", "") + "-" + String.format("%04d", id);
    }

    public String getCabinetLabel() {
        return cabinetArea + "-" + cabinetNumber;
    }

    public boolean isProxy() {
        return agentName != null && !agentName.isBlank();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getParcelCode() { return parcelCode; }
    public void setParcelCode(String parcelCode) { this.parcelCode = parcelCode; }
    public ParcelStatus getStatus() { return status; }
    public void setStatus(ParcelStatus status) { this.status = status; }
    public String getCabinetArea() { return cabinetArea; }
    public void setCabinetArea(String cabinetArea) { this.cabinetArea = cabinetArea; }
    public String getCabinetNumber() { return cabinetNumber; }
    public void setCabinetNumber(String cabinetNumber) { this.cabinetNumber = cabinetNumber; }
    public Resident getResident() { return resident; }
    public void setResident(Resident resident) { this.resident = resident; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }
    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }
    public String getAgentToken() { return agentToken; }
    public void setAgentToken(String agentToken) { this.agentToken = agentToken; }
}
