package com.bpms.service;

import com.bpms.entity.Parcel;
import com.bpms.entity.ParcelStatus;
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
        parcel.setStatus(ParcelStatus.AVAILABLE);
        parcel.setCabinetArea(cabinetArea);
        parcel.setCabinetNumber(cabinetNumber);
        parcel.setArrivalTime(LocalDateTime.now());
        parcel.setAgentToken(UUID.randomUUID().toString().replace("-", ""));
        parcel = parcelRepository.save(parcel);
        mailService.sendArrivalNotification(resident, parcel);
        return parcel;
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = String.format("%04d", random.nextInt(10000));
        } while (parcelRepository.existsByParcelCodeAndStatus(code, ParcelStatus.AVAILABLE));
        return code;
    }
}
