package com.example.patientcare.repository;

import com.example.patientcare.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    Optional<Patient> findByPatientId(String patientId);

    @Query("SELECT p FROM Patient p WHERE p.researchConsent = true")
    List<Patient> findByResearchConsentTrue();

    @Query("SELECT p FROM Patient p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "p.patientId LIKE CONCAT('%', :query, '%')")
    List<Patient> searchPatients(@Param("query") String query);

    Boolean existsByPatientId(String patientId);
    Boolean existsByEmail(String email);
}