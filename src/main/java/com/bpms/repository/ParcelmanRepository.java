package com.bpms.repository;

import com.bpms.entity.Parcelman;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParcelmanRepository extends JpaRepository<Parcelman, Long> {
    Optional<Parcelman> findByParcelmanAccount(String parcelmanAccount);
}
