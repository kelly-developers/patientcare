package com.example.PatientCareBackend.repository;

import com.example.PatientCareBackend.model.PreOperative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreOperativeRepository extends JpaRepository<PreOperative, Long> {
    Optional<PreOperative> findByPatientId(Long patientId);
    List<PreOperative> findByProcedureName(String procedureName);
}