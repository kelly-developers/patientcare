package com.example.PatientCareBackend.repository;

import com.example.PatientCareBackend.model.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LabTestRepository extends JpaRepository<LabTest, Long> {
    List<LabTest> findByPatientId(Long patientId);
    List<LabTest> findByStatus(LabTest.TestStatus status);
    List<LabTest> findByPriority(LabTest.Priority priority);
    List<LabTest> findByTestType(String testType);

    @Query("SELECT lt FROM LabTest lt WHERE lt.orderedBy = :orderedBy")
    List<LabTest> findByOrderedBy(@Param("orderedBy") String orderedBy);

    @Query("SELECT lt FROM LabTest lt WHERE lt.priority = 'URGENT' AND lt.status != 'COMPLETED'")
    List<LabTest> findPendingUrgentTests();

    @Query("SELECT lt FROM LabTest lt WHERE lt.orderedDate BETWEEN :startDate AND :endDate")
    List<LabTest> findTestsOrderedBetween(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);
}