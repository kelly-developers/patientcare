package com.example.PatientCareBackend.service;

import com.example.PatientCareBackend.dto.request.SurgicalDecisionRequest;
import com.example.PatientCareBackend.dto.response.SurgicalDecisionResponse;
import com.example.PatientCareBackend.exception.ResourceNotFoundException;
import com.example.PatientCareBackend.model.Patient;
import com.example.PatientCareBackend.model.SurgicalDecision;
import com.example.PatientCareBackend.model.Surgery;
import com.example.PatientCareBackend.repository.SurgicalDecisionRepository;
import com.example.PatientCareBackend.repository.SurgeryRepository;
import com.example.PatientCareBackend.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurgicalDecisionService {

    private final SurgicalDecisionRepository surgicalDecisionRepository;
    private final SurgeryRepository surgeryRepository;

    @Transactional
    public SurgicalDecisionResponse submitDecision(SurgicalDecisionRequest decisionRequest) {
        Surgery surgery = surgeryRepository.findById(decisionRequest.getSurgeryId())
                .orElseThrow(() -> new ResourceNotFoundException("Surgery not found with id: " + decisionRequest.getSurgeryId()));

        SurgicalDecision decision = new SurgicalDecision();
        decision.setSurgery(surgery);
        decision.setSurgeonName(decisionRequest.getSurgeonName());
        decision.setDecisionStatus(decisionRequest.getDecisionStatus());
        decision.setComments(decisionRequest.getComments());

        // Convert Map to JSON string
        decision.setFactorsConsidered(JsonUtil.toJson(decisionRequest.getFactorsConsidered()));

        SurgicalDecision savedDecision = surgicalDecisionRepository.save(decision);
        return mapToResponse(savedDecision);
    }

    @Transactional(readOnly = true)
    public List<SurgicalDecisionResponse> getDecisionsBySurgery(Long surgeryId) {
        if (!surgeryRepository.existsById(surgeryId)) {
            throw new ResourceNotFoundException("Surgery not found with id: " + surgeryId);
        }

        return surgicalDecisionRepository.findBySurgeryId(surgeryId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getDecisionConsensus(Long surgeryId) {
        if (!surgeryRepository.existsById(surgeryId)) {
            throw new ResourceNotFoundException("Surgery not found with id: " + surgeryId);
        }

        List<SurgicalDecision> decisions = surgicalDecisionRepository.findBySurgeryId(surgeryId);

        long acceptedCount = decisions.stream()
                .filter(d -> d.getDecisionStatus() == SurgicalDecision.DecisionStatus.ACCEPTED)
                .count();

        long declinedCount = decisions.stream()
                .filter(d -> d.getDecisionStatus() == SurgicalDecision.DecisionStatus.DECLINED)
                .count();

        return Map.of(
                "totalDecisions", decisions.size(),
                "accepted", acceptedCount,
                "declined", declinedCount,
                "consensusReached", decisions.size() >= 3 && acceptedCount >= 2,
                "requiresMoreReviews", decisions.size() < 3
        );
    }

    @Transactional(readOnly = true)
    public SurgicalDecisionResponse getDecisionById(Long id) {
        SurgicalDecision decision = surgicalDecisionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Surgical decision not found with id: " + id));
        return mapToResponse(decision);
    }

    @Transactional(readOnly = true)
    public List<SurgicalDecisionResponse> getDecisionsBySurgeon(String surgeonName) {
        return surgicalDecisionRepository.findBySurgeonName(surgeonName).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean hasConsensusForSurgery(Long surgeryId) {
        Map<String, Object> consensus = getDecisionConsensus(surgeryId);
        return (Boolean) consensus.get("consensusReached");
    }

    private SurgicalDecisionResponse mapToResponse(SurgicalDecision decision) {
        SurgicalDecisionResponse response = new SurgicalDecisionResponse();
        response.setId(decision.getId());
        response.setSurgery(mapToSurgeryResponse(decision.getSurgery()));
        response.setSurgeonName(decision.getSurgeonName());
        response.setDecisionStatus(decision.getDecisionStatus());
        response.setComments(decision.getComments());

        // Convert JSON string back to Map
        response.setFactorsConsidered(JsonUtil.fromJson(decision.getFactorsConsidered()));
        response.setCreatedAt(decision.getCreatedAt());

        return response;
    }

    private com.example.PatientCareBackend.dto.response.SurgeryResponse mapToSurgeryResponse(Surgery surgery) {
        return new com.example.PatientCareBackend.dto.response.SurgeryResponse(
                surgery.getId(),
                mapToPatientResponse(surgery.getPatient()),
                surgery.getProcedureName(),
                surgery.getUrgency(),
                surgery.getRecommendedBy(),
                surgery.getDiagnosis(),
                surgery.getStatus(),
                surgery.getConsentDate(),
                surgery.getScheduledDate(),
                surgery.getActualDate(),
                surgery.getCompletedDate(),
                surgery.getSurgeonName(),
                surgery.getDurationMinutes(),
                surgery.getCreatedAt()
        );
    }

    private com.example.PatientCareBackend.dto.response.PatientResponse mapToPatientResponse(Patient patient) {
        return new com.example.PatientCareBackend.dto.response.PatientResponse(
                patient.getId(),
                patient.getPatientId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getAddress(),
                patient.getEmergencyContactName(),
                patient.getEmergencyContactPhone(),
                patient.getMedicalHistory(),
                patient.getAllergies(),
                patient.getCurrentMedications(),
                patient.getConsentAccepted(),  // Position 15: Boolean
                patient.getConsentFormPath(),  // Position 16: String
                patient.getResearchConsent(),  // Position 17: Boolean
                patient.getSampleStorageConsent(),  // Position 18: Boolean
                patient.getCreatedAt(),        // Position 19: LocalDateTime
                patient.getUpdatedAt()         // Position 20: LocalDateTime
        );
    }
}