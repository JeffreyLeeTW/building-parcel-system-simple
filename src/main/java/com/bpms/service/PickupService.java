package com.bpms.service;

import com.bpms.entity.*;
import com.bpms.repository.ParcelRepository;
import com.bpms.repository.PickupRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class PickupService {

    private final ParcelRepository parcelRepository;
    private final PickupRecordRepository pickupRecordRepository;
    private final MailService mailService;

    public PickupService(ParcelRepository parcelRepository, PickupRecordRepository pickupRecordRepository, MailService mailService) {
        this.parcelRepository = parcelRepository;
        this.pickupRecordRepository = pickupRecordRepository;
        this.mailService = mailService;
    }

    public sealed interface SearchResult permits Found, AlreadyPicked, NotFound {}
    public record Found(Parcel parcel) implements SearchResult {}
    public record AlreadyPicked(LocalDateTime pickupTime) implements SearchResult {}
    public record NotFound() implements SearchResult {}

    public SearchResult search(String code) {
        Optional<Parcel> available = parcelRepository.findFirstByParcelCodeAndStatus(code, ParcelStatus.AVAILABLE);
        if (available.isPresent()) {
            return new Found(available.get());
        }
        Optional<Parcel> recent = parcelRepository.findFirstByParcelCodeOrderByArrivalTimeDesc(code);
        if (recent.isPresent() && recent.get().getStatus() == ParcelStatus.PICKED_UP) {
            LocalDateTime pickupTime = pickupRecordRepository.findFirstByParcelOrderByPickupTimeDesc(recent.get())
                    .map(PickupRecord::getPickupTime)
                    .orElse(recent.get().getArrivalTime());
            return new AlreadyPicked(pickupTime);
        }
        return new NotFound();
    }

    public PickupRecord confirmPickup(Parcel parcel, Parcelman admin, String photoDataUrl) {
        byte[] photoBytes = decodeDataUrl(photoDataUrl);

        PickupMethod method = parcel.isProxy() ? PickupMethod.AGENT : PickupMethod.SELF;
        String actualPickerName = parcel.isProxy() ? parcel.getAgentName() : parcel.getResident().getName();
        String signerNote = parcel.isProxy() ? "由代領人簽收" : "由領受人本人簽收";

        PickupRecord record = new PickupRecord();
        record.setParcel(parcel);
        record.setPickupTime(LocalDateTime.now());
        record.setPickupPhoto(photoBytes);
        record.setHandlingParcelman(admin);
        record.setActualPickerName(actualPickerName);
        record.setPickupMethod(method);
        record.setSignerNote(signerNote);
        record = pickupRecordRepository.save(record);

        parcel.setStatus(ParcelStatus.PICKED_UP);
        parcelRepository.save(parcel);

        mailService.sendPickupConfirmation(parcel.getResident(), parcel, record);
        return record;
    }

    private byte[] decodeDataUrl(String dataUrl) {
        if (dataUrl == null) return new byte[0];
        int comma = dataUrl.indexOf(',');
        String base64 = comma >= 0 ? dataUrl.substring(comma + 1) : dataUrl;
        try {
            return Base64.getDecoder().decode(base64);
        } catch (IllegalArgumentException e) {
            return new byte[0];
        }
    }
}
