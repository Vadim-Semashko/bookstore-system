package com.example.gateway.client;
import com.example.gateway.dto.RegisterUserInput;
import com.example.gateway.dto.UserDto;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MockUserClient {

    private final Map<String, UserDto> users = new ConcurrentHashMap<>();

    private final Map<String, String> emailIndex = new ConcurrentHashMap<>();

    private final String currentUserId = "550e8400-e29b-41d4-a716-446655440000";

    public MockUserClient() {
        UserDto testUser = new UserDto(
                currentUserId,
                "testclient",
                "client@example.com",
                "CLIENT",
                "ACTIVE"
        );

        users.put(testUser.login(), testUser);
        emailIndex.put(testUser.email(), testUser.login());
    }

    public Optional<UserDto> getCurrentUser() {
        return users.values().stream()
                .filter(user -> user.id().equals(currentUserId))
                .findFirst();
    }

    public synchronized UserDto registerUser(RegisterUserInput input) {

        if (input == null ||
                isBlank(input.login()) ||
                isBlank(input.email()) ||
                isBlank(input.password()) ||
                isBlank(input.firstName()) ||
                isBlank(input.lastName())) {
            throw new IllegalArgumentException("Все обязательные поля должны быть заполнены");
        }

        if (input.login().length() < 3 ||
                input.login().length() > 50) {
            throw new IllegalArgumentException("Логин должен содержать от 3 до 50 символов");
        }

        if (input.password().length() < 8) {
            throw new IllegalArgumentException("Пароль должен содержать минимум 8 символов");
        }

        if (!input.email().matches(
                "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Некорректный email");
        }

        if (users.containsKey(input.login())) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }

        if (emailIndex.containsKey(input.email())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        UserDto user = new UserDto(
                UUID.randomUUID().toString(),
                input.login(),
                input.email(),
                "CLIENT",
                "ACTIVE"
        );

        users.put(user.login(), user);
        emailIndex.put(user.email(), user.login());

        return user;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
