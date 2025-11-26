package com.example.patientcare.service;

import com.example.patientcare.dto.request.VitalDataRequest;
import com.example.patientcare.dto.response.VitalDataResponse;
import com.example.patientcare.entity.Patient;
import com.example.patientcare.entity.User;
import com.example.patientcare.entity.VitalData;
import com.example.patientcare.exception.ResourceNotFoundException;
import com.example.patientcare.repository.PatientRepository;
import com.example.patientcare.repository.UserRepository;
import com.example.patientcare.repository.VitalDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VitalDataService {

    private final VitalDataRepository vitalDataRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Transactional
    public VitalDataResponse recordVitalData(VitalDataRequest request, String recordedByUserId) {
        try {
            // Validate and find patient using your existing repository method
            Patient patient = patientRepository.findByPatientId(request.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + request.getPatientId()));

            // Validate and find user (nurse/doctor recording the data)
            User recordedBy = userRepository.findById(UUID.fromString(recordedByUserId))
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + recordedByUserId));

            // Create vital data entity
            VitalData vitalData = mapToEntity(request, patient, recordedBy);

            // Validate vital data ranges
            if (!vitalData.isValidVitalData()) {
                throw new IllegalArgumentException("One or more vital signs are outside valid ranges");
            }

            VitalData savedVitalData = vitalDataRepository.save(vitalData);
            return mapToResponse(savedVitalData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to record vital data: " + e.getMessage(), e);
        }
    }

    public VitalDataResponse getVitalDataById(String id) {
        VitalData vitalData = vitalDataRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Vital data not found with ID: " + id));
        return mapToResponse(vitalData);
    }

    public List<VitalDataResponse> getVitalDataByPatientId(String patientId) {
        // Use your existing findByPatientId method
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));

        return vitalDataRepository.findByPatientIdOrderByRecordedAtDesc(patient.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<VitalDataResponse> getVitalDataByPatientUUID(String patientUUID) {
        return vitalDataRepository.findByPatientIdOrderByRecordedAtDesc(UUID.fromString(patientUUID))
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Page<VitalDataResponse> getVitalDataByPatientIdPaginated(String patientId, Pageable pageable) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));

        return vitalDataRepository.findByPatientIdOrderByRecordedAtDesc(patient.getId(), pageable)
                .map(this::mapToResponse);
    }

    public List<VitalDataResponse> getVitalDataByRecordedBy(String recordedByUserId) {
        return vitalDataRepository.findByRecordedByIdOrderByRecordedAtDesc(UUID.fromString(recordedByUserId))
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<VitalDataResponse> getVitalDataByPatientAndDateRange(String patientId, LocalDateTime startDate, LocalDateTime endDate) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));

        return vitalDataRepository.findByPatientIdAndDateRange(patient.getId(), startDate, endDate)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public VitalDataResponse getLatestVitalDataByPatientId(String patientId) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));

        VitalData vitalData = vitalDataRepository.findLatestByPatientId(patient.getId());
        if (vitalData == null) {
            throw new ResourceNotFoundException("No vital data found for patient: " + patientId);
        }
        return mapToResponse(vitalData);
    }

    @Transactional
    public void deleteVitalData(String id) {
        try {
            VitalData vitalData = vitalDataRepository.findById(UUID.fromString(id))
                    .orElseThrow(() -> new ResourceNotFoundException("Vital data not found with ID: " + id));
            vitalDataRepository.delete(vitalData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete vital data: " + e.getMessage(), e);
        }
    }

    // Additional method to get vital data count by patient
    public Long getVitalDataCountByPatientId(String patientId) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));
        return vitalDataRepository.countByPatientId(patient.getId());
    }

    private VitalData mapToEntity(VitalDataRequest request, Patient patient, User recordedBy) {
        return VitalData.builder()
                .patient(patient)
                .recordedBy(recordedBy)
                .systolicBP(request.getSystolicBP())
                .diastolicBP(request.getDiastolicBP())
                .heartRate(request.getHeartRate())
                .respiratoryRate(request.getRespiratoryRate())
                .temperature(request.getTemperature())
                .oxygenSaturation(request.getOxygenSaturation())
                .height(request.getHeight())
                .weight(request.getWeight())
                .bloodGlucose(request.getBloodGlucose())
                .painLevel(request.getPainLevel())
                .notes(request.getNotes())
                .recordedAt(LocalDateTime.now())
                .build();
    }

    private VitalDataResponse mapToResponse(VitalData vitalData) {
        return VitalDataResponse.builder()
                .id(vitalData.getId())
                .patientId(vitalData.getPatient().getPatientId())
                .patientName(vitalData.getPatient().getFirstName() + " " + vitalData.getPatient().getLastName())
                .recordedByName(vitalData.getRecordedBy().getFirstName() + " " + vitalData.getRecordedBy().getLastName())
                .systolicBP(vitalData.getSystolicBP())
                .diastolicBP(vitalData.getDiastolicBP())
                .heartRate(vitalData.getHeartRate())
                .respiratoryRate(vitalData.getRespiratoryRate())
                .temperature(vitalData.getTemperature())
                .oxygenSaturation(vitalData.getOxygenSaturation())
                .height(vitalData.getHeight())
                .weight(vitalData.getWeight())
                .bloodGlucose(vitalData.getBloodGlucose())
                .painLevel(vitalData.getPainLevel())
                .bmi(vitalData.calculateBMI())
                .bloodPressure(vitalData.getBloodPressure())
                .notes(vitalData.getNotes())
                .recordedAt(vitalData.getRecordedAt())
                .updatedAt(vitalData.getUpdatedAt())
                .build();
    }
}