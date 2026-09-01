package com.bpms.service;

import com.bpms.entity.Parcel;
import com.bpms.entity.Parcel.ParcelStatus;
import com.bpms.entity.Resident;
import com.bpms.repository.ParcelRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ParcelService {

    private final ParcelRepository parcelRepository;
    private final MailService mailService;
    private final SecureRandom random = new SecureRandom();

    public ParcelService(ParcelRepository parcelRepository, MailService mailService) {
        this.parcelRepository = parcelRepository;
        this.mailService = mailService;
    }

    public Parcel registerParcel(Resident resident, String cabinetArea, String cabinetNumber) {
        Parcel parcel = new Parcel();
        parcel.setResident(resident);
        parcel.setParcelCode(generateUniqueCode());
        parcel.setParcelstatus(ParcelStatus.AVAILABLE);
        parcel.setCabinetArea(cabinetArea);
        parcel.setCabinetNumber(cabinetNumber);
        parcel.setArrivalTime(LocalDateTime.now());
        parcel.setRecipientRepresentativeToken(UUID.randomUUID().toString().replace("-", ""));
        parcel = parcelRepository.save(parcel);
        resident.sendNotification(mailService, parcel);
        return parcel;
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = String.format("%04d", random.nextInt(10000));
        } while (parcelRepository.existsByParcelCodeAndParcelstatus(code, ParcelStatus.AVAILABLE));
        return code;
    }
}
