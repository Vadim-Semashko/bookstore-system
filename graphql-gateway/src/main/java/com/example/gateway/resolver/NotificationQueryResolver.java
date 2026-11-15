package com.example.gateway.resolver;

import com.example.gateway.client.MockNotificationClient;
import com.example.gateway.client.MockUserClient;
import com.example.gateway.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationQueryResolver {

    private final MockNotificationClient notificationClient;
    private final MockUserClient userClient;

    @QueryMapping
    public List<NotificationDto> myNotifications() {
        return userClient.getCurrentUser()
                .map(user -> notificationClient
                        .getNotificationsByUserId(user.id()))
                .orElseGet(List::of);
    }
}
