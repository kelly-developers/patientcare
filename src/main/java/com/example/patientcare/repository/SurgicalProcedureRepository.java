package com.example.patientcare.repository;

import com.example.patientcare.entity.Patient;
import com.example.patientcare.entity.RefreshToken;
import com.example.patientcare.entity.SurgicalProcedure;
import com.example.patientcare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;



@Repository
public interface SurgicalProcedureRepository extends JpaRepository<SurgicalProcedure, UUID> {
    List<SurgicalProcedure> findByPatientId(UUID patientId);
    List<SurgicalProcedure> findByStatus(SurgicalProcedure.ProcedureStatus status);
}

