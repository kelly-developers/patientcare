package com.example.PatientCareBackend.service;

import com.example.PatientCareBackend.dto.request.LabTestRequest;
import com.example.PatientCareBackend.dto.response.LabTestResponse;
import com.example.PatientCareBackend.exception.ResourceNotFoundException;
import com.example.PatientCareBackend.model.LabTest;
import com.example.PatientCareBackend.model.Patient;
import com.example.PatientCareBackend.repository.LabTestRepository;
import com.example.PatientCareBackend.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabTestService {

    private final LabTestRepository labTestRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public LabTestResponse orderLabTest(LabTestRequest labTestRequest) {
        Patient patient = patientRepository.findById(labTestRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + labTestRequest.getPatientId()));

        LabTest labTest = new LabTest();
        labTest.setPatient(patient);
        labTest.setTestType(labTestRequest.getTestType());
        labTest.setTestName(labTestRequest.getTestName());
        labTest.setOrderedBy(labTestRequest.getOrderedBy());
        labTest.setOrderedDate(labTestRequest.getOrderedDate());
        labTest.setStatus(labTestRequest.getStatus());
        labTest.setPriority(labTestRequest.getPriority());
        labTest.setResults(labTestRequest.getResults());
        labTest.setClinicalNotes(labTestRequest.getClinicalNotes());
        labTest.setNotes(labTestRequest.getNotes());
        labTest.setReportDate(labTestRequest.getReportDate());
        labTest.setCompletedDate(labTestRequest.getCompletedDate());

        LabTest savedLabTest = labTestRepository.save(labTest);
        return mapToResponse(savedLabTest);
    }

    @Transactional(readOnly = true)
    public List<LabTestResponse> getAllLabTests() {
        return labTestRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LabTestResponse getLabTestById(Long id) {
        LabTest labTest = labTestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab test not found with id: " + id));
        return mapToResponse(labTest);
    }

    @Transactional
    public LabTestResponse updateTestStatus(Long id, LabTest.TestStatus status, String results) {
        LabTest labTest = labTestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab test not found with id: " + id));

        labTest.setStatus(status);
        labTest.setResults(results);

        if (status == LabTest.TestStatus.COMPLETED) {
            labTest.setCompletedDate(LocalDateTime.now());
            if (labTest.getReportDate() == null) {
                labTest.setReportDate(LocalDateTime.now());
            }
        }

        LabTest updatedLabTest = labTestRepository.save(labTest);
        return mapToResponse(updatedLabTest);
    }

    @Transactional(readOnly = true)
    public List<LabTestResponse> getLabTestsByPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }

        return labTestRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LabTestResponse> getLabTestsByStatus(LabTest.TestStatus status) {
        return labTestRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LabTestResponse> getUrgentLabTests() {
        return labTestRepository.findPendingUrgentTests().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LabTestResponse> getLabTestsByType(String testType) {
        return labTestRepository.findByTestType(testType).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LabTestResponse> getLabTestsOrderedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return labTestRepository.findTestsOrderedBetween(startDate, endDate).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public LabTestResponse updateLabTest(Long id, LabTestRequest labTestRequest) {
        LabTest labTest = labTestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab test not found with id: " + id));

        Patient patient = patientRepository.findById(labTestRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + labTestRequest.getPatientId()));

        labTest.setPatient(patient);
        labTest.setTestType(labTestRequest.getTestType());
        labTest.setTestName(labTestRequest.getTestName());
        labTest.setOrderedBy(labTestRequest.getOrderedBy());
        labTest.setOrderedDate(labTestRequest.getOrderedDate());
        labTest.setStatus(labTestRequest.getStatus());
        labTest.setPriority(labTestRequest.getPriority());
        labTest.setResults(labTestRequest.getResults());
        labTest.setClinicalNotes(labTestRequest.getClinicalNotes());
        labTest.setNotes(labTestRequest.getNotes());
        labTest.setReportDate(labTestRequest.getReportDate());
        labTest.setCompletedDate(labTestRequest.getCompletedDate());

        LabTest updatedLabTest = labTestRepository.save(labTest);
        return mapToResponse(updatedLabTest);
    }

    @Transactional
    public void deleteLabTest(Long id) {
        LabTest labTest = labTestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab test not found with id: " + id));
        labTestRepository.delete(labTest);
    }

    private LabTestResponse mapToResponse(LabTest labTest) {
        return new LabTestResponse(
                labTest.getId(),
                mapToPatientResponse(labTest.getPatient()),
                labTest.getTestType(),
                labTest.getTestName(),
                labTest.getOrderedBy(),
                labTest.getOrderedDate(),
                labTest.getStatus(),
                labTest.getPriority(),
                labTest.getResults(),
                labTest.getClinicalNotes(),
                labTest.getNotes(),
                labTest.getReportDate(),
                labTest.getCompletedDate(),
                labTest.getCreatedAt()
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
                patient.getResearchConsent(),
                patient.getSampleStorageConsent(),
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }
}