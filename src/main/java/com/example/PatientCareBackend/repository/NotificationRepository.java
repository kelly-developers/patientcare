package com.example.PatientCareBackend.repository;

import com.example.PatientCareBackend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByDoctorId(Long doctorId);
    List<Notification> findByPatientId(Long patientId);
    List<Notification> findByReadFalse();
    List<Notification> findByType(Notification.NotificationType type);
    List<Notification> findByPriority(Notification.Priority priority);

    @Query("SELECT n FROM Notification n WHERE n.scheduledFor <= :now AND n.read = false")
    List<Notification> findDueNotifications(@Param("now") LocalDateTime now);

    @Query("SELECT n FROM Notification n WHERE n.doctor.id = :doctorId AND n.read = false")
    List<Notification> findUnreadByDoctor(@Param("doctorId") Long doctorId);

    @Query("SELECT n FROM Notification n WHERE n.createdAt >= :since")
    List<Notification> findRecentNotifications(@Param("since") LocalDateTime since);
}