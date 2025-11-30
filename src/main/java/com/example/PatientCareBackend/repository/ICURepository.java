package com.example.PatientCareBackend.repository;

import com.example.PatientCareBackend.model.ICU;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ICURepository extends JpaRepository<ICU, Long> {
    List<ICU> findByPatientId(Long patientId);

    @Query("SELECT i FROM ICU i WHERE i.patient.id = :patientId ORDER BY i.createdAt DESC LIMIT 1")
    Optional<ICU> findLatestByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT i FROM ICU i WHERE i.heartRate < 60 OR i.heartRate > 100 OR " +
            "i.bloodPressureSystolic < 90 OR i.bloodPressureSystolic > 140 OR " +
            "i.oxygenSaturation < 90")
    List<ICU> findCriticalReadings();

    @Query("SELECT i FROM ICU i WHERE i.createdAt BETWEEN :startTime AND :endTime AND i.patient.id = :patientId")
    List<ICU> findByPatientAndTimeRange(@Param("patientId") Long patientId,
                                        @Param("startTime") LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime);
}