package com.example.patientcare.repository;

import com.example.patientcare.entity.VitalData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface VitalDataRepository extends JpaRepository<VitalData, UUID> {

    // Find all vital data for a patient ordered by most recent
    List<VitalData> findByPatientIdOrderByRecordedAtDesc(UUID patientId);

    // Paginated version
    Page<VitalData> findByPatientIdOrderByRecordedAtDesc(UUID patientId, Pageable pageable);

    // Find vital data recorded by a specific user
    List<VitalData> findByRecordedByIdOrderByRecordedAtDesc(UUID recordedById);

    // Find vital data for a patient within a date range
    @Query("SELECT v FROM VitalData v WHERE v.patient.id = :patientId AND v.recordedAt BETWEEN :startDate AND :endDate ORDER BY v.recordedAt DESC")
    List<VitalData> findByPatientIdAndDateRange(@Param("patientId") UUID patientId,
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);

    // Count vital data entries for a patient
    @Query("SELECT COUNT(v) FROM VitalData v WHERE v.patient.id = :patientId")
    Long countByPatientId(@Param("patientId") UUID patientId);

    // Find vital data by patient ID string (using patient.patientId)
    @Query("SELECT v FROM VitalData v WHERE v.patient.patientId = :patientId ORDER BY v.recordedAt DESC")
    List<VitalData> findByPatientPatientIdOrderByRecordedAtDesc(@Param("patientId") String patientId);

    // Find latest vital data for a patient
    @Query("SELECT v FROM VitalData v WHERE v.patient.id = :patientId ORDER BY v.recordedAt DESC LIMIT 1")
    VitalData findLatestByPatientId(@Param("patientId") UUID patientId);

    // Find vital data with critical values (for alerting)
    @Query("SELECT v FROM VitalData v WHERE " +
            "(v.systolicBP > 180 OR v.systolicBP < 90 OR " +
            "v.diastolicBP > 120 OR v.diastolicBP < 60 OR " +
            "v.heartRate > 100 OR v.heartRate < 50 OR " +
            "v.temperature > 38.5 OR v.oxygenSaturation < 92) " +
            "AND v.recordedAt > :sinceDate " +
            "ORDER BY v.recordedAt DESC")
    List<VitalData> findCriticalVitalData(@Param("sinceDate") LocalDateTime sinceDate);
}