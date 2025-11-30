package com.example.PatientCareBackend.repository;

import com.example.PatientCareBackend.model.Consent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsentRepository extends JpaRepository<Consent, Long> {
    Optional<Consent> findBySurgeryId(Long surgeryId);
    List<Consent> findByConsentDecision(Consent.ConsentDecision consentDecision);
    boolean existsBySurgeryIdAndConsentDecision(Long surgeryId, Consent.ConsentDecision consentDecision);
}