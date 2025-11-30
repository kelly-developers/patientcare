package com.example.PatientCareBackend.service;

import com.example.PatientCareBackend.dto.request.PatientRequest;
import com.example.PatientCareBackend.dto.response.PatientResponse;
import com.example.PatientCareBackend.exception.ResourceNotFoundException;
import com.example.PatientCareBackend.exception.ValidationException;
import com.example.PatientCareBackend.model.Patient;
import com.example.PatientCareBackend.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    @Transactional(readOnly = true)
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        return mapToResponse(patient);
    }

    @Transactional
    public PatientResponse createPatient(PatientRequest patientRequest) {
        if (patientRepository.existsByPatientId(patientRequest.getPatientId())) {
            throw new ValidationException("Patient ID already exists: " + patientRequest.getPatientId());
        }

        Patient patient = new Patient();
        mapRequestToEntity(patientRequest, patient);

        Patient savedPatient = patientRepository.save(patient);
        return mapToResponse(savedPatient);
    }

    @Transactional
    public PatientResponse updatePatient(Long id, PatientRequest patientRequest) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        if (!patient.getPatientId().equals(patientRequest.getPatientId()) &&
                patientRepository.existsByPatientId(patientRequest.getPatientId())) {
            throw new ValidationException("Patient ID already exists: " + patientRequest.getPatientId());
        }

        mapRequestToEntity(patientRequest, patient);
        Patient updatedPatient = patientRepository.save(patient);
        return mapToResponse(updatedPatient);
    }

    @Transactional
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        patientRepository.delete(patient);
    }

    @Transactional(readOnly = true)
    public List<PatientResponse> searchPatients(String query) {
        return patientRepository.searchPatients(query).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatientResponse getPatientByPatientId(String patientId) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));
        return mapToResponse(patient);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return patientRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public long getTotalPatients() {
        return patientRepository.count();
    }

    @Transactional(readOnly = true)
    public List<PatientResponse> getPatientsWithResearchConsent() {
        return patientRepository.findPatientsWithResearchConsent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PatientResponse mapToResponse(Patient patient) {
        return new PatientResponse(
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
                patient.getResearchConsent(),
                patient.getSampleStorageConsent(),
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }

    private void mapRequestToEntity(PatientRequest request, Patient patient) {
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
        patient.setResearchConsent(request.getResearchConsent());
        patient.setSampleStorageConsent(request.getSampleStorageConsent());
    }
}