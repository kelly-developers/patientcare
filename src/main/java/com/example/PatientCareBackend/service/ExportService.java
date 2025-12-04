package com.example.PatientCareBackend.service;

import com.example.PatientCareBackend.dto.response.*;
import com.example.PatientCareBackend.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final PatientRepository patientRepository;
    private final SurgeryRepository surgeryRepository;
    private final AppointmentRepository appointmentRepository;
    private final LabTestRepository labTestRepository;
    private final PharmacyRepository pharmacyRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public ByteArrayInputStream exportPatients(String format) {
        List<PatientResponse> patients = getPatientsForExport(null, null);

        if ("csv".equalsIgnoreCase(format)) {
            return exportToCSV(patients, PatientResponse.class);
        } else {
            return exportToJSON(patients);
        }
    }

    @Transactional(readOnly = true)
    public ByteArrayInputStream exportSurgeries(String format) {
        List<SurgeryResponse> surgeries = getSurgeriesForExport(null, null);

        if ("csv".equalsIgnoreCase(format)) {
            return exportToCSV(surgeries, SurgeryResponse.class);
        } else {
            return exportToJSON(surgeries);
        }
    }

    @Transactional(readOnly = true)
    public ByteArrayInputStream exportAppointments(String format) {
        List<AppointmentResponse> appointments = getAppointmentsForExport(null, null);

        if ("csv".equalsIgnoreCase(format)) {
            return exportToCSV(appointments, AppointmentResponse.class);
        } else {
            return exportToJSON(appointments);
        }
    }

    @Transactional(readOnly = true)
    public ByteArrayInputStream exportLabTests(String format) {
        List<LabTestResponse> labTests = labTestRepository.findAll().stream()
                .map(this::mapToLabTestResponse)
                .collect(Collectors.toList());

        if ("csv".equalsIgnoreCase(format)) {
            return exportToCSV(labTests, LabTestResponse.class);
        } else {
            return exportToJSON(labTests);
        }
    }

    @Transactional(readOnly = true)
    public ByteArrayInputStream exportPrescriptions(String format) {
        List<PharmacyResponse> prescriptions = pharmacyRepository.findAll().stream()
                .map(this::mapToPharmacyResponse)
                .collect(Collectors.toList());

        if ("csv".equalsIgnoreCase(format)) {
            return exportToCSV(prescriptions, PharmacyResponse.class);
        } else {
            return exportToJSON(prescriptions);
        }
    }

    @Transactional(readOnly = true)
    public List<PatientResponse> getPatientsForExport(LocalDateTime startDate, LocalDateTime endDate) {
        return patientRepository.findAll().stream()
                .map(this::mapToPatientResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SurgeryResponse> getSurgeriesForExport(LocalDateTime startDate, LocalDateTime endDate) {
        List<SurgeryResponse> surgeries;

        if (startDate != null && endDate != null) {
            surgeries = surgeryRepository.findSurgeriesBetweenDates(startDate, endDate).stream()
                    .map(this::mapToSurgeryResponse)
                    .collect(Collectors.toList());
        } else {
            surgeries = surgeryRepository.findAll().stream()
                    .map(this::mapToSurgeryResponse)
                    .collect(Collectors.toList());
        }

        return surgeries;
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsForExport(LocalDateTime startDate, LocalDateTime endDate) {
        List<AppointmentResponse> appointments;

        if (startDate != null && endDate != null) {
            appointments = appointmentRepository.findAppointmentsBetweenDates(
                            startDate.toLocalDate(), endDate.toLocalDate()).stream()
                    .map(this::mapToAppointmentResponse)
                    .collect(Collectors.toList());
        } else {
            appointments = appointmentRepository.findAll().stream()
                    .map(this::mapToAppointmentResponse)
                    .collect(Collectors.toList());
        }

        return appointments;
    }

    private <T> ByteArrayInputStream exportToCSV(List<T> data, Class<T> clazz) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVWriter writer = new CSVWriter(new OutputStreamWriter(out))) {

            if (!data.isEmpty()) {
                // Write header
                String[] header = getCSVHeaders(clazz);
                writer.writeNext(header);

                // Write data
                for (T item : data) {
                    String[] row = convertToCSVRow(item);
                    writer.writeNext(row);
                }
            }

            writer.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data to CSV", e);
        }
    }

    private <T> ByteArrayInputStream exportToJSON(List<T> data) {
        try {
            String json = objectMapper.writeValueAsString(data);
            return new ByteArrayInputStream(json.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data to JSON", e);
        }
    }

    private String[] getCSVHeaders(Class<?> clazz) {
        if (clazz == PatientResponse.class) {
            return new String[]{
                    "ID", "Patient ID", "First Name", "Last Name", "Date of Birth", "Gender",
                    "Phone", "Email", "Address", "Emergency Contact", "Emergency Phone",
                    "Medical History", "Allergies", "Current Medications", "Consent Accepted",
                    "Consent Form Path", "Research Consent", "Sample Storage Consent", "Created At"
            };
        } else if (clazz == SurgeryResponse.class) {
            return new String[]{
                    "ID", "Patient Name", "Procedure Name", "Urgency", "Recommended By",
                    "Diagnosis", "Status", "Consent Date", "Scheduled Date", "Actual Date",
                    "Completed Date", "Surgeon Name", "Duration Minutes", "Created At"
            };
        } else if (clazz == AppointmentResponse.class) {
            return new String[]{
                    "ID", "Patient Name", "Doctor Name", "Appointment Date", "Appointment Time",
                    "Type", "Status", "Reason", "Priority", "Arrival Status", "Created At"
            };
        } else if (clazz == LabTestResponse.class) {
            return new String[]{
                    "ID", "Patient Name", "Test Type", "Test Name", "Ordered By", "Ordered Date",
                    "Status", "Priority", "Results", "Clinical Notes", "Report Date", "Completed Date"
            };
        } else if (clazz == PharmacyResponse.class) {
            return new String[]{
                    "ID", "Patient Name", "Doctor Name", "Medication Name", "Dosage", "Frequency",
                    "Duration", "Instructions", "Status", "Dispensed At", "Collected At", "Created At"
            };
        }

        return new String[0];
    }

    private String[] convertToCSVRow(Object item) {
        if (item instanceof PatientResponse) {
            PatientResponse patient = (PatientResponse) item;
            return new String[]{
                    patient.getId().toString(),
                    patient.getPatientId(),
                    patient.getFirstName(),
                    patient.getLastName(),
                    patient.getDateOfBirth() != null ? patient.getDateOfBirth().toString() : "",
                    patient.getGender() != null ? patient.getGender().toString() : "",
                    patient.getPhone(),
                    patient.getEmail(),
                    patient.getAddress(),
                    patient.getEmergencyContactName(),
                    patient.getEmergencyContactPhone(),
                    patient.getMedicalHistory(),
                    patient.getAllergies(),
                    patient.getCurrentMedications(),
                    patient.getConsentAccepted() != null ? patient.getConsentAccepted().toString() : "",
                    patient.getConsentFormPath(),
                    patient.getResearchConsent() != null ? patient.getResearchConsent().toString() : "",
                    patient.getSampleStorageConsent() != null ? patient.getSampleStorageConsent().toString() : "",
                    patient.getCreatedAt() != null ? patient.getCreatedAt().toString() : ""
            };
        } else if (item instanceof SurgeryResponse) {
            SurgeryResponse surgery = (SurgeryResponse) item;
            return new String[]{
                    surgery.getId().toString(),
                    surgery.getPatient() != null ? surgery.getPatient().getFirstName() + " " + surgery.getPatient().getLastName() : "",
                    surgery.getProcedureName(),
                    surgery.getUrgency() != null ? surgery.getUrgency().toString() : "",
                    surgery.getRecommendedBy(),
                    surgery.getDiagnosis(),
                    surgery.getStatus() != null ? surgery.getStatus().toString() : "",
                    surgery.getConsentDate() != null ? surgery.getConsentDate().toString() : "",
                    surgery.getScheduledDate() != null ? surgery.getScheduledDate().toString() : "",
                    surgery.getActualDate() != null ? surgery.getActualDate().toString() : "",
                    surgery.getCompletedDate() != null ? surgery.getCompletedDate().toString() : "",
                    surgery.getSurgeonName(),
                    surgery.getDurationMinutes() != null ? surgery.getDurationMinutes().toString() : "",
                    surgery.getCreatedAt() != null ? surgery.getCreatedAt().toString() : ""
            };
        } else if (item instanceof AppointmentResponse) {
            AppointmentResponse appointment = (AppointmentResponse) item;
            return new String[]{
                    appointment.getId().toString(),
                    appointment.getPatient() != null ? appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName() : "",
                    appointment.getDoctor() != null ? appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName() : "",
                    appointment.getAppointmentDate() != null ? appointment.getAppointmentDate().toString() : "",
                    appointment.getAppointmentTime() != null ? appointment.getAppointmentTime().toString() : "",
                    appointment.getType() != null ? appointment.getType().toString() : "",
                    appointment.getStatus() != null ? appointment.getStatus().toString() : "",
                    appointment.getReason(),
                    appointment.getPriority() != null ? appointment.getPriority().toString() : "",
                    appointment.getArrivalStatus() != null ? appointment.getArrivalStatus().toString() : "",
                    appointment.getCreatedAt() != null ? appointment.getCreatedAt().toString() : ""
            };
        } else if (item instanceof LabTestResponse) {
            LabTestResponse labTest = (LabTestResponse) item;
            return new String[]{
                    labTest.getId().toString(),
                    labTest.getPatient() != null ? labTest.getPatient().getFirstName() + " " + labTest.getPatient().getLastName() : "",
                    labTest.getTestType() != null ? labTest.getTestType().toString() : "",
                    labTest.getTestName(),
                    labTest.getOrderedBy(),
                    labTest.getOrderedDate() != null ? labTest.getOrderedDate().toString() : "",
                    labTest.getStatus() != null ? labTest.getStatus().toString() : "",
                    labTest.getPriority() != null ? labTest.getPriority().toString() : "",
                    labTest.getResults(),
                    labTest.getClinicalNotes(),
                    labTest.getReportDate() != null ? labTest.getReportDate().toString() : "",
                    labTest.getCompletedDate() != null ? labTest.getCompletedDate().toString() : ""
            };
        } else if (item instanceof PharmacyResponse) {
            PharmacyResponse pharmacy = (PharmacyResponse) item;
            return new String[]{
                    pharmacy.getId().toString(),
                    pharmacy.getPatient() != null ? pharmacy.getPatient().getFirstName() + " " + pharmacy.getPatient().getLastName() : "",
                    pharmacy.getDoctor() != null ? pharmacy.getDoctor().getFirstName() + " " + pharmacy.getDoctor().getLastName() : "",
                    pharmacy.getMedicationName(),
                    pharmacy.getDosage(),
                    pharmacy.getFrequency(),
                    pharmacy.getDuration(),
                    pharmacy.getInstructions(),
                    pharmacy.getStatus() != null ? pharmacy.getStatus().toString() : "",
                    pharmacy.getDispensedAt() != null ? pharmacy.getDispensedAt().toString() : "",
                    pharmacy.getCollectedAt() != null ? pharmacy.getCollectedAt().toString() : "",
                    pharmacy.getCreatedAt() != null ? pharmacy.getCreatedAt().toString() : ""
            };
        }

        return new String[0];
    }

    // Mapping methods for different entity types
    private PatientResponse mapToPatientResponse(com.example.PatientCareBackend.model.Patient patient) {
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
                patient.getConsentAccepted(),
                patient.getConsentFormPath(),
                patient.getResearchConsent(),
                patient.getSampleStorageConsent(),
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }

    private SurgeryResponse mapToSurgeryResponse(com.example.PatientCareBackend.model.Surgery surgery) {
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

    private AppointmentResponse mapToAppointmentResponse(com.example.PatientCareBackend.model.Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setPatient(mapToPatientResponse(appointment.getPatient()));
        response.setDoctor(mapToUserResponse(appointment.getDoctor()));
        response.setAppointmentDate(appointment.getAppointmentDate());
        response.setAppointmentTime(appointment.getAppointmentTime());
        response.setType(appointment.getType());
        response.setStatus(appointment.getStatus());
        response.setReason(appointment.getReason());
        response.setNotes(appointment.getNotes());
        response.setPriority(appointment.getPriority());
        response.setArrivalStatus(appointment.getArrivalStatus());
        response.setCreatedAt(appointment.getCreatedAt());
        return response;
    }

    private LabTestResponse mapToLabTestResponse(com.example.PatientCareBackend.model.LabTest labTest) {
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

    private PharmacyResponse mapToPharmacyResponse(com.example.PatientCareBackend.model.Pharmacy pharmacy) {
        return new PharmacyResponse(
                pharmacy.getId(),
                mapToPatientResponse(pharmacy.getPatient()),
                mapToUserResponse(pharmacy.getDoctor()),
                pharmacy.getMedicationName(),
                pharmacy.getDosage(),
                pharmacy.getFrequency(),
                pharmacy.getDuration(),
                pharmacy.getInstructions(),
                pharmacy.getStatus(),
                pharmacy.getDispensedAt(),
                pharmacy.getCollectedAt(),
                pharmacy.getCreatedAt()
        );
    }

    private com.example.PatientCareBackend.dto.response.UserResponse mapToUserResponse(com.example.PatientCareBackend.model.User user) {
        return new com.example.PatientCareBackend.dto.response.UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getSpecialty(),
                user.getAvailable(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}