package com.example.gateway.client;

import com.example.gateway.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MockUserClient {

    private final UserDto currentUser = new UserDto(
            "550e8400-e29b-41d4-a716-446655440000",
            "testclient",
            "client@example.com",
            "CLIENT",
            "ACTIVE"
    );

    public Optional<UserDto> getCurrentUser() {
        return Optional.of(currentUser);
    }
}
