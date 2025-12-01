package com.example.PatientCareBackend.service;

import com.example.PatientCareBackend.dto.request.SurgeryRequest;
import com.example.PatientCareBackend.dto.response.SurgeryResponse;
import com.example.PatientCareBackend.exception.ResourceNotFoundException;
import com.example.PatientCareBackend.model.Patient;
import com.example.PatientCareBackend.model.Surgery;
import com.example.PatientCareBackend.repository.PatientRepository;
import com.example.PatientCareBackend.repository.SurgeryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurgeryService {

    private final SurgeryRepository surgeryRepository;
    private final PatientRepository patientRepository;

    @Transactional(readOnly = true)
    public List<SurgeryResponse> getAllSurgeries() {
        return surgeryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SurgeryResponse getSurgeryById(Long id) {
        Surgery surgery = surgeryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Surgery not found with id: " + id));
        return mapToResponse(surgery);
    }

    @Transactional
    public SurgeryResponse createSurgery(SurgeryRequest surgeryRequest) {
        Patient patient = patientRepository.findById(surgeryRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + surgeryRequest.getPatientId()));

        Surgery surgery = new Surgery();
        surgery.setPatient(patient);
        surgery.setProcedureName(surgeryRequest.getProcedureName());
        surgery.setUrgency(surgeryRequest.getUrgency());
        surgery.setRecommendedBy(surgeryRequest.getRecommendedBy());
        surgery.setDiagnosis(surgeryRequest.getDiagnosis());
        surgery.setStatus(surgeryRequest.getStatus());
        surgery.setConsentDate(surgeryRequest.getConsentDate());
        surgery.setScheduledDate(surgeryRequest.getScheduledDate());
        surgery.setActualDate(surgeryRequest.getActualDate());
        surgery.setCompletedDate(surgeryRequest.getCompletedDate());
        surgery.setSurgeonName(surgeryRequest.getSurgeonName());
        surgery.setDurationMinutes(surgeryRequest.getDurationMinutes());

        Surgery savedSurgery = surgeryRepository.save(surgery);
        return mapToResponse(savedSurgery);
    }

    @Transactional
    public SurgeryResponse updateSurgery(Long id, SurgeryRequest surgeryRequest) {
        Surgery surgery = surgeryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Surgery not found with id: " + id));

        Patient patient = patientRepository.findById(surgeryRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + surgeryRequest.getPatientId()));

        surgery.setPatient(patient);
        surgery.setProcedureName(surgeryRequest.getProcedureName());
        surgery.setUrgency(surgeryRequest.getUrgency());
        surgery.setRecommendedBy(surgeryRequest.getRecommendedBy());
        surgery.setDiagnosis(surgeryRequest.getDiagnosis());
        surgery.setStatus(surgeryRequest.getStatus());
        surgery.setConsentDate(surgeryRequest.getConsentDate());
        surgery.setScheduledDate(surgeryRequest.getScheduledDate());
        surgery.setActualDate(surgeryRequest.getActualDate());
        surgery.setCompletedDate(surgeryRequest.getCompletedDate());
        surgery.setSurgeonName(surgeryRequest.getSurgeonName());
        surgery.setDurationMinutes(surgeryRequest.getDurationMinutes());

        Surgery updatedSurgery = surgeryRepository.save(surgery);
        return mapToResponse(updatedSurgery);
    }

    @Transactional
    public void deleteSurgery(Long id) {
        Surgery surgery = surgeryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Surgery not found with id: " + id));
        surgeryRepository.delete(surgery);
    }

    @Transactional
    public SurgeryResponse updateSurgeryStatus(Long id, Surgery.SurgeryStatus status) {
        Surgery surgery = surgeryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Surgery not found with id: " + id));

        surgery.setStatus(status);

        // Update relevant dates based on status
        if (status == Surgery.SurgeryStatus.IN_PROGRESS) {
            surgery.setActualDate(LocalDateTime.now());
        } else if (status == Surgery.SurgeryStatus.COMPLETED) {
            surgery.setCompletedDate(LocalDateTime.now());
        }

        Surgery updatedSurgery = surgeryRepository.save(surgery);
        return mapToResponse(updatedSurgery);
    }

    @Transactional(readOnly = true)
    public List<SurgeryResponse> getSurgeriesByPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }

        return surgeryRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SurgeryResponse> getPendingConsentSurgeries() {
        return surgeryRepository.findPendingConsentSurgeries().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SurgeryResponse> getSurgeriesByStatus(Surgery.SurgeryStatus status) {
        return surgeryRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SurgeryResponse> getSurgeriesBySurgeon(String surgeonName) {
        return surgeryRepository.findBySurgeonName(surgeonName).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SurgeryResponse> getSurgeriesBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return surgeryRepository.findSurgeriesBetweenDates(startDate, endDate).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private SurgeryResponse mapToResponse(Surgery surgery) {
        return new SurgeryResponse(
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
                patient.getConsentAccepted(),  // ✅ Position 15
                patient.getConsentFormPath(),  // ✅ Position 16
                patient.getResearchConsent(),  // ✅ Position 17
                patient.getSampleStorageConsent(),  // ✅ Position 18
                patient.getCreatedAt(),        // ✅ Position 19
                patient.getUpdatedAt()         // ✅ Position 20
        );
    }
}