package com.example.PatientCareBackend.service;

import com.example.PatientCareBackend.dto.request.ConsentRequest;
import com.example.PatientCareBackend.dto.response.ConsentResponse;
import com.example.PatientCareBackend.exception.ResourceNotFoundException;
import com.example.PatientCareBackend.model.Consent;
import com.example.PatientCareBackend.model.Surgery;
import com.example.PatientCareBackend.repository.ConsentRepository;
import com.example.PatientCareBackend.repository.SurgeryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsentService {

    private final ConsentRepository consentRepository;
    private final SurgeryRepository surgeryRepository;

    private final String UPLOAD_DIR = "uploads/consents/";

    @Transactional
    public ConsentResponse submitConsent(ConsentRequest consentRequest) {
        Surgery surgery = surgeryRepository.findById(consentRequest.getSurgeryId())
                .orElseThrow(() -> new ResourceNotFoundException("Surgery not found with id: " + consentRequest.getSurgeryId()));

        Consent consent = new Consent();
        consent.setSurgery(surgery);
        consent.setPatientName(consentRequest.getPatientName());
        consent.setNextOfKin(consentRequest.getNextOfKin());
        consent.setNextOfKinPhone(consentRequest.getNextOfKinPhone());
        consent.setUnderstoodRisks(consentRequest.getUnderstoodRisks());
        consent.setUnderstoodBenefits(consentRequest.getUnderstoodBenefits());
        consent.setUnderstoodAlternatives(consentRequest.getUnderstoodAlternatives());
        consent.setConsentToSurgery(consentRequest.getConsentToSurgery());
        consent.setSignature(consentRequest.getSignature());
        consent.setConsentDecision(consentRequest.getConsentDecision());
        consent.setConsentFilePath(consentRequest.getConsentFilePath());

        Consent savedConsent = consentRepository.save(consent);

        // Update surgery consent date if consent is accepted
        if (consentRequest.getConsentDecision() == Consent.ConsentDecision.ACCEPTED) {
            surgery.setConsentDate(java.time.LocalDateTime.now());
            surgeryRepository.save(surgery);
        }

        return mapToResponse(savedConsent);
    }

    @Transactional(readOnly = true)
    public ConsentResponse getConsentBySurgery(Long surgeryId) {
        Consent consent = consentRepository.findBySurgeryId(surgeryId)
                .orElseThrow(() -> new ResourceNotFoundException("Consent not found for surgery id: " + surgeryId));
        return mapToResponse(consent);
    }

    @Transactional
    public ConsentResponse uploadConsentFile(Long consentId, MultipartFile file) {
        Consent consent = consentRepository.findById(consentId)
                .orElseThrow(() -> new ResourceNotFoundException("Consent not found with id: " + consentId));

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

            consent.setConsentFilePath(filePath.toString());
            Consent updatedConsent = consentRepository.save(consent);

            return mapToResponse(updatedConsent);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload consent file", e);
        }
    }

    @Transactional(readOnly = true)
    public List<ConsentResponse> getStoredConsentForms() {
        return consentRepository.findAll().stream()
                .filter(consent -> consent.getConsentFilePath() != null)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean hasValidConsent(Long surgeryId) {
        return consentRepository.findBySurgeryId(surgeryId)
                .map(consent -> consent.getConsentDecision() == Consent.ConsentDecision.ACCEPTED)
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public List<ConsentResponse> getConsentsByDecision(Consent.ConsentDecision decision) {
        return consentRepository.findByConsentDecision(decision).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ConsentResponse mapToResponse(Consent consent) {
        return new ConsentResponse(
                consent.getId(),
                mapToSurgeryResponse(consent.getSurgery()),
                consent.getPatientName(),
                consent.getNextOfKin(),
                consent.getNextOfKinPhone(),
                consent.getUnderstoodRisks(),
                consent.getUnderstoodBenefits(),
                consent.getUnderstoodAlternatives(),
                consent.getConsentToSurgery(),
                consent.getSignature(),
                consent.getConsentDecision(),
                consent.getConsentFilePath(),
                consent.getCreatedAt()
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

    private com.example.PatientCareBackend.dto.response.PatientResponse mapToPatientResponse(com.example.PatientCareBackend.model.Patient patient) {
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
                patient.getConsentAccepted(),
                patient.getConsentFormPath(),
                patient.getResearchConsent(),
                patient.getSampleStorageConsent(),
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }
}