package com.example.PatientCareBackend.controller;

import com.example.PatientCareBackend.dto.request.NotificationRequest;
import com.example.PatientCareBackend.dto.response.NotificationResponse;
import com.example.PatientCareBackend.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        List<NotificationResponse> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications() {
        List<NotificationResponse> notifications = notificationService.getUnreadNotifications();
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Long id) {
        NotificationResponse notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(notification);
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead() {
        notificationService.markAllAsRead();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(@Valid @RequestBody NotificationRequest notificationRequest) {
        NotificationResponse notification = notificationService.createNotification(notificationRequest);
        return new ResponseEntity<>(notification, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByUser(@PathVariable Long userId) {
        List<NotificationResponse> notifications = notificationService.getNotificationsByUser(userId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/emergency")
    public ResponseEntity<Void> sendEmergencyAlert(
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam Long patientId) {
        notificationService.sendEmergencyAlert(title, message, patientId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/due")
    public ResponseEntity<List<NotificationResponse>> getDueNotifications() {
        List<NotificationResponse> notifications = notificationService.getDueNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<NotificationResponse>> getRecentNotifications(@RequestParam(defaultValue = "24") int hours) {
        List<NotificationResponse> notifications = notificationService.getRecentNotifications(hours);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Long> getUnreadCountByUser(@PathVariable Long userId) {
        long count = notificationService.getUnreadCountByUser(userId);
        return ResponseEntity.ok(count);
    }
}