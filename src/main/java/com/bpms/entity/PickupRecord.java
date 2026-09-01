package com.bpms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pickup_record")
public class PickupRecord {

    public enum PickupMethod {
        SELF,
        RECIPIENT_REPRESENTATIVE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long pickupRecordId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "parcel_id", nullable = false)
    private Parcel parcel;

    @Column(name = "pickup_time", nullable = false)
    private LocalDateTime pickupTime;

    // Diagram says type Image; the codebase stores the photo captured from
    // the admin's camera as raw bytes decoded from a base64 data URL, so
    // byte[] is kept rather than introducing an Image abstraction.
    // No @Lob: that maps to a PostgreSQL large object (oid), which can only
    // be read inside a transaction and broke reads made outside one (e.g.
    // ResidentController.dashboard()). Plain byte[] maps to bytea instead.
    @Column(name = "pickup_photo")
    private byte[] pickupPhoto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "handling_concierge_id", nullable = false)
    private Concierge handlingConcierge;

    @Column(name = "actual_picker_name", nullable = false)
    private String actualPickerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "pickup_method", nullable = false, length = 30)
    private PickupMethod pickupMethod;

    @Column(name = "signer_note", nullable = false)
    private String signerNote;

    public String getDisplayId() {
        return "REC-" + String.format("%06d", pickupRecordId);
    }

    /**
     * Diagram method: uploadPickupPhoto(photo: Image): Void.
     * Pure field mutation; byte[] kept in place of Image (see field comment
     * above).
     */
    public void uploadPickupPhoto(byte[] photo) {
        this.pickupPhoto = photo;
    }

    public Long getPickupRecordId() { return pickupRecordId; }
    public void setPickupRecordId(Long pickupRecordId) { this.pickupRecordId = pickupRecordId; }
    public Parcel getParcel() { return parcel; }
    public void setParcel(Parcel parcel) { this.parcel = parcel; }
    public LocalDateTime getPickupTime() { return pickupTime; }
    public void setPickupTime(LocalDateTime pickupTime) { this.pickupTime = pickupTime; }
    public byte[] getPickupPhoto() { return pickupPhoto; }
    public void setPickupPhoto(byte[] pickupPhoto) { this.pickupPhoto = pickupPhoto; }
    public Concierge getHandlingConcierge() { return handlingConcierge; }
    public void setHandlingConcierge(Concierge handlingConcierge) { this.handlingConcierge = handlingConcierge; }
    public String getActualPickerName() { return actualPickerName; }
    public void setActualPickerName(String actualPickerName) { this.actualPickerName = actualPickerName; }
    public PickupMethod getPickupMethod() { return pickupMethod; }
    public void setPickupMethod(PickupMethod pickupMethod) { this.pickupMethod = pickupMethod; }
    public String getSignerNote() { return signerNote; }
    public void setSignerNote(String signerNote) { this.signerNote = signerNote; }
}
