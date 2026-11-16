package com.example.gateway.resolver;

import com.example.gateway.client.MockNotificationClient;
import com.example.gateway.client.MockUserClient;
import com.example.gateway.dto.CreateNotificationInput;
import com.example.gateway.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class NotificationMutationResolver {

    private final MockNotificationClient notificationClient;
    private final MockUserClient userClient;

    @MutationMapping
    public NotificationDto createNotification(@Argument CreateNotificationInput input) {
        return notificationClient.createNotification(input);
    }

    @MutationMapping
    public NotificationDto markNotificationRead(@Argument String notificationId) {
        String userId = userClient.getCurrentUser()
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"))
                .id();

        return notificationClient.markNotificationRead(notificationId, userId);
    }
}
