package com.example.PatientCareBackend.repository;

import com.example.PatientCareBackend.model.SurgicalDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurgicalDecisionRepository extends JpaRepository<SurgicalDecision, Long> {
    List<SurgicalDecision> findBySurgeryId(Long surgeryId);
    List<SurgicalDecision> findBySurgeonName(String surgeonName);
    List<SurgicalDecision> findByDecisionStatus(SurgicalDecision.DecisionStatus decisionStatus);

    long countBySurgeryIdAndDecisionStatus(Long surgeryId, SurgicalDecision.DecisionStatus decisionStatus);
}