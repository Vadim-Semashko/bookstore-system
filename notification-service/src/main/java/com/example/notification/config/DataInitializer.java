package com.example.notification.config;

import com.example.notification.entity.Notification;
import com.example.notification.repository.NotificationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeNotifications(
            NotificationRepository repository
    ) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            UUID userId = UUID.fromString(
                    "a715cdec-ad7f-438e-b893-e2e35c3c24b2"
            );

            Notification notification1 = new Notification();
            notification1.setUserId(userId);
            notification1.setType("REGISTRATION");
            notification1.setMessage("Добро пожаловать в книжный магазин!");
            notification1.setStatus("SENT");
            notification1.setCreatedAt(LocalDateTime.now().minusDays(2));

            Notification notification2 = new Notification();
            notification2.setUserId(userId);
            notification2.setType("ORDER_STATUS");
            notification2.setMessage("Ваш заказ успешно оплачен.");
            notification2.setStatus("SENT");
            notification2.setCreatedAt(LocalDateTime.now().minusHours(3));

            repository.save(notification1);
            repository.save(notification2);
        };
    }
}
