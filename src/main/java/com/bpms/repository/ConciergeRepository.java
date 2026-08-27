package com.bpms.repository;

import com.bpms.entity.Concierge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConciergeRepository extends JpaRepository<Concierge, Long> {
    Optional<Concierge> findByConciergeAccount(String conciergeAccount);
}
