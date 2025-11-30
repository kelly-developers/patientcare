package com.example.PatientCareBackend.repository;

import com.example.PatientCareBackend.model.Surgery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SurgeryRepository extends JpaRepository<Surgery, Long> {
    List<Surgery> findByPatientId(Long patientId);
    List<Surgery> findByStatus(Surgery.SurgeryStatus status);
    List<Surgery> findByUrgency(Surgery.SurgeryUrgency urgency);

    @Query("SELECT s FROM Surgery s WHERE s.scheduledDate BETWEEN :startDate AND :endDate")
    List<Surgery> findSurgeriesBetweenDates(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT s FROM Surgery s WHERE s.surgeonName = :surgeonName")
    List<Surgery> findBySurgeonName(@Param("surgeonName") String surgeonName);

    @Query("SELECT s FROM Surgery s WHERE s.status = 'PENDING_CONSENT'")
    List<Surgery> findPendingConsentSurgeries();
}