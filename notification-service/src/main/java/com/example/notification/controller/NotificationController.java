package com.example.notification.controller;

import com.example.notification.dto.NotificationDto;
import com.example.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public List<NotificationDto> getNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> getNotification(
            @PathVariable UUID id
    ) {
        return ResponseEntity.of(
                notificationService.getNotificationById(id)
        );
    }

    @GetMapping("/user/{userId}")
    public List<NotificationDto> getUserNotifications(
            @PathVariable UUID userId
    ) {
        return notificationService.getNotificationsByUserId(userId);
    }
}
