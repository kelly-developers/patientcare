package com.example.PatientCareBackend.service;

import com.example.PatientCareBackend.dto.request.AppointmentRequest;
import com.example.PatientCareBackend.dto.response.AppointmentResponse;
import com.example.PatientCareBackend.exception.ResourceNotFoundException;
import com.example.PatientCareBackend.exception.ValidationException;
import com.example.PatientCareBackend.model.Appointment;
import com.example.PatientCareBackend.model.Patient;
import com.example.PatientCareBackend.model.User;
import com.example.PatientCareBackend.repository.AppointmentRepository;
import com.example.PatientCareBackend.repository.PatientRepository;
import com.example.PatientCareBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
        return mapToResponse(appointment);
    }

    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest appointmentRequest) {
        Patient patient = patientRepository.findById(appointmentRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + appointmentRequest.getPatientId()));

        User doctor = userRepository.findById(appointmentRequest.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + appointmentRequest.getDoctorId()));

        if (doctor.getRole() != User.Role.DOCTOR) {
            throw new ValidationException("User is not a doctor: " + doctor.getUsername());
        }

        List<Appointment> conflictingAppointments = appointmentRepository
                .findByDateAndDoctor(appointmentRequest.getAppointmentDate(), doctor.getId());

        if (!conflictingAppointments.isEmpty()) {
            throw new ValidationException("Doctor already has appointments scheduled for this date");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(appointmentRequest.getAppointmentDate());
        appointment.setAppointmentTime(appointmentRequest.getAppointmentTime());
        appointment.setType(appointmentRequest.getType());
        appointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);
        appointment.setReason(appointmentRequest.getReason());
        appointment.setNotes(appointmentRequest.getNotes());
        appointment.setPriority(appointmentRequest.getPriority());
        appointment.setArrivalStatus(Appointment.ArrivalStatus.PENDING);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapToResponse(savedAppointment);
    }

    @Transactional
    public AppointmentResponse updateAppointment(Long id, AppointmentRequest appointmentRequest) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        Patient patient = patientRepository.findById(appointmentRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + appointmentRequest.getPatientId()));

        User doctor = userRepository.findById(appointmentRequest.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + appointmentRequest.getDoctorId()));

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(appointmentRequest.getAppointmentDate());
        appointment.setAppointmentTime(appointmentRequest.getAppointmentTime());
        appointment.setType(appointmentRequest.getType());
        appointment.setReason(appointmentRequest.getReason());
        appointment.setNotes(appointmentRequest.getNotes());
        appointment.setPriority(appointmentRequest.getPriority());

        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return mapToResponse(updatedAppointment);
    }

    @Transactional
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
        appointmentRepository.delete(appointment);
    }

    @Transactional
    public AppointmentResponse updateAppointmentStatus(Long id, Appointment.AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        appointment.setStatus(status);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return mapToResponse(updatedAppointment);
    }

    @Transactional
    public AppointmentResponse updateArrivalStatus(Long id, Appointment.ArrivalStatus arrivalStatus) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        appointment.setArrivalStatus(arrivalStatus);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return mapToResponse(updatedAppointment);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }

        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId) {
        if (!userRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }

        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findByAppointmentDate(date).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getUpcomingAppointmentsByPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }

        return appointmentRepository.findUpcomingAppointmentsByPatient(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AppointmentResponse mapToResponse(Appointment appointment) {
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

        // Fixed: Use fully qualified name to avoid conflict
        response.setPriority((com.example.PatientCareBackend.model.Appointment.Priority) appointment.getPriority());

        response.setArrivalStatus(appointment.getArrivalStatus());
        response.setCreatedAt(appointment.getCreatedAt());

        return response;
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

    private com.example.PatientCareBackend.dto.response.UserResponse mapToUserResponse(User user) {
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