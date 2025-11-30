package com.example.PatientCareBackend.repository;

import com.example.PatientCareBackend.model.PostOperative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostOperativeRepository extends JpaRepository<PostOperative, Long> {
    List<PostOperative> findByPatientId(Long patientId);
    List<PostOperative> findBySurgeryId(Long surgeryId);
    List<PostOperative> findByFollowupType(PostOperative.FollowupType followupType);

    @Query("SELECT p FROM PostOperative p WHERE p.medicationAdherence = false")
    List<PostOperative> findNonAdherentPatients();

    @Query("SELECT p FROM PostOperative p WHERE p.nextVisitDate IS NOT NULL AND p.nextVisitDate < CURRENT_DATE")
    List<PostOperative> findOverdueFollowups();
}