package com.example.PatientCareBackend.service;

import com.example.PatientCareBackend.dto.request.PreOperativeRequest;
import com.example.PatientCareBackend.dto.response.PreOperativeResponse;
import com.example.PatientCareBackend.exception.ResourceNotFoundException;
import com.example.PatientCareBackend.model.Patient;
import com.example.PatientCareBackend.model.PreOperative;
import com.example.PatientCareBackend.repository.PatientRepository;
import com.example.PatientCareBackend.repository.PreOperativeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreOperativeService {

    private final PreOperativeRepository preOperativeRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public PreOperativeResponse submitChecklist(PreOperativeRequest checklistRequest) {
        Patient patient = patientRepository.findById(checklistRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + checklistRequest.getPatientId()));

        PreOperative checklist = new PreOperative();
        checklist.setPatient(patient);
        checklist.setProcedureName(checklistRequest.getProcedureName());

        // Patient Identity
        checklist.setPatientIdentityConfirmed(checklistRequest.getPatientIdentityConfirmed());
        checklist.setConsentSigned(checklistRequest.getConsentSigned());
        checklist.setSiteMarked(checklistRequest.getSiteMarked());

        // Anesthesia Safety
        checklist.setAnesthesiaMachineChecked(checklistRequest.getAnesthesiaMachineChecked());
        checklist.setOxygenAvailable(checklistRequest.getOxygenAvailable());
        checklist.setSuctionAvailable(checklistRequest.getSuctionAvailable());

        // Patient Assessment
        checklist.setKnownAllergy(checklistRequest.getKnownAllergy());
        checklist.setDifficultAirway(checklistRequest.getDifficultAirway());
        checklist.setAspirationRisk(checklistRequest.getAspirationRisk());
        checklist.setBloodLossRisk(checklistRequest.getBloodLossRisk());

        // Equipment
        checklist.setSterileIndicatorsConfirmed(checklistRequest.getSterileIndicatorsConfirmed());
        checklist.setEquipmentIssues(checklistRequest.getEquipmentIssues());
        checklist.setImplantAvailable(checklistRequest.getImplantAvailable());

        // Team Confirmation
        checklist.setNurseConfirmed(checklistRequest.getNurseConfirmed());
        checklist.setAnesthetistConfirmed(checklistRequest.getAnesthetistConfirmed());
        checklist.setSurgeonConfirmed(checklistRequest.getSurgeonConfirmed());

        // Research Consent
        checklist.setResearchConsentGiven(checklistRequest.getResearchConsentGiven());
        checklist.setDataUsageConsent(checklistRequest.getDataUsageConsent());
        checklist.setSampleStorageConsent(checklistRequest.getSampleStorageConsent());
        checklist.setResearchConsentDate(checklistRequest.getResearchConsentDate());
        checklist.setResearchConsentWitness(checklistRequest.getResearchConsentWitness());

        // Additional Info
        checklist.setAdditionalConcerns(checklistRequest.getAdditionalConcerns());
        checklist.setCompletedBy(checklistRequest.getCompletedBy());

        PreOperative savedChecklist = preOperativeRepository.save(checklist);
        return mapToResponse(savedChecklist);
    }

    @Transactional(readOnly = true)
    public PreOperativeResponse getChecklistByPatient(Long patientId) {
        PreOperative checklist = preOperativeRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Pre-operative checklist not found for patient id: " + patientId));
        return mapToResponse(checklist);
    }

    @Transactional(readOnly = true)
    public PreOperativeResponse getChecklistById(Long id) {
        PreOperative checklist = preOperativeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pre-operative checklist not found with id: " + id));
        return mapToResponse(checklist);
    }

    @Transactional(readOnly = true)
    public List<PreOperativeResponse> getAllChecklists() {
        return preOperativeRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PreOperativeResponse updateChecklist(Long id, PreOperativeRequest checklistRequest) {
        PreOperative checklist = preOperativeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pre-operative checklist not found with id: " + id));

        Patient patient = patientRepository.findById(checklistRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + checklistRequest.getPatientId()));

        checklist.setPatient(patient);
        checklist.setProcedureName(checklistRequest.getProcedureName());

        // Update all fields
        checklist.setPatientIdentityConfirmed(checklistRequest.getPatientIdentityConfirmed());
        checklist.setConsentSigned(checklistRequest.getConsentSigned());
        checklist.setSiteMarked(checklistRequest.getSiteMarked());
        checklist.setAnesthesiaMachineChecked(checklistRequest.getAnesthesiaMachineChecked());
        checklist.setOxygenAvailable(checklistRequest.getOxygenAvailable());
        checklist.setSuctionAvailable(checklistRequest.getSuctionAvailable());
        checklist.setKnownAllergy(checklistRequest.getKnownAllergy());
        checklist.setDifficultAirway(checklistRequest.getDifficultAirway());
        checklist.setAspirationRisk(checklistRequest.getAspirationRisk());
        checklist.setBloodLossRisk(checklistRequest.getBloodLossRisk());
        checklist.setSterileIndicatorsConfirmed(checklistRequest.getSterileIndicatorsConfirmed());
        checklist.setEquipmentIssues(checklistRequest.getEquipmentIssues());
        checklist.setImplantAvailable(checklistRequest.getImplantAvailable());
        checklist.setNurseConfirmed(checklistRequest.getNurseConfirmed());
        checklist.setAnesthetistConfirmed(checklistRequest.getAnesthetistConfirmed());
        checklist.setSurgeonConfirmed(checklistRequest.getSurgeonConfirmed());
        checklist.setResearchConsentGiven(checklistRequest.getResearchConsentGiven());
        checklist.setDataUsageConsent(checklistRequest.getDataUsageConsent());
        checklist.setSampleStorageConsent(checklistRequest.getSampleStorageConsent());
        checklist.setResearchConsentDate(checklistRequest.getResearchConsentDate());
        checklist.setResearchConsentWitness(checklistRequest.getResearchConsentWitness());
        checklist.setAdditionalConcerns(checklistRequest.getAdditionalConcerns());
        checklist.setCompletedBy(checklistRequest.getCompletedBy());

        PreOperative updatedChecklist = preOperativeRepository.save(checklist);
        return mapToResponse(updatedChecklist);
    }

    @Transactional(readOnly = true)
    public boolean isChecklistComplete(Long patientId) {
        return preOperativeRepository.findByPatientId(patientId)
                .map(checklist ->
                        checklist.getPatientIdentityConfirmed() &&
                                checklist.getConsentSigned() &&
                                checklist.getSiteMarked() &&
                                checklist.getAnesthesiaMachineChecked() &&
                                checklist.getOxygenAvailable() &&
                                checklist.getSuctionAvailable() &&
                                checklist.getSterileIndicatorsConfirmed() &&
                                checklist.getNurseConfirmed() &&
                                checklist.getAnesthetistConfirmed() &&
                                checklist.getSurgeonConfirmed()
                )
                .orElse(false);
    }

    private PreOperativeResponse mapToResponse(PreOperative checklist) {
        return new PreOperativeResponse(
                checklist.getId(),
                mapToPatientResponse(checklist.getPatient()),
                checklist.getProcedureName(),
                checklist.getPatientIdentityConfirmed(),
                checklist.getConsentSigned(),
                checklist.getSiteMarked(),
                checklist.getAnesthesiaMachineChecked(),
                checklist.getOxygenAvailable(),
                checklist.getSuctionAvailable(),
                checklist.getKnownAllergy(),
                checklist.getDifficultAirway(),
                checklist.getAspirationRisk(),
                checklist.getBloodLossRisk(),
                checklist.getSterileIndicatorsConfirmed(),
                checklist.getEquipmentIssues(),
                checklist.getImplantAvailable(),
                checklist.getNurseConfirmed(),
                checklist.getAnesthetistConfirmed(),
                checklist.getSurgeonConfirmed(),
                checklist.getResearchConsentGiven(),
                checklist.getDataUsageConsent(),
                checklist.getSampleStorageConsent(),
                checklist.getResearchConsentDate(),
                checklist.getResearchConsentWitness(),
                checklist.getAdditionalConcerns(),
                checklist.getCompletedBy(),
                checklist.getCompletedAt()
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
                patient.getConsentAccepted(), // This should be Boolean, not String
                patient.getConsentFormPath(), // This should be String
                patient.getResearchConsent(), // This should be Boolean
                patient.getSampleStorageConsent(), // This should be Boolean
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }
}