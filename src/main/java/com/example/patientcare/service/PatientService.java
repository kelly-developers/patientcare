package com.example.patientcare.service;

import com.example.patientcare.dto.request.ConsentUpdateRequest;
import com.example.patientcare.dto.request.PatientRequest;
import com.example.patientcare.dto.response.PatientResponse;
import com.example.patientcare.entity.Patient;
import com.example.patientcare.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(PatientResponse::new)
                .collect(Collectors.toList());
    }

    public Page<PatientResponse> getAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable)
                .map(PatientResponse::new);
    }

    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        return new PatientResponse(patient);
    }

    public PatientResponse getPatientByPatientId(String patientId) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with patient ID: " + patientId));
        return new PatientResponse(patient);
    }

    @Transactional
    public PatientResponse createPatient(PatientRequest patientRequest) {
        // Check if patient ID already exists
        if (patientRepository.existsByPatientId(patientRequest.getPatientId())) {
            throw new RuntimeException("Patient ID already exists: " + patientRequest.getPatientId());
        }

        // Check if email already exists
        if (patientRequest.getEmail() != null &&
                patientRepository.existsByEmail(patientRequest.getEmail())) {
            throw new RuntimeException("Email already exists: " + patientRequest.getEmail());
        }

        Patient patient = new Patient();
        updatePatientFromRequest(patient, patientRequest);

        Patient savedPatient = patientRepository.save(patient);
        return new PatientResponse(savedPatient);
    }

    @Transactional
    public PatientResponse updatePatient(Long id, PatientRequest patientRequest) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        // Check if email is being changed and already exists for another patient
        if (patientRequest.getEmail() != null &&
                !patientRequest.getEmail().equals(patient.getEmail()) &&
                patientRepository.existsByEmail(patientRequest.getEmail())) {
            throw new RuntimeException("Email already exists: " + patientRequest.getEmail());
        }

        updatePatientFromRequest(patient, patientRequest);

        Patient updatedPatient = patientRepository.save(patient);
        return new PatientResponse(updatedPatient);
    }

    @Transactional
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        patientRepository.delete(patient);
    }

    public List<PatientResponse> searchPatients(String query) {
        return patientRepository.searchPatients(query).stream()
                .map(PatientResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PatientResponse updateConsent(Long id, ConsentUpdateRequest consentRequest) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        patient.setResearchConsent(consentRequest.getResearchConsent());

        // Set consent date if consent is given and date is provided or null
        if (consentRequest.getResearchConsent() != null && consentRequest.getResearchConsent()) {
            if (consentRequest.getResearchConsentDate() != null) {
                patient.setResearchConsentDate(consentRequest.getResearchConsentDate());
            } else if (patient.getResearchConsentDate() == null) {
                patient.setResearchConsentDate(LocalDateTime.now());
            }
        } else {
            patient.setResearchConsentDate(null);
        }

        Patient updatedPatient = patientRepository.save(patient);
        return new PatientResponse(updatedPatient);
    }

    public List<Patient> getPatientsWithConsent(List<Long> patientIds) {
        if (patientIds == null || patientIds.isEmpty()) {
            return patientRepository.findByResearchConsentTrue();
        } else {
            List<Patient> patients = patientRepository.findByIdIn(patientIds);
            return patients.stream()
                    .filter(Patient::getResearchConsent)
                    .collect(Collectors.toList());
        }
    }

    private void updatePatientFromRequest(Patient patient, PatientRequest request) {
        patient.setPatientId(request.getPatientId());
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

        // Handle research consent separately to maintain proper date handling
        if (request.getResearchConsent() != null) {
            patient.setResearchConsent(request.getResearchConsent());
            if (request.getResearchConsent() && request.getResearchConsentDate() != null) {
                patient.setResearchConsentDate(request.getResearchConsentDate());
            }
        }
    }
}