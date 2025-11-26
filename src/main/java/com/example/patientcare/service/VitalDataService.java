package com.example.patientcare.service;

import com.example.patientcare.dto.request.VitalDataRequest;
import com.example.patientcare.dto.response.VitalDataResponse;
import com.example.patientcare.entity.Patient;
import com.example.patientcare.entity.User;
import com.example.patientcare.entity.VitalData;
import com.example.patientcare.repository.PatientRepository;
import com.example.patientcare.repository.UserRepository;
import com.example.patientcare.repository.VitalDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public VitalDataResponse recordVitalData(VitalDataRequest request, String recordedByUserId) {
        // Find patient by patientId string (like "P208322")
        Patient patient = patientRepository.findByPatientId(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with patient ID: " + request.getPatientId()));

        // Find user by username (authentication.getName() returns username)
        User recordedBy = userRepository.findByUsername(recordedByUserId)
                .orElseThrow(() -> new RuntimeException("User not found: " + recordedByUserId));

        // Create and save vital data
        VitalData vitalData = VitalData.builder()
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
                .build();

        // Validate vital data
        if (!vitalData.isValidVitalData()) {
            throw new RuntimeException("Invalid vital data values");
        }

        VitalData savedVitalData = vitalDataRepository.save(vitalData);
        return convertToResponse(savedVitalData);
    }

    public List<VitalDataResponse> getVitalDataByRecordedBy(String recordedByUserId) {
        // Find user by username (authentication.getName() returns username)
        User recordedBy = userRepository.findByUsername(recordedByUserId)
                .orElseThrow(() -> new RuntimeException("User not found: " + recordedByUserId));

        List<VitalData> vitalDataList = vitalDataRepository.findByRecordedByIdOrderByRecordedAtDesc(recordedBy.getId());

        return vitalDataList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ... rest of your methods remain the same

    private VitalDataResponse convertToResponse(VitalData vitalData) {
        return VitalDataResponse.builder()
                .id(vitalData.getId().toString())
                .patientId(vitalData.getPatient().getPatientId())
                .patientName(vitalData.getPatient().getFirstName() + " " + vitalData.getPatient().getLastName())
                .recordedBy(vitalData.getRecordedBy().getUsername())
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