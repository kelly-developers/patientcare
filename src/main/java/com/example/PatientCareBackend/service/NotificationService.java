package com.example.PatientCareBackend.service;

import com.example.PatientCareBackend.dto.request.NotificationRequest;
import com.example.PatientCareBackend.dto.response.NotificationResponse;
import com.example.PatientCareBackend.exception.ResourceNotFoundException;
import com.example.PatientCareBackend.model.Appointment;
import com.example.PatientCareBackend.model.Notification;
import com.example.PatientCareBackend.model.Patient;
import com.example.PatientCareBackend.model.User;
import com.example.PatientCareBackend.repository.AppointmentRepository;
import com.example.PatientCareBackend.repository.NotificationRepository;
import com.example.PatientCareBackend.repository.PatientRepository;
import com.example.PatientCareBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreadNotifications() {
        return notificationRepository.findByReadFalse().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public NotificationResponse markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));

        notification.setRead(true);
        Notification updatedNotification = notificationRepository.save(notification);
        return mapToResponse(updatedNotification);
    }

    @Transactional
    public void markAllAsRead() {
        User currentUser = jwtService.getCurrentUser();
        List<Notification> unreadNotifications = notificationRepository.findUnreadByDoctor(currentUser.getId());

        unreadNotifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }

    @Transactional
    public void deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        notificationRepository.delete(notification);
    }

    @Transactional
    public NotificationResponse createNotification(NotificationRequest notificationRequest) {
        Notification notification = new Notification();
        notification.setType(notificationRequest.getType());
        notification.setTitle(notificationRequest.getTitle());
        notification.setMessage(notificationRequest.getMessage());
        notification.setPriority(notificationRequest.getPriority());
        notification.setRead(false);
        notification.setScheduledFor(notificationRequest.getScheduledFor());

        // Set optional relationships
        if (notificationRequest.getDoctorId() != null) {
            User doctor = userRepository.findById(notificationRequest.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + notificationRequest.getDoctorId()));
            notification.setDoctor(doctor);
        }

        if (notificationRequest.getPatientId() != null) {
            Patient patient = patientRepository.findById(notificationRequest.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + notificationRequest.getPatientId()));
            notification.setPatient(patient);
        }

        if (notificationRequest.getAppointmentId() != null) {
            Appointment appointment = appointmentRepository.findById(notificationRequest.getAppointmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + notificationRequest.getAppointmentId()));
            notification.setAppointment(appointment);
        }

        Notification savedNotification = notificationRepository.save(notification);
        return mapToResponse(savedNotification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return notificationRepository.findByDoctorId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void sendEmergencyAlert(String title, String message, Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        // Get all available doctors
        List<User> availableDoctors = userRepository.findAvailableDoctors();

        for (User doctor : availableDoctors) {
            Notification notification = new Notification();
            notification.setType(Notification.NotificationType.EMERGENCY);
            notification.setTitle(title);
            notification.setMessage(message + " - Patient: " + patient.getFirstName() + " " + patient.getLastName());
            notification.setPriority(Notification.Priority.URGENT);
            notification.setRead(false);
            notification.setDoctor(doctor);
            notification.setPatient(patient);

            notificationRepository.save(notification);
        }
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getDueNotifications() {
        return notificationRepository.findDueNotifications(LocalDateTime.now()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getRecentNotifications(int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return notificationRepository.findRecentNotifications(since).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long getUnreadCountByUser(Long userId) {
        return notificationRepository.findUnreadByDoctor(userId).size();
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getType(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getDoctor() != null ? mapToUserResponse(notification.getDoctor()) : null,
                notification.getPatient() != null ? mapToPatientResponse(notification.getPatient()) : null,
                notification.getAppointment() != null ? mapToAppointmentResponse(notification.getAppointment()) : null,
                (com.example.PatientCareBackend.model.Notification.Priority) notification.getPriority(), // Fixed: Cast to correct type
                notification.getRead(),
                notification.getScheduledFor(),
                notification.getCreatedAt()
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

    private com.example.PatientCareBackend.dto.response.AppointmentResponse mapToAppointmentResponse(Appointment appointment) {
        com.example.PatientCareBackend.dto.response.AppointmentResponse response =
                new com.example.PatientCareBackend.dto.response.AppointmentResponse();

        response.setId(appointment.getId());
        response.setPatient(mapToPatientResponse(appointment.getPatient()));
        response.setDoctor(mapToUserResponse(appointment.getDoctor()));
        response.setAppointmentDate(appointment.getAppointmentDate());
        response.setAppointmentTime(appointment.getAppointmentTime());
        response.setType(appointment.getType());
        response.setStatus(appointment.getStatus());
        response.setReason(appointment.getReason());
        response.setNotes(appointment.getNotes());
        response.setPriority((com.example.PatientCareBackend.model.Appointment.Priority) appointment.getPriority()); // Fixed: Cast to correct type
        response.setArrivalStatus(appointment.getArrivalStatus());
        response.setCreatedAt(appointment.getCreatedAt());

        return response;
    }
}