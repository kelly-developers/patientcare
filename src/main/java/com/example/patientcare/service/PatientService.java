package com.example.patientcare.service;

import com.example.patientcare.dto.request.PatientRequest;
import com.example.patientcare.dto.response.PatientResponse;
import com.example.patientcare.entity.Patient;
import com.example.patientcare.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Transactional
    public PatientResponse createPatient(PatientRequest request) {
        try {
            // Validate date of birth
            if (request.getDateOfBirth() != null && request.getDateOfBirth().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Date of birth cannot be in the future");
            }

            // Validate required fields
            if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
                throw new IllegalArgumentException("First name is required");
            }
            if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
                throw new IllegalArgumentException("Last name is required");
            }
            if (request.getGender() == null) {
                throw new IllegalArgumentException("Gender is required");
            }

            Patient patient = mapToEntity(request);
            patient.generatePatientId();

            // Set consent timestamp if research consent is given
            if (Boolean.TRUE.equals(request.getResearchConsent()) && request.getResearchConsentDate() == null) {
                patient.setResearchConsentDate(LocalDateTime.now());
            }

            Patient savedPatient = patientRepository.save(patient);
            return mapToResponse(savedPatient);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create patient: " + e.getMessage(), e);
        }
    }

    @Transactional
    public PatientResponse updatePatient(String id, PatientRequest request) {
        try {
            Patient existingPatient = patientRepository.findById(UUID.fromString(id))
                    .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

            // Validate date of birth
            if (request.getDateOfBirth() != null && request.getDateOfBirth().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Date of birth cannot be in the future");
            }

            updateEntityFromRequest(existingPatient, request);
            Patient updatedPatient = patientRepository.save(existingPatient);
            return mapToResponse(updatedPatient);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update patient: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deletePatient(String id) {
        try {
            Patient patient = patientRepository.findById(UUID.fromString(id))
                    .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
            patientRepository.delete(patient);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete patient: " + e.getMessage(), e);
        }
    }

    public List<PatientResponse> searchPatients(String query) {
        return patientRepository.searchPatients(query).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PatientResponse updateResearchConsent(String id, Boolean consent) {
        try {
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
        } catch (Exception e) {
            throw new RuntimeException("Failed to update research consent: " + e.getMessage(), e);
        }
    }

    public List<PatientResponse> getPatientsWithResearchConsent() {
        return patientRepository.findByResearchConsentTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private Patient mapToEntity(PatientRequest request) {
        return Patient.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .phone(request.getPhone() != null ? request.getPhone().trim() : null)
                .email(request.getEmail() != null ? request.getEmail().trim() : null)
                .address(request.getAddress() != null ? request.getAddress().trim() : null)
                .emergencyContactName(request.getEmergencyContactName() != null ? request.getEmergencyContactName().trim() : null)
                .emergencyContactPhone(request.getEmergencyContactPhone() != null ? request.getEmergencyContactPhone().trim() : null)
                .medicalHistory(request.getMedicalHistory() != null ? request.getMedicalHistory().trim() : null)
                .allergies(request.getAllergies() != null ? request.getAllergies().trim() : null)
                .currentMedications(request.getCurrentMedications() != null ? request.getCurrentMedications().trim() : null)
                .researchConsent(request.getResearchConsent() != null ? request.getResearchConsent() : false)
                .researchConsentDate(request.getResearchConsentDate())
                .futureContactConsent(request.getFutureContactConsent() != null ? request.getFutureContactConsent() : false)
                .anonymizedDataConsent(request.getAnonymizedDataConsent() != null ? request.getAnonymizedDataConsent() : false)
                .sampleStorageConsent(request.getSampleStorageConsent() != null ? request.getSampleStorageConsent() : false)
                .sampleTypes(request.getSampleTypes())
                .storageDuration(request.getStorageDuration())
                .futureResearchUseConsent(request.getFutureResearchUseConsent() != null ? request.getFutureResearchUseConsent() : false)
                .destructionConsent(request.getDestructionConsent() != null ? request.getDestructionConsent() : false)
                .consentData(request.getConsentData())
                .build();
    }

    private void updateEntityFromRequest(Patient patient, PatientRequest request) {
        if (request.getFirstName() != null) {
            patient.setFirstName(request.getFirstName().trim());
        }
        if (request.getLastName() != null) {
            patient.setLastName(request.getLastName().trim());
        }
        if (request.getDateOfBirth() != null) {
            patient.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            patient.setGender(request.getGender());
        }
        if (request.getPhone() != null) {
            patient.setPhone(request.getPhone().trim());
        }
        if (request.getEmail() != null) {
            patient.setEmail(request.getEmail().trim());
        }
        if (request.getAddress() != null) {
            patient.setAddress(request.getAddress().trim());
        }
        if (request.getEmergencyContactName() != null) {
            patient.setEmergencyContactName(request.getEmergencyContactName().trim());
        }
        if (request.getEmergencyContactPhone() != null) {
            patient.setEmergencyContactPhone(request.getEmergencyContactPhone().trim());
        }
        if (request.getMedicalHistory() != null) {
            patient.setMedicalHistory(request.getMedicalHistory().trim());
        }
        if (request.getAllergies() != null) {
            patient.setAllergies(request.getAllergies().trim());
        }
        if (request.getCurrentMedications() != null) {
            patient.setCurrentMedications(request.getCurrentMedications().trim());
        }

        // Update consent fields
        if (request.getResearchConsent() != null) {
            patient.setResearchConsent(request.getResearchConsent());
        }
        if (request.getFutureContactConsent() != null) {
            patient.setFutureContactConsent(request.getFutureContactConsent());
        }
        if (request.getAnonymizedDataConsent() != null) {
            patient.setAnonymizedDataConsent(request.getAnonymizedDataConsent());
        }
        if (request.getSampleStorageConsent() != null) {
            patient.setSampleStorageConsent(request.getSampleStorageConsent());
        }
        if (request.getSampleTypes() != null) {
            patient.setSampleTypes(request.getSampleTypes());
        }
        if (request.getStorageDuration() != null) {
            patient.setStorageDuration(request.getStorageDuration());
        }
        if (request.getFutureResearchUseConsent() != null) {
            patient.setFutureResearchUseConsent(request.getFutureResearchUseConsent());
        }
        if (request.getDestructionConsent() != null) {
            patient.setDestructionConsent(request.getDestructionConsent());
        }
        if (request.getConsentData() != null) {
            patient.setConsentData(request.getConsentData());
        }

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