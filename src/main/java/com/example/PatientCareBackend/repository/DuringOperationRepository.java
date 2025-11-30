package com.example.PatientCareBackend.repository;

import com.example.PatientCareBackend.model.DuringOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DuringOperationRepository extends JpaRepository<DuringOperation, Long> {
    Optional<DuringOperation> findBySurgeryId(Long surgeryId);
    List<DuringOperation> findByPatientId(Long patientId);
    List<DuringOperation> findByStatus(DuringOperation.OperationStatus status);

    @Query("SELECT d FROM DuringOperation d WHERE d.status = 'IN_PROGRESS'")
    List<DuringOperation> findActiveOperations();

    @Query("SELECT d FROM DuringOperation d WHERE d.patient.id = :patientId ORDER BY d.startTime DESC")
    List<DuringOperation> findRecentOperationsByPatient(@Param("patientId") Long patientId);
}