package com.example.PatientCareBackend.service;

import com.example.PatientCareBackend.dto.request.PostOperativeRequest;
import com.example.PatientCareBackend.dto.response.PostOperativeResponse;
import com.example.PatientCareBackend.exception.ResourceNotFoundException;
import com.example.PatientCareBackend.model.Patient;
import com.example.PatientCareBackend.model.PostOperative;
import com.example.PatientCareBackend.model.Surgery;
import com.example.PatientCareBackend.repository.PatientRepository;
import com.example.PatientCareBackend.repository.PostOperativeRepository;
import com.example.PatientCareBackend.repository.SurgeryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostOperativeService {

    private final PostOperativeRepository postOperativeRepository;
    private final PatientRepository patientRepository;
    private final SurgeryRepository surgeryRepository;

    @Transactional
    public PostOperativeResponse recordFollowup(PostOperativeRequest followupRequest) {
        Patient patient = patientRepository.findById(followupRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + followupRequest.getPatientId()));

        Surgery surgery = surgeryRepository.findById(followupRequest.getSurgeryId())
                .orElseThrow(() -> new ResourceNotFoundException("Surgery not found with id: " + followupRequest.getSurgeryId()));

        PostOperative followup = new PostOperative();
        followup.setPatient(patient);
        followup.setSurgery(surgery);
        followup.setFollowupType(followupRequest.getFollowupType());
        followup.setSymptoms(followupRequest.getSymptoms());
        followup.setImprovements(followupRequest.getImprovements());
        followup.setConcerns(followupRequest.getConcerns());
        followup.setNextVisitDate(followupRequest.getNextVisitDate());
        followup.setMedicationAdherence(followupRequest.getMedicationAdherence());
        followup.setNotes(followupRequest.getNotes());

        PostOperative savedFollowup = postOperativeRepository.save(followup);
        return mapToResponse(savedFollowup);
    }

    @Transactional(readOnly = true)
    public List<PostOperativeResponse> getPatientFollowups(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }

        return postOperativeRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostOperativeResponse> getSurgeryFollowups(Long surgeryId) {
        if (!surgeryRepository.existsById(surgeryId)) {
            throw new ResourceNotFoundException("Surgery not found with id: " + surgeryId);
        }

        return postOperativeRepository.findBySurgeryId(surgeryId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostOperativeResponse getFollowupById(Long id) {
        PostOperative followup = postOperativeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post-operative followup not found with id: " + id));
        return mapToResponse(followup);
    }

    @Transactional(readOnly = true)
    public List<PostOperativeResponse> getFollowupsByType(PostOperative.FollowupType followupType) {
        return postOperativeRepository.findByFollowupType(followupType).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostOperativeResponse> getNonAdherentPatients() {
        return postOperativeRepository.findNonAdherentPatients().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostOperativeResponse> getOverdueFollowups() {
        return postOperativeRepository.findOverdueFollowups().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostOperativeResponse updateFollowup(Long id, PostOperativeRequest followupRequest) {
        PostOperative followup = postOperativeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post-operative followup not found with id: " + id));

        Patient patient = patientRepository.findById(followupRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + followupRequest.getPatientId()));

        Surgery surgery = surgeryRepository.findById(followupRequest.getSurgeryId())
                .orElseThrow(() -> new ResourceNotFoundException("Surgery not found with id: " + followupRequest.getSurgeryId()));

        followup.setPatient(patient);
        followup.setSurgery(surgery);
        followup.setFollowupType(followupRequest.getFollowupType());
        followup.setSymptoms(followupRequest.getSymptoms());
        followup.setImprovements(followupRequest.getImprovements());
        followup.setConcerns(followupRequest.getConcerns());
        followup.setNextVisitDate(followupRequest.getNextVisitDate());
        followup.setMedicationAdherence(followupRequest.getMedicationAdherence());
        followup.setNotes(followupRequest.getNotes());

        PostOperative updatedFollowup = postOperativeRepository.save(followup);
        return mapToResponse(updatedFollowup);
    }

    private PostOperativeResponse mapToResponse(PostOperative followup) {
        return new PostOperativeResponse(
                followup.getId(),
                mapToPatientResponse(followup.getPatient()),
                mapToSurgeryResponse(followup.getSurgery()),
                followup.getFollowupType(),
                followup.getSymptoms(),
                followup.getImprovements(),
                followup.getConcerns(),
                followup.getNextVisitDate(),
                followup.getMedicationAdherence(),
                followup.getNotes(),
                followup.getCreatedAt()
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
}