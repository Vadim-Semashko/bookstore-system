package com.example.notification.service;

import com.example.notification.dto.CreateNotificationRequest;
import com.example.notification.dto.NotificationDto;
import com.example.notification.entity.Notification;
import com.example.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationDto> getAllNotifications() {
        return notificationRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<NotificationDto> getNotificationById(UUID id) {
        return notificationRepository.findById(id)
                .map(this::toDto);
    }

    public List<NotificationDto> getNotificationsByUserId(UUID userId) {
        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public NotificationDto createNotification(CreateNotificationRequest request) {
        if (request == null || request.userId() == null ||
                request.type() == null || request.type().isBlank() ||
                request.message() == null || request.message().isBlank()) {
            throw new IllegalArgumentException("Некорректные данные уведомления");
        }

        Notification notification = new Notification();
        notification.setUserId(request.userId());
        notification.setType(request.type());
        notification.setMessage(request.message());
        notification.setStatus("UNREAD");
        notification.setCreatedAt(LocalDateTime.now());

        return toDto(notificationRepository.save(notification));
    }

    @Transactional
    public NotificationDto markNotificationRead(UUID notificationId, UUID userId) {
        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Уведомление не найдено"));

        if (!notification.getUserId().equals(userId)) {
            throw new IllegalStateException("Уведомление принадлежит другому пользователю");
        }

        if ("READ".equals(notification.getStatus())) {
            throw new IllegalStateException("Уведомление уже прочитано");
        }

        if (!"UNREAD".equals(notification.getStatus()) && !"SENT".equals(notification.getStatus())) {
            throw new IllegalStateException("Уведомление нельзя отметить как прочитанное");
        }

        notification.setStatus("READ");

        return toDto(notificationRepository.save(notification));
    }

    private NotificationDto toDto(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getUserId(),
                notification.getType(),
                notification.getMessage(),
                notification.getStatus(),
                notification.getCreatedAt()
        );
    }
}
