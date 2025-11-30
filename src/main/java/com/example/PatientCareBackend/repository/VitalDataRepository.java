package com.example.PatientCareBackend.repository;

import com.example.PatientCareBackend.model.VitalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VitalDataRepository extends JpaRepository<VitalData, Long> {
    List<VitalData> findByPatientId(Long patientId);
    List<VitalData> findByRecordedBy(String recordedBy);

    @Query("SELECT v FROM VitalData v WHERE v.riskLevel = 'CRITICAL' OR v.riskLevel = 'HIGH'")
    List<VitalData> findCriticalVitals();

    @Query("SELECT v FROM VitalData v WHERE v.patient.id = :patientId AND v.createdAt BETWEEN :startTime AND :endTime ORDER BY v.createdAt DESC")
    List<VitalData> findByPatientAndTimeRange(@Param("patientId") Long patientId,
                                              @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);

    @Query("SELECT v FROM VitalData v WHERE v.patient.id = :patientId ORDER BY v.createdAt DESC LIMIT :limit")
    List<VitalData> findRecentByPatient(@Param("patientId") Long patientId, @Param("limit") int limit);
}