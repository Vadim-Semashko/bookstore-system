package com.example.user.dto;

import java.util.UUID;

public record UserDto(
        UUID id,
        String login,
        String email,
        String role,
        String status
) {}
