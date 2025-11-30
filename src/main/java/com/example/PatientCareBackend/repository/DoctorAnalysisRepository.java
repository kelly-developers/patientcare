package com.example.PatientCareBackend.repository;

import com.example.PatientCareBackend.model.DoctorAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorAnalysisRepository extends JpaRepository<DoctorAnalysis, Long> {
    List<DoctorAnalysis> findByPatientId(Long patientId);
    List<DoctorAnalysis> findByDoctorId(Long doctorId);
    List<DoctorAnalysis> findByRecommendSurgeryTrue();
    List<DoctorAnalysis> findByStatus(DoctorAnalysis.AnalysisStatus status);
}