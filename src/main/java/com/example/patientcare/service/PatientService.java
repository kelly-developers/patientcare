package com.example.patientcare.service;

import com.example.patientcare.dto.request.PatientRequest;
import com.example.patientcare.dto.response.PatientResponse;
import com.example.patientcare.entity.Patient;
import com.example.patientcare.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PatientResponse getPatientById(String id) {
        Patient patient = patientRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        return mapToResponse(patient);
    }

    public PatientResponse getPatientByPatientId(String patientId) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with patient ID: " + patientId));
        return mapToResponse(patient);
    }

    public PatientResponse createPatient(PatientRequest request) {
        Patient patient = mapToEntity(request);
        patient.generatePatientId();

        // Set consent timestamp if research consent is given
        if (Boolean.TRUE.equals(request.getResearchConsent()) && request.getResearchConsentDate() == null) {
            patient.setResearchConsentDate(LocalDateTime.now());
        }

        Patient savedPatient = patientRepository.save(patient);
        return mapToResponse(savedPatient);
    }

    public PatientResponse updatePatient(String id, PatientRequest request) {
        Patient existingPatient = patientRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        updateEntityFromRequest(existingPatient, request);
        Patient updatedPatient = patientRepository.save(existingPatient);
        return mapToResponse(updatedPatient);
    }

    public void deletePatient(String id) {
        Patient patient = patientRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        patientRepository.delete(patient);
    }

    public List<PatientResponse> searchPatients(String query) {
        return patientRepository.searchPatients(query).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PatientResponse updateResearchConsent(String id, Boolean consent) {
        Patient patient = patientRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        patient.setResearchConsent(consent);
        if (Boolean.TRUE.equals(consent)) {
            patient.setResearchConsentDate(LocalDateTime.now());
        } else {
            patient.setResearchConsentDate(null);
        }

        Patient updatedPatient = patientRepository.save(patient);
        return mapToResponse(updatedPatient);
    }

    public List<PatientResponse> getPatientsWithResearchConsent() {
        return patientRepository.findByResearchConsentTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private Patient mapToEntity(PatientRequest request) {
        return Patient.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactPhone(request.getEmergencyContactPhone())
                .medicalHistory(request.getMedicalHistory())
                .allergies(request.getAllergies())
                .currentMedications(request.getCurrentMedications())
                .researchConsent(request.getResearchConsent())
                .researchConsentDate(request.getResearchConsentDate())
                .futureContactConsent(request.getFutureContactConsent())
                .anonymizedDataConsent(request.getAnonymizedDataConsent())
                .sampleStorageConsent(request.getSampleStorageConsent())
                .sampleTypes(request.getSampleTypes())
                .storageDuration(request.getStorageDuration())
                .futureResearchUseConsent(request.getFutureResearchUseConsent())
                .destructionConsent(request.getDestructionConsent())
                .consentData(request.getConsentData())
                .build();
    }

    private void updateEntityFromRequest(Patient patient, PatientRequest request) {
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());
        patient.setAddress(request.getAddress());
        patient.setEmergencyContactName(request.getEmergencyContactName());
        patient.setEmergencyContactPhone(request.getEmergencyContactPhone());
        patient.setMedicalHistory(request.getMedicalHistory());
        patient.setAllergies(request.getAllergies());
        patient.setCurrentMedications(request.getCurrentMedications());

        // Update consent fields
        patient.setResearchConsent(request.getResearchConsent());
        patient.setFutureContactConsent(request.getFutureContactConsent());
        patient.setAnonymizedDataConsent(request.getAnonymizedDataConsent());
        patient.setSampleStorageConsent(request.getSampleStorageConsent());
        patient.setSampleTypes(request.getSampleTypes());
        patient.setStorageDuration(request.getStorageDuration());
        patient.setFutureResearchUseConsent(request.getFutureResearchUseConsent());
        patient.setDestructionConsent(request.getDestructionConsent());
        patient.setConsentData(request.getConsentData());

        // Update consent date if consent is given
        if (Boolean.TRUE.equals(request.getResearchConsent()) && patient.getResearchConsentDate() == null) {
            patient.setResearchConsentDate(LocalDateTime.now());
        } else if (Boolean.FALSE.equals(request.getResearchConsent())) {
            patient.setResearchConsentDate(null);
        }
    }

    private PatientResponse mapToResponse(Patient patient) {
        PatientResponse response = new PatientResponse();
        response.setId(patient.getId());
        response.setPatientId(patient.getPatientId());
        response.setFirstName(patient.getFirstName());
        response.setLastName(patient.getLastName());
        response.setDateOfBirth(patient.getDateOfBirth());
        response.setGender(patient.getGender());
        response.setPhone(patient.getPhone());
        response.setEmail(patient.getEmail());
        response.setAddress(patient.getAddress());
        response.setEmergencyContactName(patient.getEmergencyContactName());
        response.setEmergencyContactPhone(patient.getEmergencyContactPhone());
        response.setMedicalHistory(patient.getMedicalHistory());
        response.setAllergies(patient.getAllergies());
        response.setCurrentMedications(patient.getCurrentMedications());

        // Consent fields
        response.setResearchConsent(patient.getResearchConsent());
        response.setResearchConsentDate(patient.getResearchConsentDate());
        response.setFutureContactConsent(patient.getFutureContactConsent());
        response.setAnonymizedDataConsent(patient.getAnonymizedDataConsent());
        response.setSampleStorageConsent(patient.getSampleStorageConsent());
        response.setSampleTypes(patient.getSampleTypes());
        response.setStorageDuration(patient.getStorageDuration());
        response.setFutureResearchUseConsent(patient.getFutureResearchUseConsent());
        response.setDestructionConsent(patient.getDestructionConsent());
        response.setConsentData(patient.getConsentData());

        response.setCreatedAt(patient.getCreatedAt());
        response.setUpdatedAt(patient.getUpdatedAt());

        return response;
    }
}