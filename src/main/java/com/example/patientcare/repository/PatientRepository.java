package com.example.patientcare.repository;

import com.example.patientcare.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByPatientId(String patientId);
    Optional<Patient> findByEmail(String email);
    Boolean existsByPatientId(String patientId);
    Boolean existsByEmail(String email);

    List<Patient> findByResearchConsentTrue();

    @Query("SELECT p FROM Patient p WHERE " +
            "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.patientId) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.email) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Patient> searchPatients(@Param("query") String query);

    Page<Patient> findAll(Pageable pageable);

    List<Patient> findByIdIn(List<Long> ids);
}