package com.example.PatientCareBackend.repository;

import com.example.PatientCareBackend.model.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
    List<Pharmacy> findByPatientId(Long patientId);
    List<Pharmacy> findByDoctorId(Long doctorId);
    List<Pharmacy> findByStatus(Pharmacy.PrescriptionStatus status);

    @Query("SELECT p FROM Pharmacy p WHERE p.status = 'PENDING'")
    List<Pharmacy> findPendingPrescriptions();

    @Query("SELECT p FROM Pharmacy p WHERE p.medicationName LIKE %:medicationName%")
    List<Pharmacy> findByMedicationNameContaining(@Param("medicationName") String medicationName);

    @Query("SELECT p FROM Pharmacy p WHERE p.patient.id = :patientId AND p.status != 'COLLECTED'")
    List<Pharmacy> findActivePrescriptionsByPatient(@Param("patientId") Long patientId);
}