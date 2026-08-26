package com.bpms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pickup_record")
public class PickupRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "parcel_id", nullable = false)
    private Parcel parcel;

    @Column(name = "pickup_time", nullable = false)
    private LocalDateTime pickupTime;

    @Lob
    @Column(name = "pickup_photo")
    private byte[] pickupPhoto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "handling_parcelman_id", nullable = false)
    private Parcelman handlingParcelman;

    @Column(name = "actual_picker_name", nullable = false)
    private String actualPickerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "pickup_method", nullable = false, length = 20)
    private PickupMethod pickupMethod;

    @Column(name = "signer_note", nullable = false)
    private String signerNote;

    public String getDisplayId() {
        return "REC-" + String.format("%06d", id);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Parcel getParcel() { return parcel; }
    public void setParcel(Parcel parcel) { this.parcel = parcel; }
    public LocalDateTime getPickupTime() { return pickupTime; }
    public void setPickupTime(LocalDateTime pickupTime) { this.pickupTime = pickupTime; }
    public byte[] getPickupPhoto() { return pickupPhoto; }
    public void setPickupPhoto(byte[] pickupPhoto) { this.pickupPhoto = pickupPhoto; }
    public Parcelman getHandlingParcelman() { return handlingParcelman; }
    public void setHandlingParcelman(Parcelman handlingParcelman) { this.handlingParcelman = handlingParcelman; }
    public String getActualPickerName() { return actualPickerName; }
    public void setActualPickerName(String actualPickerName) { this.actualPickerName = actualPickerName; }
    public PickupMethod getPickupMethod() { return pickupMethod; }
    public void setPickupMethod(PickupMethod pickupMethod) { this.pickupMethod = pickupMethod; }
    public String getSignerNote() { return signerNote; }
    public void setSignerNote(String signerNote) { this.signerNote = signerNote; }
}
