package com.example.PatientCareBackend.controller;

import com.example.PatientCareBackend.dto.request.AppointmentRequest;
import com.example.PatientCareBackend.dto.response.AppointmentResponse;
import com.example.PatientCareBackend.model.Appointment;
import com.example.PatientCareBackend.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        List<AppointmentResponse> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Long id) {
        AppointmentResponse appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(@Valid @RequestBody AppointmentRequest appointmentRequest) {
        AppointmentResponse createdAppointment = appointmentService.createAppointment(appointmentRequest);
        return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponse> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRequest appointmentRequest) {
        AppointmentResponse updatedAppointment = appointmentService.updateAppointment(id, appointmentRequest);
        return ResponseEntity.ok(updatedAppointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AppointmentResponse> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam Appointment.AppointmentStatus status) {
        AppointmentResponse updatedAppointment = appointmentService.updateAppointmentStatus(id, status);
        return ResponseEntity.ok(updatedAppointment);
    }

    @PutMapping("/{id}/arrival")
    public ResponseEntity<AppointmentResponse> updateArrivalStatus(
            @PathVariable Long id,
            @RequestParam Appointment.ArrivalStatus arrivalStatus) {
        AppointmentResponse updatedAppointment = appointmentService.updateArrivalStatus(id, arrivalStatus);
        return ResponseEntity.ok(updatedAppointment);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByPatient(@PathVariable Long patientId) {
        List<AppointmentResponse> appointments = appointmentService.getAppointmentsByPatient(patientId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        List<AppointmentResponse> appointments = appointmentService.getAppointmentsByDoctor(doctorId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByDate(@PathVariable LocalDate date) {
        List<AppointmentResponse> appointments = appointmentService.getAppointmentsByDate(date);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/patient/{patientId}/upcoming")
    public ResponseEntity<List<AppointmentResponse>> getUpcomingAppointmentsByPatient(@PathVariable Long patientId) {
        List<AppointmentResponse> appointments = appointmentService.getUpcomingAppointmentsByPatient(patientId);
        return ResponseEntity.ok(appointments);
    }
}